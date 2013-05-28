package poker.model.logic;

import java.util.ArrayList;
import java.util.Collections;

import poker.model.Card;
import poker.model.MyRandom;

/**
 * This class represents the Deck. You can get new random Cards with the {@link #getCard()} method
 * and you can mix the Deck with the {@link #reset()} method.
 * 
 * @author Becker
 */
class Deck {

    private final ArrayList<Card> cards;

    private int index;

    /**
     * Creates a new Deck.
     */
    public Deck() {
        cards = new ArrayList<Card>();
        for (int i = 0; i < 52; ++i) {
            cards.add(new Card(i / 13, i % 13));
        }

        index = 0;
    }

    /**
     * Shuffles the Deck. All cards are now <i>unused</i> again.
     * 
     * @see #getCard()
     */
    protected void reset() {
        Collections.shuffle(cards, MyRandom.getRandom());

        index = 0;
    }

    /**
     * Returns a randomly chosen unused Card, since the last {@link #reset()} call.
     * 
     * @see #reset()
     */
    public Card getCard() {
        return cards.get(index++);
    }

}
