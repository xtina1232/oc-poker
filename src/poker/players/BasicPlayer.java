package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;
import poker.model.logic.showdown.HandQuality;

/**
 * This is a basic intelligent player.
 * 
 * @author Becker
 */
public class BasicPlayer extends AbstractPlayer {

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {    	
    	Action action = null;
    	double quality = HandQuality.getHandQuality(hand[0], hand[1], gs.getRemainingPlayers());
    	int ownCoins = gs.getStack(position);
    	int minToRaise = gs.getMinimumRaise() + callSize;

    	if(quality > 40 && ownCoins >= minToRaise) {
    		action = new Action(PlayerAction.RAISE, minToRaise);
    	} else if(quality > 40) {
    		action = new Action(PlayerAction.CALL);
    	} else {
    		action = new Action(PlayerAction.FOLD);
    	}
    	
        return action;
    }
}
