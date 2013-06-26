package poker.players.ui;

import poker.model.AbstractPlayer;
import poker.model.Action;
import poker.model.Card;
import poker.model.GameState;

/**
 * This player provides an {@link InputDialog UI} for selecting the desired action.
 * 
 * @author Becker
 */
public class UIPlayer extends AbstractPlayer {

    private InputDialog dialog;

    @Override
    public Action getAction(int position, GameState gs, Card[] hand, int callSize) {
        if (dialog == null) dialog = new InputDialog();
        return dialog.getAction(position, gs, hand, callSize);
    }

}
