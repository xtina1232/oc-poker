package poker.model.logic.showdown;

/**
 * Contains all possible strengthes of a hand.
 * 
 * @author Becker
 */
public enum HandStrength {

    /**
     * The hand has no strength, only the high card.
     */
    HighCard,

    /**
     * The hand contains one pair.
     */
    OnePair,

    /**
     * The hand contains two pairs.
     */
    TwoPair,

    /**
     * The hand contains three cards of a kind.
     */
    Trips,

    /**
     * The hand contains a straight.
     */
    Straight,

    /**
     * The hand contains a flush.
     */
    Flush,

    /**
     * The hand contains a full house.
     */
    FullHouse,

    /**
     * The hand contains four cards of a kind.
     */
    FourOfAKind,

    /**
     * The hand contains a straight flush.
     */
    StraightFlush

}
