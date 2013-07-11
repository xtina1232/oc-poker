package poker.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import poker.model.Card;
import poker.model.logic.Table;

/**
 * This class can be used for evaluating the hand from player.
 * 
 * @author Becker
 */
public class HandEvaluator {

	/**
	 * The position of the player. Used for creating the {@link WinningCards}
	 * object.
	 */
	private final int player;

	/**
	 * Contains the cards for determining the strength.
	 */
	private final ArrayList<Card> hand;

	/**
	 * Contains all strength of this hand sorted by the normal poker order.
	 */
	private final TreeMap<HandStrength, int[]> strength;

	private final ArrayList<Card> cardsSuitSortedByRank;
	private final ArrayList<Card> handSortedByRankStraight;

	/**
	 * Starts the evaluation for the hand passed as arguments.
	 * 
	 * @param player
	 *            The ID from the player.
	 * @param holeCards
	 *            The hole cards.
	 * @param board
	 *            The board.
	 */
	public HandEvaluator(int player, Card[] holeCards, Card[] board) {
		this.player = player;
		this.strength = new TreeMap<HandStrength, int[]>();

		ArrayList<Card> hand = new ArrayList<Card>();
		hand.add(holeCards[0]);
		hand.add(holeCards[1]);
		// for (int i = 0; i < 5; ++i) {
		// hand.add(board[i]);
		// }
		for (int i = 0; i < board.length; i++) {
			if (board[i] == null)
				break;
			hand.add(board[i]);
		}
		this.hand = sortByRank(hand);

		ArrayList<Card> cardsSuitClub = new ArrayList<Card>();
		ArrayList<Card> cardsSuitSpade = new ArrayList<Card>();
		ArrayList<Card> cardsSuitDiamond = new ArrayList<Card>();
		ArrayList<Card> cardsSuitHeart = new ArrayList<Card>();
		for (Card card : this.hand) {
			if (card.getSuit() == 0) {
				cardsSuitClub.add(card);
			} else if (card.getSuit() == 1) {
				cardsSuitSpade.add(card);
			} else if (card.getSuit() == 2) {
				cardsSuitDiamond.add(card);
			} else if (card.getSuit() == 3) {
				cardsSuitHeart.add(card);
			}
		}
		if (cardsSuitClub.size() >= 5) {
			cardsSuitSortedByRank = cardsSuitClub;
		} else if (cardsSuitSpade.size() >= 5) {
			cardsSuitSortedByRank = cardsSuitSpade;
		} else if (cardsSuitDiamond.size() >= 5) {
			cardsSuitSortedByRank = cardsSuitDiamond;
		} else if (cardsSuitHeart.size() >= 5) {
			cardsSuitSortedByRank = cardsSuitHeart;
		} else {
			cardsSuitSortedByRank = null;
		}

		handSortedByRankStraight = new ArrayList<Card>(this.hand);
		removePairs(handSortedByRankStraight);

		/*
		 * The checks for Straight, Flush and Straight Flush don't need the
		 * other checks. So perform those checks first!
		 */
		checkForStraight(strength, handSortedByRankStraight);
		checkForFlush();
		checkForStraightFlush();
		if (!strength.isEmpty())
			return;

		/*
		 * The checks for Pairs, Trips, FullHouse and FourOfAKind are next in
		 * line. If any of them succeeds, the check for HighCard isn't needed
		 * anymore!
		 */
		checkForPairings();
		if (!strength.isEmpty())
			return;

		checkForHighCard();
	}

	/**
	 * Print and return the highest strength of this hand.
	 */
	public WinningCards getResult() {
		Entry<HandStrength, int[]> lastEntry = strength.lastEntry();
		WinningCards result = new WinningCards(player, lastEntry.getKey(), lastEntry.getValue());
		Table.debugMessage("The player " + player + " " + hand + " holds " + result);
		return result;
	}

	/**
	 * Create the kicker for the {@link HandStrength#HighCard}.
	 */
	private void checkForHighCard() {
		int[] highCard = new int[5];
		for (int i = 0; i < 5; ++i) {
			highCard[i] = this.hand.get(i).getRank();
		}
		strength.put(HandStrength.HighCard, highCard);
	}

