package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;

/**
 * This player folds in every situation.
 * 
 * @author Becker
 */
public class FoldPlayer extends AbstractPlayer {

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {
        return new Action(PlayerAction.FOLD);
    }

}
