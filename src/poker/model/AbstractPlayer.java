package poker.model;

import poker.model.logic.Table;

/**
 * This abstract class is the basic class for any player that will be available in the game.
 * <ul>
 * <li>When {@link Table#run() starting} a table the {@link #tableStarted(int)} method will be
 * called by the engine with the position of the player as argument.</li>
 * <li>While {@link Table#playHand() playing} a hand the
 * {@link #handStarted(int, GameState, Card[])} method will be called by the engine to inform the
 * player about the new {@link GameState} and his new hole cards.</li>
 * <li>When it is the players turn the engine will call the
 * {@link #getAction(int, GameState, Card[], int)} until it received a valid action. As additional
 * argument it contains the call size needed for calling.</li>
 * <li>If the hand is finished, the {@link #handFinished(int, GameState)} will be called by the
 * engine to inform the player about the new {@link GameState} after determining the winner of the
 * old hand.</li>
 * <li>If there is only one player left on the table, the engine will call the
 * {@link #tableFinished(int, Integer[])} method to inform all players on the table about the
 * ranking.
 * </ul>
 * 
 * @author Becker
 */
public abstract class AbstractPlayer {

    /**
     * Default constructor without arguments. <br>
     * <br>
     * The Poker Engine only uses your player's default constructor to create an instance, so make
     * sure all initializing work is done here.
     */
    public AbstractPlayer() {
    }

    /**
     * Returns the {@link Action} from the player for the current {@link GameState} and your
     * {@link Card}s.
     * 
     * @param position
     *            Your position.
     * @param gs
     *            The current GameState.
     * @param hand
     *            Your {@link Card}s.
     * @param callSize
     *            The chips needed to call.
     * @return The {@link Action} from the player.
     * @see Action
     * @see GameState
     * @see Card
     */
    public abstract Action getAction(int position, GameState gs, Card[] hand, int callSize);

    /**
     * This method is called when a new game has started.
     * 
     * @param position
     *            Your position.
     */
    public void tableStarted(int position) {
    }

    /**
     * This method is called when the game is finished.
     * 
     * @param position
     *            Your position.
     * @param ranking
     *            The ranking from the last tournament. The first component in the Array contains
     *            the ID from the winner.
     */
    public void tableFinished(int position, Integer[] ranking) {
    }

    /**
     * This method is called when a new hand is started.
     * 
     * @param position
     *            Your position.
     * @param gs
     *            The GameState after the hand.
     * @param cards
     *            Your new holecards.
     */
    public void handStarted(int position, GameState gs, Card[] cards) {
    }

    /**
     * This method is called when a hand is finished.
     * 
     * @param position
     *            Your position.
     * @param gs
     *            The GameState after the hand.
     */
    public void handFinished(int position, GameState gs) {
    }

}
