package poker.model;

import java.util.ArrayList;

/**
 * This class will be the argument for many methods of the {@link AbstractPlayer} class. It will
 * represent the situation of the table. The following information is included in the GameState:
 * <ul>
 * <li>The number of players remaining in the game</li>
 * <li>The current size of the pot</li>
 * <li>The position of the dealer</li>
 * <li>The chip count (stack) of each player</li>
 * <li>The chips in pot from each player</li>
 * <li>The states from each player</li>
 * <li>The cards on the board</li>
 * <li>The size of the Big Blind</li>
 * <li>The minimum amount of chips needed to raise</li>
 * <li>The actions chosen by each player in the last bet round</li>
 * </ul>
 * 
 * @author Becker
 */
public class GameState {

    private final int remainingPlayers;
    private final int potSize;
    private final int dealer;
    private final Integer[] stacks;
    private final int[] chipsInPot;
    private final PlayerState[] playerStates;
    private final Card[] board;
    private final int bigBlind;
    private final int minimumRaise;
    private ArrayList<Action> lastActions;

    public GameState(int remainingPlayers, int potSize, int dealer, Integer[] stacks, int[] chipsInPot, PlayerState[] playerStates, Card[] board, int bigBlind, int minimumRaise) {
        this.remainingPlayers = remainingPlayers;
        this.potSize = potSize;
        this.dealer = dealer;
        this.stacks = stacks;
        this.chipsInPot = chipsInPot;
        this.playerStates = playerStates;
        this.board = board;
        this.bigBlind = bigBlind;
        this.minimumRaise = minimumRaise;
    }

    /**
     * @return Returns the amount of remaining players on the table.
     */
    public int getRemainingPlayers() {
        return remainingPlayers;
    }

    /**
     * @return Returns the total amount of the merged pots from the last betting rounds.
     */
    public int getPotSize() {
        return potSize;
    }

    /**
     * @return Returns the position of the dealer.
     */
    public int getDealer() {
        return dealer;
    }

    /**
     * @param id
     *            The position of the player.
     * @return Returns <code>true</code> iff the player on the position <code>id</code> still has
     *         chips (isn't busted).
     */
    public boolean isPlayerIn(int id) {
        return stacks[id] != null;
    }

    /**
     * @param id
     *            The position of the player.
     * @return Returns the stack from the player on the position <code>id</code> or
     *         <code>null</code> if the player is busted.
     */
    public Integer getStack(int id) {
        return stacks[id];
    }

    /**
     * @param id
     *            The position of the player.
     * @return Returns the amount of chips that the player on the position <code>id</code> has set
     *         in the current betting round.
     */
    public int getChipsInPot(int id) {
        return chipsInPot[id];
    }

    /**
     * @param id
     *            The position of the player.
     * @return Returns the {@link PlayerState} of the player on the position <code>id</code>
     */
    public PlayerState getPlayerState(int id) {
        return playerStates[id];
    }

    /**
     * @return Returns an array with the length of 5 that represents the board. If an element is
     *         <code>null</code> this card isn't dealt yet.
     */
    public Card[] getBoard() {
        return board;
    }

    /**
     * @return Returns the size of the big blind.
     */
    public int getBigBlind() {
        return bigBlind;
    }

    /**
     * @return Returns the size of the minimum raise.
     */
    public int getMinimumRaise() {
        return minimumRaise;
    }

    /**
     * Sets the last actions. Is called automatically from the engine before passing it as argument
     * to the methods.
     * 
     * @param lastActions
     *            Last actions for the player.
     * @see #getLastActions()
     */
    public void setLastActions(ArrayList<Action> lastActions) {
        this.lastActions = lastActions;
    }

    /**
     * @return Returns the actions from the other players since the last call to your player's
     *         {@link AbstractPlayer#getAction(int, GameState, Card[], int)} method or since the
     *         start of the current hand.
     */
    public ArrayList<Action> getLastActions() {
        return new ArrayList<Action>(lastActions);
    }

}
