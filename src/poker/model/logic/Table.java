package poker.model.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import poker.gui.GameWindow;
import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Card;
import poker.model.GameState;
import poker.model.PlayerState;
import poker.model.logic.showdown.HandWinner;

/**
 * Represents one Tournament, i.e. a sequence of hands until a player has won all chips.
 * 
 * @author Becker
 */
public class Table {

    /** Default value for the initial stack size. */
    public static final int DEFAULT_START_STACK = 1000;
    /** Default value for the first blind. */
    public static final int DEFAULT_START_BLIND = 20;
    /** Default value for the number of hands to play before increasing blinds. */
    public static final int DEFAULT_INCREASE_BLIND = 5;

    private static boolean debugPrint = false;

    private static enum Street {
        PREFLOP, FLOP, TURN, RIVER
    }

    private final Deck deck = new Deck();

    private AbstractPlayer[] players;
    private final int playerCount;
    private final boolean updateGui;
    private final PlayerState[] playerStates;
    private Integer[] stacks;

    private Card[] board;
    private Card[][] holeCards;
    private Street street;
    private int currentSmallBlind;
    private int currentBigBlind;
    private final int increaseBlind;
    private int lastBlindIncreasementRound = -1;
    private int dealer;
    private int playerOnAction;
    private int minimumRaise;
    private ArrayList<Pot> pots;
    private ArrayList<MergedPot> mergedPots;

    private final ArrayList<Action> lastActionsComplete;
    private final Integer[] lastActionsIndicesForPlayer;
    private final ArrayList<Integer> ranking;

    /**
     * Constructor using default values for {@link Table#DEFAULT_START_STACK Stack Size},
     * {@link Table#DEFAULT_START_BLIND Start Blind} and {@link Table#DEFAULT_INCREASE_BLIND Number
     * of Hands until increasing Blinds}. <br>
     * <br>
     * Creates a new Tournament/Table and waits for a call to {@link Table#run()}. The dealer
     * position is randomly chosen.
     * 
     * @param players
     *            An array of players that participate at the tournament.
     * @param updateGui
     *            Should the GUI be updated?
     * @see Table#Table(AbstractPlayer[], int, int, int, boolean)
     */
    public Table(AbstractPlayer[] players, boolean updateGui) {
        this(players, DEFAULT_START_STACK, DEFAULT_START_BLIND, DEFAULT_INCREASE_BLIND, updateGui);
    }

    /**
     * Constructor with maximal flexibility. <br>
     * <br>
     * Creates a new Tournament/Table and waits for a call to {@link Table#run()}. The dealer
     * position is randomly chosen.
     * 
     * @param players
     *            An array of players that participate at the tournament.
     * @param startStack
     *            The initial stack of each player.
     * @param startBlind
     *            The size of the first blind.
     * @param increaseBlind
     *            The number of hands to play before increasing blinds.
     * @param updateGui
     *            Should the GUI be updated?
     */
    public Table(AbstractPlayer[] players, int startStack, int startBlind, int increaseBlind, boolean updateGui) {
        this.players = players;
        this.playerCount = players.length;
        this.increaseBlind = increaseBlind;
        this.updateGui = updateGui;

        this.stacks = new Integer[playerCount];
        this.playerStates = new PlayerState[playerCount];
        this.holeCards = new Card[playerCount][2];
        this.lastActionsComplete = new ArrayList<Action>();
        this.lastActionsIndicesForPlayer = new Integer[playerCount];
        this.ranking = new ArrayList<Integer>();

        ArrayList<Integer> availablePositions = new ArrayList<Integer>();
        for (int i = 0; i < playerCount; ++i) {
            if (players[i] != null) {
                this.stacks[i] = startStack;
                this.playerStates[i] = PlayerState.WAITING_FOR_OWN_TURN;
                this.lastActionsIndicesForPlayer[i] = 0;
                availablePositions.add(i);
            } else {
                this.stacks[i] = null;
                this.playerStates[i] = PlayerState.BUSTED;
            }
        }

        /* The dealer is randomly chosen. */
        Collections.shuffle(availablePositions);
        this.dealer = availablePositions.get(0);

        setBlinds(startBlind);
    }

    public AbstractPlayer[] getPlayers() {
        return players;
    }

    public Integer[] getStacks() {
        return stacks;
    }

