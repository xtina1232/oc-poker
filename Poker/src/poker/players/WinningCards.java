package poker.players;

import java.util.Arrays;

/**
 * @author Becker
 */
public class WinningCards implements Comparable<WinningCards> {

    private final int player;

    private final HandStrength strength;

    private final int[] handStrength;

    protected WinningCards(int player, HandStrength strength, int[] handStrength) {
        this.player = player;
        this.strength = strength;
        this.handStrength = handStrength;
    }

    public int getPlayer() {
        return player;
    }

    public HandStrength getStrength() {
        return strength;
    }

    public int[] getHandStrength() {
        return handStrength;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(strength);
        result.append(' ');
        result.append(Arrays.toString(handStrength));
        return result.toString();
    }

    @Override
    public int compareTo(WinningCards o) {
        if (o == null) return 1;

        /* The strength is different. */
        if (strength != o.getStrength()) return strength.compareTo(o.getStrength());

        /* The strength is the same, but there could be different kickers. */
        int[] oHandStrength = o.getHandStrength();
        for (int i = 0; i < oHandStrength.length; i++) {
            if (handStrength[i] != oHandStrength[i]) return handStrength[i] - oHandStrength[i];
        }

        /* The strength and the kickers are the same. */
        return 0;
    }

}