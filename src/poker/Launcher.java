package poker;

import poker.gui.GameWindow;
import poker.model.AbstractPlayer;
import poker.model.Game;
import poker.model.logic.Table;
import poker.players.Peter;
import poker.players.CallPlayer;
import poker.players.FoldPlayer;


/**
 * @author Becker
 */
public class Launcher {

    public static void main(String[] args) {
        Table.setDebugPrint(true);
        
        GameWindow.getInstance().setVisible(true);
    }

}
