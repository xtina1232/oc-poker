package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;
import poker.model.MyRandom;

/**
 * This player calls or raises in every situation,
 * 
 * @author Becker
 */
public class CallOrRaisePlayer extends AbstractPlayer {

    private final MyRandom rnd;

    public CallOrRaisePlayer() {
        rnd = MyRandom.getRandom();
    }

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {
        if (rnd.nextBoolean()) {
            return new Action(PlayerAction.CALL);
        } else {
            return RaisePlayer.getRandomRaiseAction(gs, gs.getStack(position), callSize);
        }
    }

}
