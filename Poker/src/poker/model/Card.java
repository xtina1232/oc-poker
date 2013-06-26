package poker.model;

/**
 * This class represents a Card.
 * 
 * @author Becker
 */
public class Card implements Comparable<Card> {

    /**
     * Returns a String representation for two (Hole) Cards.<br>
     * <br>
     * The first letter is the character for the higher rank and the second letter is the character
     * for the lower rank of the cards. If the cards have different ranks (means: they aren't a
     * pair), a third character follows to indicate, if they are <b>s</b>uited or <b>o</b>ffsuited.<br>
     * <br>
     * Examples:<br>
     * <ul>
     * <b>AA</b> - means a pair of Aces<br>
     * <b>T9s</b> - means a 10 and a 9 that are of the same suit<br>
     * <b>KQo</b> - means a King and a Queen that are of different suits<br>
     * </ul>
     * 
     * @param card1
     *            The first hole card.
     * @param card2
     *            The second hole card.
     */
    public static String getCardsString(Card card1, Card card2) {
        StringBuilder sb = new StringBuilder();

        if (card1.getRank() > card2.getRank()) {
            sb.append(card1.getRankString()).append(card2.getRankString());
        } else {
            sb.append(card2.getRankString()).append(card1.getRankString());
        }

        if (card1.getRank() != card2.getRank()) {
            if (card1.getSuit() == card2.getSuit()) {
                sb.append('s');
            } else {
                sb.append('o');
            }
        }

        return sb.toString();
    }

    /**
     * The suit of the Card.
     */
    private final int suit;

    /**
     * The rank of the Card.
     */
    private final int rank;

    /**
     * Creates a new Card with the desired suit and rank.
     * 
     * @param suit
     *            The suit of the Card.
     * @param rank
     *            The rank of the Card.
     */
    public Card(int suit, int rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the numerical representation of the Card's suit.
     */
    public int getSuit() {
        return suit;
    }

    /**
     * Returns the numerical representation of the Card's rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Returns the String representation of the suit of the Card. <br>
     * <br>
     * For an Ace of Spades it would be <code>s</code>.
     */
    public String getSuitString() {
        switch (suit) {
        case 0:
            /* Clubs */
            return "c";
        case 1:
            /* Spades */
            return "s";
        case 2:
            /* Diamonds */
            return "d";
        case 3:
            /* Hearts */
            return "h";
        default:
            throw new IllegalStateException("Wrong suit for card " + this);
        }
    }

    /**
     * Returns the String representation of the rank of the Card. <br>
     * <br>
     * For an Ace of Spades it would be <code>A</code>.
     */
    public String getRankString() {
        /* 2 .. 9 */
        if (0 <= rank && rank < 8) return String.valueOf(rank + 2);
        switch (rank) {
        case 8:
            /* Ten */
            return "T";
        case 9:
            /* Joker */
            return "J";
        case 10:
            /* Queen */
            return "Q";
        case 11:
            /* King */
            return "K";
        case 12:
            /* Ace */
            return "A";
        default:
            throw new IllegalStateException("Wrong rank for card " + this);
        }
    }

    /**
     * Returns the String representation of the Card. <br>
     * <br>
     * For an Ace of Spades it would be <code>As</code>.
     * 
     * @see #getRankString()
     * @see #getSuitString()
     */
    @Override
    public String toString() {
        return getRankString() + getSuitString();
    }

    @Override
    public int hashCode() {
        return 13 * this.getRank() + this.getSuit();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        if (getRank() != other.getRank()) return false;
        if (getSuit() != other.getSuit()) return false;
        return true;
    }

    /**
     * Compares this Card with another Card against their rank.
     */
    @Override
    public int compareTo(Card card) {
        return card.getRank() - this.getRank();
    }

}