	/**
	 * Check for {@link HandStrength#OnePair}. If it succeeds, continue with the
	 * checks for {@link HandStrength#TwoPair} and {@link HandStrength#Trips}.
	 */
	private void checkForPairings() {
		/*
		 * This HashMap contains one entry for each rank of the hand and the
		 * times it occurs.
		 */
		TreeMap<Integer, Integer> pairings = new TreeMap<Integer, Integer>(new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}

		});

		for (Card card : hand) {
			if (pairings.containsKey(card.getRank())) {
				pairings.put(card.getRank(), pairings.get(card.getRank()) + 1);
			} else {
				pairings.put(card.getRank(), 1);
			}
		}

		while (pairings.values().remove(1)) {
			/* Remove single cards. Now only pairings are remaining. */
		}

		if (pairings.isEmpty())
			return;

		ArrayList<Card> cardsForPairings = new ArrayList<Card>(hand);
		if (pairings.containsValue(4)) {
			int[] result = new int[2];
			result[0] = getHighestRankForCardcount(4, pairings);
			removeRankFromList(result[0], cardsForPairings);
			result[1] = cardsForPairings.remove(0).getRank();
			strength.put(HandStrength.FourOfAKind, result);
		} else if (pairings.containsValue(3)) {
			if (pairings.containsValue(2)) {
				int[] result = new int[2];
				result[0] = getHighestRankForCardcount(3, pairings);
				result[1] = getHighestRankForCardcount(2, pairings);
				strength.put(HandStrength.FullHouse, result);
			} else {
				int[] result = new int[3];
				result[0] = getHighestRankForCardcount(3, pairings);
				removeRankFromList(result[0], cardsForPairings);
				result[1] = cardsForPairings.remove(0).getRank();
				result[2] = cardsForPairings.remove(0).getRank();
				strength.put(HandStrength.Trips, result);
			}
		} else if (pairings.containsValue(2)) {
			if (pairings.values().size() >= 2) {
				int[] result = new int[3];
				result[0] = getHighestRankForCardcount(2, pairings);
				result[1] = getHighestRankForCardcount(2, pairings);
				removeRankFromList(result[0], cardsForPairings);
				removeRankFromList(result[1], cardsForPairings);
				result[2] = cardsForPairings.remove(0).getRank();
				strength.put(HandStrength.TwoPair, result);
			} else {
				int[] result = new int[4];
				result[0] = getHighestRankForCardcount(2, pairings);
				removeRankFromList(result[0], cardsForPairings);
				result[1] = cardsForPairings.remove(0).getRank();
				result[2] = cardsForPairings.remove(0).getRank();
				result[3] = cardsForPairings.remove(0).getRank();
				strength.put(HandStrength.OnePair, result);
			}
		}
	}

	private static int getHighestRankForCardcount(int cardcount, TreeMap<Integer, Integer> pairings) {
		for (Entry<Integer, Integer> entry : pairings.entrySet()) {
			if (entry.getValue() == cardcount) {
				pairings.remove(entry.getKey());
				return entry.getKey();
			}
		}
		return -1;
	}

	private static void removeRankFromList(int rank, ArrayList<Card> hand) {
		for (int i = hand.size() - 1; i >= 0; --i) {
			if (hand.get(i).getRank() == rank)
				hand.remove(i);
		}
	}

	/**
	 * Check for {@link HandStrength#Straight}.
	 * 
	 * @param hand
	 *            The list of cards with <b>no</b> pairs containing.
	 * @see #removePairs(ArrayList)
	 */
	private boolean checkForStraight(TreeMap<HandStrength, int[]> handStrength, ArrayList<Card> hand) {
		int highestCard = hand.get(0).getRank();

		if (hand.size() >= 5) {
			for (int i = 1; i < 5; ++i) {
				if (highestCard - i != hand.get(i).getRank()) {
					hand.remove(0);
					return checkForStraight(handStrength, hand);
				}
			}

			int[] straight = new int[1];
			straight[0] = highestCard;
			handStrength.put(HandStrength.Straight, straight);
			return true;
		} else {
			hand = new ArrayList<Card>(this.hand);

			removePairs(hand);

			if (hand.size() >= 5 && hand.get(0).getRank() == 12) {
				while (hand.size() > 4) {
					hand.remove(0);
				}
				for (int i = 0; i < 4; ++i) {
					if (3 - i != hand.get(i).getRank())
						break;

					if (i == 3) {
						int[] straight = new int[1];
						straight[0] = 3;
						handStrength.put(HandStrength.Straight, straight);
						return true;
					}
				}
			}
			return false;
		}
	}

	/**
	 * Check for {@link HandStrength#Flush}.
	 */
	private void checkForFlush() {
		if (cardsSuitSortedByRank != null) {
			int[] flush = new int[5];
			for (int i = 0; i < 5; ++i) {
				flush[i] = cardsSuitSortedByRank.get(i).getRank();
			}
			strength.put(HandStrength.Flush, flush);
		}
	}

	/**
	 * Check for {@link HandStrength#StraightFlush}. Will only be performed, if
	 * the checks for {@link HandStrength#Flush} and
	 * {@link HandStrength#Straight} were successful.
	 * 
	 * @see #checkForStraight(TreeMap, ArrayList)
	 * @see #checkForFlush()
	 */
	private void checkForStraightFlush() {
		if (strength.containsKey(HandStrength.Flush) && strength.containsKey(HandStrength.Straight)) {
			TreeMap<HandStrength, int[]> straightStrength = new TreeMap<HandStrength, int[]>();
			if (checkForStraight(straightStrength, cardsSuitSortedByRank)) {
				int[] straightFlush = new int[1];
				straightFlush[0] = straightStrength.get(HandStrength.Straight)[0];
				strength.put(HandStrength.StraightFlush, straightFlush);
			}
		}
	}

	/**
	 * Returns a new version of <code>hand</code>, which is sorted by rank.
	 */
	private static ArrayList<Card> sortByRank(ArrayList<Card> hand) {
		ArrayList<Card> clonedHand = new ArrayList<Card>(hand);
		Collections.sort(clonedHand);
		return clonedHand;
	}

	/**
	 * Removes all pairings from the list.
	 * 
	 * @see #checkForStraight(TreeMap, ArrayList)
	 */
	private static void removePairs(ArrayList<Card> hand) {
		for (int i = 0; i < hand.size() - 1; ++i) {
			if (hand.get(i).getRank() == hand.get(i + 1).getRank()) {
				hand.remove(i + 1);
				--i;
			}
		}
	}

}