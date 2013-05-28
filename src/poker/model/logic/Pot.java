package poker.model.logic;

/**
 * @author Becker
 */
class Pot {

    private final int[] size;

    private final boolean[] playersInPot;

    private boolean closed;

    protected Pot(int[] size, boolean[] playersInPot) {
        this(size, playersInPot, false);
    }

    protected Pot(int[] size, boolean[] playersInPot, boolean closed) {
        this.size = size;
        this.playersInPot = playersInPot;
        this.closed = closed;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getSize() {
        int sum = 0;
        for (int i : size) {
            sum += i;
        }
        return sum;
    }

    protected void setSize(int playerPosition, int amount) {
        size[playerPosition] = amount;
    }

    protected int getSize(int playerPosition) {
        return size[playerPosition];
    }

    protected boolean[] getPlayersInPot() {
        return playersInPot;
    }

    protected void removePlayerFromPot(int playerPosition) {
        playersInPot[playerPosition] = false;
    }

    protected int max() {
        int maxPotValue = 0;
        for (int i = 0; i < size.length; i++) {
            if (size[i] > maxPotValue) {
                maxPotValue = getSize(i);
            }
        }
        return maxPotValue;
    }

    public int toCall(int playerPosition) {
        return max() - getSize(playerPosition);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Pot:");
        for (int i = 0; i < playersInPot.length; i++) {
            result.append(' ');
            if (playersInPot[i]) {
                result.append("INDEX_");
            } else {
                result.append("DEAD_");
            }
            result.append(i);
            result.append(' ');
            result.append(size[i]);
        }
        return result.toString();
    }

}
