package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;
import poker.model.logic.showdown.HandQuality;

/**
 * This is Dominik's approach to an intelligent player.
 * 
 * @author Dominik Schšler
 */
public class Dominik extends AbstractPlayer {

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {    	
    	Action action = null;
    	double quality = HandQuality.getHandQuality(hand[0], hand[1], gs.getRemainingPlayers());
    	int ownCoins = gs.getStack(position);
    	int minToRaise = gs.getMinimumRaise() + callSize;

    	if(quality > 50 && ownCoins >= minToRaise) {
    		action = new Action(PlayerAction.RAISE, minToRaise);
    	} else {
    		action = new Action(PlayerAction.CALL);
    	}
    	
        return action;
    }
}