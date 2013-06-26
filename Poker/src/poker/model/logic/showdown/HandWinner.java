package poker.model.logic.showdown;

import java.util.ArrayList;

import poker.model.Card;
import poker.model.logic.MergedPot;

/**
 * With this class it is possible to determine the winner(s) for different pots.
 * 
 * @author Becker
 * @see #getWinner(MergedPot)
 */
public class HandWinner {

    private final WinningCards[] handStrengths;

    /**
     * Determines the strengthes of all hands from the ShowDown.
     * 
     * @param board
     *            The community cards.
     * @param holeCards
     *            The HoleCards of the players from the ShowDown.
     * @see #getWinner(MergedPot)
     */
    public HandWinner(Card[] board, Card[][] holeCards) {
        this.handStrengths = new WinningCards[holeCards.length];
        for (int i = 0; i < handStrengths.length; i++) {
            if (holeCards[i] != null) handStrengths[i] = new HandEvaluator(i, holeCards[i], board).getResult();
        }
    }

    /**
     * Returns the positions from the winners for the pot.
     * 
     * @param pot
     *            The pot.
     * @return The positions from the winners.
     */
    public ArrayList<Integer> getWinner(MergedPot pot) {
        ArrayList<Integer> winners = new ArrayList<Integer>();
        WinningCards max = null;
        for (int i = 0; i < handStrengths.length; i++) {
            if (!pot.isPlayerInPot(i)) continue;
            if (handStrengths[i] == null) continue;
            int diff = handStrengths[i].compareTo(max);
            if (diff > 0) {
                winners.clear();
                winners.add(handStrengths[i].getPlayer());
                max = handStrengths[i];
            } else if (diff == 0) {
                winners.add(handStrengths[i].getPlayer());
            }
        }
        return winners;
    }

}