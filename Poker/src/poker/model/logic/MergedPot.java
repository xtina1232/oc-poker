package poker.model.logic;

import java.util.Arrays;

/**
 * @author Becker
 */
public class MergedPot {

    private int size;

    private final boolean[] playersInPot;

    protected MergedPot(int size, boolean[] playersInPot) {
        this.setSize(size);
        this.playersInPot = playersInPot;
    }

    public void setSize(int size) {
        if (size <= 0) throw new IllegalStateException("Ein Pot kann keine negative Chipanzahl enthalten!");
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean[] getPlayersInPot() {
        return playersInPot;
    }

    protected void removePlayerFromPot(int playerPosition) {
        playersInPot[playerPosition] = false;
    }

    public boolean isPlayerInPot(int playerPosition) {
        return playersInPot[playerPosition];
    }

    public String getPlayersInPotString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < playersInPot.length; ++i) {
            if (isPlayerInPot(i)) {
                if (sb.length() > 0) sb.append(' ');
                sb.append(i);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MergedPot[size=" + size + "; playersInPot=" + Arrays.toString(playersInPot) + "]";
    }

}
