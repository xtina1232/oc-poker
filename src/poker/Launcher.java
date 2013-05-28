package poker;

import poker.gui.GameWindow;
import poker.model.logic.Table;


/**
 * @author Becker
 */
public class Launcher {

    public static void main(String[] args) {
        Table.setDebugPrint(true);
        
        GameWindow.getInstance().setVisible(true);
    }

}
