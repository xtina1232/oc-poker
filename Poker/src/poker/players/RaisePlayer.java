package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;
import poker.model.MyRandom;

/**
 * This player raises in every situation.
 * 
 * @author Becker
 */
public class RaisePlayer extends AbstractPlayer {

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {
        return RaisePlayer.getRandomRaiseAction(gs, gs.getStack(position), callSize);
    }

    /**
     * Use this method to get an {@link Action}, which performs a raise with a random amount of
     * chips.
     * 
     * @param gs
     *            The current game situation.
     * @param ownStack
     *            The stack from the palyer.
     * @param callSize
     *            The amount of chips needed for calling.
     * @return A raise action with a random amount of chips.
     */
    public static final Action getRandomRaiseAction(GameState gs, int ownStack, int callSize) {
        if (ownStack <= callSize) return new Action(PlayerAction.CALL);

        int minChipsToRaise = callSize + gs.getMinimumRaise();
        int remainingChips = ownStack - minChipsToRaise;
        if (remainingChips <= 0) return new Action(PlayerAction.RAISE, ownStack);

        return new Action(PlayerAction.RAISE, minChipsToRaise + MyRandom.getRandom().nextInt(remainingChips));
    }

}
