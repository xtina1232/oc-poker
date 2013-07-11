package poker.players;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;

/**
 * This is Dominik's approach to an intelligent player.
 * 
 * @author Dominik Schöler
 */
public class ChuckNorris extends AbstractPlayer {

	private enum Round {
		PREFLOP, FLOP, RIVER, TURN, UNKNOWN
	};
	
	private GameState currentGameState = null;
	private int currentPosition;
	private Card[] currentHand;
	private int currentCallSize;

	@Override
	public Action getAction(int position, GameState gs, Card[] hand, int callSize) {
		currentPosition = position;
		currentGameState = gs;
		currentHand = hand;
		currentCallSize = callSize;

		switch (getRound()) {
		case PREFLOP:
			return preflopAction();
		case FLOP:
			return flopAction();
		case RIVER:
			return riverAction();
		case TURN:
			return turnAction();
		default:
			return defaultAction();
		}
	}

	private Round getRound() {
		int cardCount = 0;
		Card[] board = currentGameState.getBoard();

		for (cardCount = 0; cardCount < board.length; cardCount++) {
			if (board[cardCount] == null)
				break;
		}

		switch (cardCount) {
		case 0:
			return Round.PREFLOP;
		case 3:
			return Round.FLOP;
		case 4:
			return Round.RIVER;
		case 5:
			return Round.TURN;
		default:
			return Round.UNKNOWN;
		}
	}

	private Action turnAction() {
		HandEvaluator evaluator = new HandEvaluator(currentPosition, currentHand, currentGameState.getBoard());
		HandStrength strength = evaluator.getResult().getStrength();

		switch (strength) {
		case HighCard:
			return tryAction(PlayerAction.FOLD);
		case OnePair:
			return tryAction(PlayerAction.CALL);
		case TwoPair:
			return tryAction(PlayerAction.CALL);
		default:
			return tryAction(PlayerAction.RAISE);
		}
	}

	private Action riverAction() {

		HandEvaluator evaluator = new HandEvaluator(currentPosition, currentHand, currentGameState.getBoard());
		HandStrength strength = evaluator.getResult().getStrength();

		switch (strength) {
		case HighCard:
			return tryAction(PlayerAction.FOLD);
		case OnePair:
			return tryAction(PlayerAction.CALL);
		case TwoPair:
			return tryAction(PlayerAction.CALL);
		default:
			return tryAction(PlayerAction.RAISE);
		}
	}
	private Action defaultAction() {
		return preflopAction();
	}

	private Action flopAction() {

		HandEvaluator evaluator = new HandEvaluator(currentPosition, currentHand, currentGameState.getBoard());
		HandStrength strength = evaluator.getResult().getStrength();

		switch (strength) {
		case HighCard:
			return tryAction(PlayerAction.FOLD);
		default:
			return tryAction(PlayerAction.CALL);
			// case OnePair:
			// return tryAction(PlayerAction.CALL);
			// case TwoPair:
			// return tryAction(PlayerAction.CALL);
			// case Trips:
			// return tryAction(PlayerAction.CALL);
			// case Straight:
			// return tryAction(PlayerAction.CALL);
			// case Flush:
			// return tryAction(PlayerAction.CALL);
			// case FullHouse:
			// return tryAction(PlayerAction.CALL);
			// case FourOfAKind:
			// return tryAction(PlayerAction.CALL);
			// case StraightFlush:
			// return tryAction(PlayerAction.CALL);
			// default:
			// return tryAction(PlayerAction.CALL);
		}
	}

	private Action preflopAction() {
		double quality = HandQuality.getHandQuality(currentHand[0], currentHand[1], currentGameState.getRemainingPlayers());

		if (quality > 50) {
			return tryAction(PlayerAction.RAISE);
		} else {
			return tryAction(PlayerAction.CALL);
		}
	}

	private Action tryAction(PlayerAction action) {
		switch (action) {
		case FOLD:
			return new Action(PlayerAction.FOLD);
		case CALL: {
			int ownCoins = currentGameState.getStack(currentPosition);
			
			if(ownCoins >= currentCallSize) {
				return new Action(PlayerAction.CALL);
			} else {
				return new Action(PlayerAction.FOLD);
			}
		}
		case RAISE: {
			int ownCoins = currentGameState.getStack(currentPosition);
			int minToRaise = currentGameState.getMinimumRaise() + currentCallSize;
			
			if(ownCoins >= minToRaise) {
				return new Action(PlayerAction.RAISE, minToRaise);
			} else if(ownCoins >= currentCallSize) {
				return new Action(PlayerAction.CALL);
			} else {
				return new Action(PlayerAction.FOLD);
			}
		}
		default:
			return new Action(PlayerAction.FOLD);

		}
	}

}