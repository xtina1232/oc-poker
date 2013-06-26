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
 * This is the Player of Peter Ittner
 * 
 * @author Ittner
 */
public class Peter extends AbstractPlayer {

	@Override
	public void tableFinished(int position, Integer[] ranking) {
		super.tableFinished(position, ranking);
		// TODO for learning: protocol rank
	}

	@Override
	public Action getAction(int position, GameState gs, Card[] hand,
			int callSize) {
		
		// Initial Action
		Action action = new Action(PlayerAction.CALL);
		
		Card[] board = gs.getBoard();
		
		Card[] filledBoard = fillBoardWithTrashToEvaluate(board);
		
		boolean tryCall = true;
		boolean tryRaise = false;

		HandEvaluator eval = new HandEvaluator(position, hand, filledBoard);
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
		return action;

	}

	private Card[] fillBoardWithTrashToEvaluate(Card[] board) {
		// muss auf jeden Fall 5 Karten enthalten
		Card[] filledBoard = new Card[5];
		for(int i=0; i<board.length && i<filledBoard.length; i++) {
			if(board[i] == null) {
				filledBoard[i] = new Card(0,0);
			} else {
				filledBoard[i] = board[i];
			}
		}
		return filledBoard;
	}
}