    public Integer[] getRanking() {
        return ranking.toArray(new Integer[ranking.size()]);
    }

    private GameState createGameState() {
        int potSize = 0;
        for (MergedPot mergedPot : mergedPots) {
            potSize += mergedPot.getSize();
        }

        int[] chipsInPot = new int[playerCount];
        for (int i = 0; i < playerCount; ++i) {
            for (Pot pot : pots) {
                chipsInPot[i] += pot.getSize(i);
            }
        }

        return new GameState(getNumberOfRemainingPlayers(), potSize, dealer, stacks.clone(), chipsInPot, playerStates.clone(), board.clone(), currentBigBlind, minimumRaise);
    }

    /**
     * Main loop for one tournament.<br>
     * <br>
     * This is the sequence for one tournament:
     * <ul>
     * <li>Call each player's {@link AbstractPlayer#tableStarted(int) tableStarted(...)} along with
     * its position at the table</li>
     * <li><b>While:</b> There are at least two players left with chips</li>
     * <li><b>Do:</b> Call the {@link #playHand()} method!</li>
     * <li>Call each player's {@link AbstractPlayer#tableFinished(int, Integer[])
     * tableFinished(...)}</li>
     * </ul>
     * 
     * @see AbstractPlayer
     * @see #playHand()
     */
    public void run() {
        for (int i = 0; i < players.length; ++i) {
            if (players[i] != null) players[i].tableStarted(i);
        }

        /* Play hands until there is only one player left. */
        while (true) {
            playHand();

            int playerCount = getNumberOfRemainingPlayers();
            if (playerCount == 1) break;

            debugMessage("There are " + playerCount + " players remaining.");
        }

        /* There is only one player left! Determine the winner! */
        for (int i = 0; i < playerCount; ++i) {
            if (stacks[i] == null) continue;
            ranking.add(0, i);
            break;
        }

        debugMessage("The table is finished. The player " + ranking.get(0) + " has won the game!");
        updateGui();

        Integer[] ranking = getRanking();
        for (int i = 0; i < players.length; ++i) {
            if (players[i] != null) players[i].tableFinished(i, ranking);
        }
    }

    /**
     * Main loop for one hand.<br>
     * <br>
     * This is the sequence for one hand:
     * <ul>
     * <li>Call each player's {@link AbstractPlayer#handStarted(int, GameState, Card[])
     * handStarted(...)}</li>
     * <li><b>While:</b> The hand isn't finished (e.g. a betting round isn't finished yet)</li>
     * <li><b>Do:</b> Consecutively request the player's actions by calling
     * {@link AbstractPlayer#getAction(int, GameState, Card[], int) getAction(...)}</li>
     * <li>Call each player's {@link AbstractPlayer#handFinished(int, GameState) handFinished(...)}</li>
     * </ul>
     * 
     * @see AbstractPlayer
     */
    public void playHand() {
        deck.reset();

        mergedPots = new ArrayList<MergedPot>();

        pots = new ArrayList<Pot>();
        int size[] = new int[playerCount];
        boolean[] isPlayerInPot = new boolean[playerCount];
        for (int i = 0; i < playerCount; ++i) {
            isPlayerInPot[i] = (stacks[i] != null);
        }
        pots.add(new Pot(size, isPlayerInPot));

        /* Which players are still in the game? */
        for (int i = 0; i < playerCount; ++i) {
            if (stacks[i] != null) {
                playerStates[i] = PlayerState.WAITING_FOR_OWN_TURN;
            } else {
                playerStates[i] = PlayerState.BUSTED;
            }
        }

        dealer = nextPlayer(dealer);
        playerOnAction = nextPlayer(dealer);

        minimumRaise = 0;
        street = Street.PREFLOP;

        increaseBlinds();

        dealHoleCards();

        /* Inform players about new hand. */
        GameState gsStart = createGameState();
        for (int i = 0; i < playerCount; ++i) {
            if (players[i] == null) continue;
            players[i].handStarted(i, gsStart, holeCards[i]);
        }

        /* Debug Print */
        int sumOfChips = 0;
        for (int i = 0; i < playerCount; ++i) {
            if (stacks[i] == null) continue;
            debugMessage("The player " + i + " has " + stacks[i] + " chips.");
            sumOfChips += stacks[i];
        }
        debugMessage("There is a total amount of " + sumOfChips + " chips in the game.");

        setBlinds();

        updateGui();

        /* All players are All-In after setting the blinds! Showdown! */
        if (isActionClosed()) {
            debugMessage("All players are All-In after setting the blinds!");
        } else {
            resetPlayerStates();

            /* Play hand. */
            while (true) {
                /* Request and process a valid action. */
                processAction(getValidAction());
                updateGui();

                /* The only remaining player has to check. */
                if (isJustOnePlayerLeft() && getChipsToCall() == 0) {
                    debugMessage("All players have folded or are All-In!");
                    break;
                }

                /* Is the betting round finished? */
                if (isActionClosed()) {
                    resetPlayerStates();

                    if (isJustOnePlayerLeft()) {
                        debugMessage("Just one player can act!");
                        break;
                    }

                    if (street == Street.RIVER) {
                        debugMessage("The last betting round is finished. Showdown!");
                        break;
                    }

                    /* Merge the pots. */
                    mergePots();

                    /* Deal the next street. */
                    nextStreet();

                    playerOnAction = nextPlayer(dealer);

                    updateGui();
                }
            }
        }

        /* Showdown! Determine winner. */
        endHand();

        GameState gsEnd = createGameState();
        for (int i = 0; i < playerCount; ++i) {
            if (players[i] == null) continue;
            gsEnd.setLastActions(new ArrayList<Action>(lastActionsComplete.subList(lastActionsIndicesForPlayer[i], lastActionsComplete.size())));
            players[i].handFinished(i, gsEnd);

            lastActionsIndicesForPlayer[i] = 0;
        }
        lastActionsComplete.clear();
    }

