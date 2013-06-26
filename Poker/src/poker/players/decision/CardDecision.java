package poker.players.decision;

import poker.model.Card;

public class CardDecision {
	public static boolean haveCardHigherThan(Card[] cards, int rank) {
		boolean higher = false;
		for(Card c : cards) {
			if(c.getRank() > rank) {
				higher = true;
				break;
			}
		}
		return higher;
	}
}
