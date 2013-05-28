package poker.gui;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import poker.model.AbstractPlayer;
import poker.model.Configuration;

/**
 * @author Becker
 */
@SuppressWarnings("rawtypes")
class PlayerSelection extends JComboBox implements ActionListener, ItemListener {

    private static final long serialVersionUID = -8383315488113151964L;

    private Object changeBack = null;

    @SuppressWarnings("unchecked")
	protected PlayerSelection() {
        super(Configuration.getPlayerNames());
    }

    public AbstractPlayer getSelectedPlayer() {
        int index = getSelectedIndex() - 1;
        if (index < 0) return null;
        return Configuration.getPlayer(index);
    }

    public void setChangeable(boolean changeable) {
        if (!changeable) {
            this.addItemListener(this);
        } else {
            this.removeItemListener(this);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            if (changeBack == null) {
                changeBack = e.getItem();
            }
        } else if (e.getStateChange() == ItemEvent.SELECTED) {
            if (changeBack != null) {
                this.setSelectedItem(changeBack);
                changeBack = null;
            }
        }
    }

}
