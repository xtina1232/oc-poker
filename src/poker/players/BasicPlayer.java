package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;
import poker.model.logic.showdown.HandEvaluator;
import poker.model.logic.showdown.HandQuality;
import poker.model.logic.showdown.HandStrength;

/**
 * This is a basic intelligent player.
 * 
 * @author Becker
 */
public class BasicPlayer extends AbstractPlayer {

	@Override
	public Action getAction(int position, GameState gs, Card[] hand,
			int callSize) {
		// TODO: Hier die Aufgabe implementieren
		Action action = new Action(PlayerAction.CALL);

		boolean tryCall = true;
		boolean tryRaise = false;

		if (gs.getBoard().length == 5 && gs.getBoard()[4] != null) {
			HandEvaluator eval = new HandEvaluator(position, hand,
					gs.getBoard());
			HandStrength strength = eval.getResult().getStrength();

			double quality = HandQuality.getHandQuality(hand[0], hand[1],
					gs.getRemainingPlayers());
			int ownCoins = gs.getStack(position);
			int minToRaise = gs.getMinimumRaise() + callSize;

			boolean canRaise = quality > 40 && ownCoins >= minToRaise;
			boolean canCall = quality > 40;

			switch (strength) {
			case Flush:
			case FourOfAKind:
			case FullHouse:
			case Straight:
			case StraightFlush:
			case Trips:
				tryRaise = true;
				// case TwoPair:
				// break;
				// case OnePair:
				// break;
				// case HighCard:
				// break;
			default:
				tryCall = true;
			}

			if (tryRaise && canRaise) {
				action = new Action(PlayerAction.RAISE, minToRaise);
			} else if (tryCall && canCall) {
				action = new Action(PlayerAction.CALL);
			} else {
				action = new Action(PlayerAction.FOLD);
			}
		}
		return action;

	}
}
