package poker.gui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import poker.model.Card;

/**
 * @author Becker
 */
public class CardsPanel extends JPanel {

    private static final long serialVersionUID = 7195629271658223582L;

    private final JLabel[] lblCards;

    public CardsPanel(int numberOfCards) {
        super(new MigLayout("ins 0"));

        this.setOpaque(false);

        lblCards = new JLabel[numberOfCards];
        for (int i = 0; i < numberOfCards; ++i) {
            lblCards[i] = new JLabel();
            lblCards[i].setOpaque(false);
            lblCards[i].setHorizontalAlignment(JLabel.CENTER);
            lblCards[i].setBorder(BorderFactory.createLineBorder(lblCards[i].getBackground().darker().darker()));

            this.add(lblCards[i], "w 30!, h 40!");
        }
    }

    public void setCards(Card[] cards) {
        for (int i = 0; i < lblCards.length; ++i) {
            if (cards[i] != null) {
                lblCards[i].setText(cards[i].toString());
            } else {
                lblCards[i].setText("");
            }
        }
    }
}
