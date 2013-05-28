package poker.model;

/**
 * This class contains all possible states of a player.
 * 
 * @author Becker
 */
public enum PlayerState {

    /**
     * The player is out of the game.
     */
    BUSTED,

    /**
     * The player is waiting for his turn.
     */
    WAITING_FOR_OWN_TURN,

    /**
     * The player has done his action. It changes back to {@link #WAITING_FOR_OWN_TURN}, when
     * another player performes a raise.
     */
    ACTION_DONE,

    /**
     * The player has folded his hand.
     */
    HAS_FOLDED,

    /**
     * The player is All-In.
     */
    ALL_IN

}