    private void updateGui() {
        if (updateGui) GameWindow.getInstance().updateComponents(createGameState());
    }

    private void dealHoleCards() {
        board = new Card[5];

        for (int i = 0; i < playerCount; ++i) {
            if (stacks[i] == null) {
                holeCards[i] = null;
            } else {
                holeCards[i] = new Card[2];
            }
        }

        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < playerCount; ++i) {
                if (holeCards[i] == null) continue;

                holeCards[i][j] = deck.getCard();
            }
        }
    }

    private void setBlinds() {
        int stackSB = stacks[playerOnAction];
        if (stackSB <= currentSmallBlind) {
            debugMessage("Player " + playerOnAction + " bets " + stackSB + " and is All-In (SmallBlind)");

            performRaise(stackSB);
        } else {
            debugMessage("Player " + playerOnAction + " bets " + currentSmallBlind + " (SmallBlind)");

            performRaise(currentSmallBlind);
        }

        int stackBB = stacks[playerOnAction];
        if (stackBB <= currentBigBlind) {
            debugMessage("Player " + playerOnAction + " bets " + stackBB + " and is All-In (BigBlind)");

            if (stackBB <= getChipsToCall()) {
                performCall();
            } else {
                performRaise(stackBB);
            }
        } else {
            debugMessage("Player " + playerOnAction + " bets " + currentBigBlind + " (BigBlind)");

            performRaise(currentBigBlind);
        }
    }

    /**
     * Sets the values for the current small- and bigblind. The value for the smallblind is the half
     * of the parameter and the bigblind is exactly the parameter.
     * 
     * @param bigBlind
     *            New value for the bigblind.
     */
    private void setBlinds(int bigBlind) {
        this.currentSmallBlind = bigBlind / 2;
        this.currentBigBlind = bigBlind;
    }

    private void increaseBlinds() {
        ++lastBlindIncreasementRound;

        if (lastBlindIncreasementRound < increaseBlind) return;
        lastBlindIncreasementRound = 0;

        setBlinds(2 * currentBigBlind);

        debugMessage("The Blinds were raised to " + currentSmallBlind + " / " + currentBigBlind + ".");
    }

    private int nextPlayer(int position) {
        int i = (position + 1) % playerCount;
        while (i != position) {
            if (playerStates[i] == PlayerState.WAITING_FOR_OWN_TURN) return i;
            i = (i + 1) % playerCount;
        }
        return -1;
    }

    private void processAction(Action action) {
        switch (action.getAction()) {
        case FOLD:
            performFold();
            break;
        case CALL:
            performCall();
            break;
        case RAISE:
            performRaise(action.getAmount());
            break;
        }
    }

    private Action getValidAction() {
        AbstractPlayer player = players[playerOnAction];

        GameState gs = createGameState();
        gs.setLastActions(new ArrayList<Action>(lastActionsComplete.subList(lastActionsIndicesForPlayer[playerOnAction], lastActionsComplete.size())));

        Card[] cards = holeCards[playerOnAction];

        int callSize = getChipsToCall();

        Action action;
        do {
            action = player.getAction(playerOnAction, gs, cards, callSize);
        } while (!isValidAction(action, callSize));

        lastActionsComplete.add(action);

        debugMessage(action);

        return action;
    }

    private boolean isValidAction(Action action, int callSize) {
        if (action == null) {
            debugMessage("Es wurde eine Null-Action zurueckgegeben!");
            return false;
        }

        action.setPosition(playerOnAction);
        action.setCallSize(callSize);

        switch (action.getAction()) {
        case FOLD:
            return true;
        case CALL:
            action.setAmount(Math.min(callSize, stacks[playerOnAction]));
            return true;
        case RAISE:
            int amount = action.getAmount();
            int stack = stacks[playerOnAction];
            if (amount <= stack && amount > callSize && (amount >= minimumRaise + callSize || amount == stack)) {
                return true;
            } else {
                debugMessage("Player " + playerOnAction + " can't raise with " + amount + "!");
                return false;
            }
        }

        return false;
    }

    private void resetPlayerStates() {
        for (int i = 0; i < playerCount; ++i) {
            if (playerStates[i] == PlayerState.ACTION_DONE) {
                playerStates[i] = PlayerState.WAITING_FOR_OWN_TURN;
            }
        }
    }

    /**
     * Is the current betting round closed?
     */
    private boolean isActionClosed() {
        for (PlayerState state : playerStates) {
            if (state == PlayerState.WAITING_FOR_OWN_TURN) return false;
        }
        return true;
    }

    private boolean isJustOnePlayerLeft() {
        int counter = 0;
        for (PlayerState state : playerStates) {
            if (state == PlayerState.WAITING_FOR_OWN_TURN || state == PlayerState.ACTION_DONE) {
                ++counter;
            }
        }
        return counter < 2;
    }

    private void endHand() {
        dealRemainingCards();
        mergePots();

        ArrayList<Integer> activePlayers = new ArrayList<Integer>();
        for (int i = 0; i < playerCount; ++i) {
            if (holeCards[i] != null) {
                activePlayers.add(i);
            }
        }

        /* Determine winner. */
        if (activePlayers.size() > 1) {
            HandWinner hw = new HandWinner(board, holeCards);

            for (MergedPot pot : mergedPots) {
                int sum = pot.getSize();
                ArrayList<Integer> winners = hw.getWinner(pot);
                int winnerCount = winners.size();

                for (Integer winner : winners) {
                    adjustPlayerChips(winner, sum / winnerCount);
                }
                if (winnerCount == 1) {
                    debugMessage("The player " + winners.toString() + " wins a pot with the size " + sum + ". (Involved players: " + pot.getPlayersInPotString() + ")");
                } else {
                    debugMessage("The players " + winners.toString() + " win a pot with the size " + sum + ". (Involved players: " + pot.getPlayersInPotString() + ")");
                }

                int remainder = sum % winnerCount;
                /* There are remaining chips in the pot. */
                if (remainder != 0) {
                    final int shift = (playerCount - dealer);
                    Collections.sort(winners, new Comparator<Integer>() {

                        @Override
                        public int compare(Integer winner1, Integer winner2) {
                            return ((winner1 + shift) % playerCount) - ((winner2 + shift) % playerCount);
                        }

                    });

                    int remainderPlayer = winners.get(0);
                    adjustPlayerChips(remainderPlayer, remainder);
                    debugMessage("There are remaining chips after splitting the pot! Player " + remainderPlayer + " received the remaining " + remainder + " chips!");
                }
            }
        } else {
            int id = activePlayers.get(0);

            for (MergedPot pot : mergedPots) {
                int sum = pot.getSize();
                debugMessage("The player [" + id + "] wins a pot with the size " + sum + ".");

                adjustPlayerChips(id, sum);
            }
        }

        /* Remove busted players. */
        for (int i = 0; i < playerCount; ++i) {
            if (stacks[i] == null) continue;
            if (stacks[i] <= 0) {
                playerStates[i] = PlayerState.BUSTED;
                stacks[i] = null;

                /* Adds the player id to the front of the ranking list. */
                ranking.add(0, i);
            }
        }

        updateGui();
    }

    @SuppressWarnings("incomplete-switch")
	private void dealRemainingCards() {
        if (street != Street.RIVER) {
            switch (street) {
            case PREFLOP:
                dealCard(0, 1, 2);
            case FLOP:
                dealCard(3);
            case TURN:
                dealCard(4);
            }
            street = Street.RIVER;
        }
    }

    @SuppressWarnings("incomplete-switch")
	private void nextStreet() {
        switch (street) {
        case PREFLOP:
            street = Street.FLOP;
            dealCard(0, 1, 2);
            debugMessage("The Flop was dealt: " + board[0] + " " + board[1] + " " + board[2]);
            break;
        case FLOP:
            street = Street.TURN;
            dealCard(3);
            debugMessage("The Turn was dealt: " + board[3]);
            break;
        case TURN:
            street = Street.RIVER;
            dealCard(4);
            debugMessage("The River was dealt: " + board[4]);
            break;
        }
    }

    private void dealCard(int... ids) {
        for (int id : ids) {
            if (board[id] == null) board[id] = deck.getCard();
        }
    }

    private void performRaise(int amount) {
        if (amount == stacks[playerOnAction]) {
            playerStates[playerOnAction] = PlayerState.ALL_IN;
        } else {
            playerStates[playerOnAction] = PlayerState.ACTION_DONE;
        }
        if (getChipsToCall() < amount) {
            for (int i = 0; i < playerCount; ++i) {
                if (playerStates[i] == PlayerState.ACTION_DONE && i != playerOnAction) {
                    playerStates[i] = PlayerState.WAITING_FOR_OWN_TURN;
                }
            }
        }

        raisePots(amount);

        adjustPlayerChips(playerOnAction, -amount);

        minimumRaise = currentBigBlind;

        playerOnAction = nextPlayer(playerOnAction);
    }

    private void performCall() {
        int toCall = getChipsToCall();

        /* The player checks. */
        if (toCall == 0) {
            playerStates[playerOnAction] = PlayerState.ACTION_DONE;
        }

        /* The player calls. */
        else if (stacks[playerOnAction] > toCall) {
            playerStates[playerOnAction] = PlayerState.ACTION_DONE;

            callPots();

            adjustPlayerChips(playerOnAction, -toCall);
        }

        /* The player calls and is All-In. */
        else if (stacks[playerOnAction] <= toCall) {
            playerStates[playerOnAction] = PlayerState.ALL_IN;

            callPotsAllIn(stacks[playerOnAction]);

            adjustPlayerChips(playerOnAction, -stacks[playerOnAction]);
        }

        playerOnAction = nextPlayer(playerOnAction);
    }

    private void performFold() {
        playerStates[playerOnAction] = PlayerState.HAS_FOLDED;
        holeCards[playerOnAction] = null;

        for (MergedPot mergedPot : mergedPots) {
            mergedPot.removePlayerFromPot(playerOnAction);
        }
        for (Pot pot : pots) {
            pot.removePlayerFromPot(playerOnAction);
        }

        playerOnAction = nextPlayer(playerOnAction);
    }

    private void raisePots(int amount) {
        /* The real raise amount (without calling size). */
        int essentialAmount = amount - getChipsToCall();

        /* Call pots. */
        callPots();

        /* Is the player All-In after the call? */
        boolean newPotClosed = (amount == stacks[playerOnAction]);

        /* If the last pot is closed, create a new pot. */
        Pot lastCalledPot = pots.get(pots.size() - 1);
        if (lastCalledPot.isClosed()) {
            int newSize[] = new int[playerCount];
            newSize[playerOnAction] = essentialAmount;

            boolean[] newIsPlayerInPot = lastCalledPot.getPlayersInPot().clone();
            for (int i = 0; i < playerCount; ++i) {
                if (stacks[i] != null && stacks[i] == 0 && playerStates[i] != PlayerState.HAS_FOLDED) {
                    newIsPlayerInPot[i] = false;
                }
            }

            pots.add(new Pot(newSize, newIsPlayerInPot, newPotClosed));
        } else {
            lastCalledPot.setSize(playerOnAction, lastCalledPot.getSize(playerOnAction) + essentialAmount);
            lastCalledPot.setClosed(newPotClosed);
        }
    }

    private void adjustPlayerChips(int playerPosition, int amount) {
        stacks[playerPosition] += amount;
    }

    private int getChipsToCall() {
        if (playerOnAction == -1) return 0;
        int toCallAllPots = 0;
        for (Pot pot : pots) {
            toCallAllPots += pot.toCall(playerOnAction);
        }
        return toCallAllPots;
    }

    private void callPots() {
        for (Pot pot : pots) {
            pot.setSize(playerOnAction, pot.max());
        }
    }

    private void callPotsAllIn(int callingSize) {
        int potCount = pots.size();
        for (int i = 0; i < potCount; ++i) {
            Pot pot = pots.get(i);
            int toCallThisPot = pot.toCall(playerOnAction);

            /* The player can check this pot. */
            if (toCallThisPot == 0) {
                continue;
            }

            /* The player can call this pot. */
            else if (callingSize >= toCallThisPot) {
                callingSize -= toCallThisPot;

                pot.setSize(playerOnAction, pot.max());
            }

            /* The player hasn't got enough chips to call this pot. */
            else {
                int thisPotNewCalled = pot.getSize(playerOnAction) + callingSize;
                callingSize = 0;

                if (thisPotNewCalled == 0) {
                    /* The player has no chips in this pot. */
                    pot.removePlayerFromPot(playerOnAction);
                } else {
                    /* The player has chips in this pot, but can't call it. */
                    pot.setSize(playerOnAction, thisPotNewCalled);

                    int[] newSize = new int[playerCount];
                    boolean[] newPlayersInPot = pot.getPlayersInPot().clone();
                    boolean newClosed = pot.isClosed();

                    for (int j = 0; j < playerCount; ++j) {
                        int sizeForNewPot = pot.getSize(j) - thisPotNewCalled;
                        if (sizeForNewPot > 0) {
                            pot.setSize(j, thisPotNewCalled);
                            newSize[j] = sizeForNewPot;
                        }
                    }

                    newPlayersInPot[playerOnAction] = false;

                    pot.setClosed(true);

                    pots.add(new Pot(newSize, newPlayersInPot, newClosed));
                }
            }
        }
    }

    private void mergePots() {
        if (pots.size() > 1 || pots.get(0).getSize() > 0) {
            debugMessage("Merge old and new pots!");

            for (Pot pot : pots) {
                int sum = pot.getSize();
                if (sum != 0) mergedPots.add(new MergedPot(sum, pot.getPlayersInPot()));
            }

            /* Merge pots with the same players. */
            for (int i = 0; i < mergedPots.size(); ++i) {
                for (int j = i + 1; j < mergedPots.size(); ++j) {
                    if (Arrays.equals(mergedPots.get(i).getPlayersInPot(), mergedPots.get(j).getPlayersInPot())) {
                        int newSize = mergedPots.get(i).getSize() + mergedPots.get(j).getSize();
                        mergedPots.remove(j);
                        mergedPots.get(i).setSize(newSize);

                        /* Decrease index to check again the "next" pot. */
                        --j;
                    }
                }
            }

            /* Create a new pot for the next betting round. */
            boolean[] isPlayerInPot = pots.get(pots.size() - 1).getPlayersInPot().clone();
            pots = new ArrayList<Pot>();
            pots.add(new Pot(new int[playerCount], isPlayerInPot));
        }
    }

    public int getNumberOfRemainingPlayers() {
        int number = 0;
        for (Integer stack : stacks) {
            if (stack != null) ++number;
        }
        return number;
    }

    /**
     * Should there be a verbose logging to the console?
     * 
     * @see Table#debugMessage(Object)
     */
    public static void setDebugPrint(boolean debugPrint) {
        Table.debugPrint = debugPrint;
    }

    /**
     * Prints the {@link Object} <code>message</code> to the console, iff the verbose logging is
     * enabled.
     * 
     * @param message
     *            The Object to print.
     * @see Table#setDebugPrint(boolean)
     */
    public static final void debugMessage(Object message) {
        if (debugPrint) System.out.println(message);
    }

}
