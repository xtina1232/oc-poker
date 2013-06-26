package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;
import poker.model.MyRandom;

/**
 * This player performs random actions in every situation.
 * 
 * @author Becker
 */
public class RandomPlayer extends AbstractPlayer {

    private final MyRandom rnd;

    public RandomPlayer() {
        rnd = MyRandom.getRandom();
    }

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {
        PlayerAction[] actionIds = PlayerAction.values();
        PlayerAction actionId = actionIds[rnd.nextInt(actionIds.length)];

        switch (actionId) {
        case FOLD:
            return new Action(PlayerAction.FOLD);
        case CALL:
            return new Action(PlayerAction.CALL);
        case RAISE:
            return RaisePlayer.getRandomRaiseAction(gs, gs.getStack(position), callSize);
        default:
            throw new IllegalStateException("Should never happen!");
        }
    }

}
