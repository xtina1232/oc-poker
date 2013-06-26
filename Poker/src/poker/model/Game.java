package poker.model;

import poker.gui.GameWindow;
import poker.model.logic.Table;

/**
 * Represents the main game loop. <br>
 * <br>
 * This class automatically plays multiple tournaments (tables) in one giant game loop and tracks
 * the winner distibution.
 * 
 * @author Becker
 */
public class Game {

    private final AbstractPlayer[] players;
    private final int gameCount;
    private final int startStack;
    private final int startBlind;
    private final int increaseBlind;
    private final boolean updateGui;
    private final int[] winCount;

    /**
     * Constructor using default values for Stack Size, Start Blind and Number of Hands until
     * increasinsg Blinds. <br>
     * <br>
     * Automatically starts the main game loop which means, that <code>gameCount</code> tournaments
     * will be played and the winner distribution will be tracked.
     * 
     * @param players
     *            An array with {@link AbstractPlayer} instances.
     * @param gameCount
     *            The games to be played.
     * @param updateGui
     *            Should the GUI be updated?
     */
    public Game(AbstractPlayer[] players, int gameCount, boolean updateGui) {
        this(players, gameCount, Table.DEFAULT_START_STACK, Table.DEFAULT_START_BLIND, Table.DEFAULT_INCREASE_BLIND, updateGui);
    }

    /**
     * Constructor with maximal flexibility. <br>
     * <br>
     * Automatically starts the main game loop which means,
     * that <code>gameCount</code> tournaments will be played and the winner distribution will be
     * tracked.
     * 
     * @param players
     *            An array with {@link AbstractPlayer} instances.
     * @param gameCount
     *            The games to be played.
     * @param startStack
     *            The startstack for each game.
     * @param startBlind
     *            The startblind for each game.
     * @param increaseBlind
     *            The amount of hands after which the blinds are increased.
     * @param updateGui
     *            Should the GUI be updated?
     */
    public Game(AbstractPlayer[] players, int gameCount, int startStack, int startBlind, int increaseBlind, boolean updateGui) {
        this.players = players;
        this.gameCount = gameCount;
        this.startStack = startStack;
        this.startBlind = startBlind;
        this.increaseBlind = increaseBlind;
        this.updateGui = updateGui;

        this.winCount = new int[players.length];

        this.start();
    }

    /**
     * Tracks the winner distribution.
     * 
     * @return an array of integers representing the amount of tables won by each participating
     *         player.
     */
    public int[] getWinCount() {
        return winCount;
    }

    private void start() {
        if (updateGui) GameWindow.getInstance().setEnabled(false);

        for (int i = 0; i < gameCount; ++i) {
            Table game = new Table(players, startStack, startBlind, increaseBlind, updateGui);
            game.run();

            ++winCount[game.getRanking()[0]];

            if (updateGui) GameWindow.getInstance().setWins(i, winCount);
        }

        if (updateGui) GameWindow.getInstance().setEnabled(true);
    }

}
