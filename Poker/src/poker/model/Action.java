package poker.model;

/**
 * This class represents an action from a player.
 * 
 * @author Becker
 */
public class Action {

    /**
     * This enumeration contains all possible types of an {@link Action}:
     * <ul>
     * <li>{@link #FOLD}</li>
     * <li>{@link #CALL}</li>
     * <li>{@link #RAISE}</li>
     * </ul>
     * 
     * @author Becker
     */
    public static enum PlayerAction {
        /**
         * The player has folded.
         */
        FOLD,

        /**
         * The player has called or checked, if the call size is <code>0</code>.
         */
        CALL,

        /**
         * The player has raised.
         */
        RAISE
    }

    /**
     * The position of the player.
     */
    private int position;

    /**
     * The amount of chips needed for calling.
     */
    private int callSize;

    /**
     * The action of the player.
     */
    private final PlayerAction action;

    /**
     * The amount of chips from the action. (Not needed for e.g. folding or checking)
     */
    private int amount;

    /**
     * Creates a new action for the player <b>without</b> a specific amount of chips.
     * 
     * @param action
     *            The action of the player.
     * @see PlayerAction#FOLD
     * @see PlayerAction#CALL
     */
    public Action(PlayerAction action) {
        this.action = action;
    }

    /**
     * Creates a new action for the player <b>with</b> a specific amount of chips.
     * 
     * @param action
     *            The action of the player.
     * @param amount
     *            The amount of chips.
     * @see PlayerAction#RAISE
     */
    public Action(PlayerAction action, int amount) {
        this(action);

        setAmount(amount);
    }

    /**
     * Sets the position of the player.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the position of the player.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the amount of chips needed for calling. This method is called automatically after
     * receiving the Action from the engine!
     */
    public void setCallSize(int callSize) {
        this.callSize = callSize;
    }

    /**
     * Returns the amount of chips needed for calling.
     */
    public int getCallSize() {
        return callSize;
    }

    /**
     * Returns the action of the player.
     */
    public PlayerAction getAction() {
        return action;
    }

    /**
     * Sets the amount of chips.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Returns the amount of chips.
     */
    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Position " + position + ": " + action + " " + amount;
    }

}
