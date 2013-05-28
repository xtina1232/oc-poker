package poker.players.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.miginfocom.swing.MigLayout;
import poker.gui.CardsPanel;
import poker.gui.GameWindow;
import poker.model.Action;
import poker.model.Action.PlayerAction;
import poker.model.Card;
import poker.model.GameState;

/**
 * This class represents the dialog for choosing the desired action.
 * 
 * @author Becker
 */
class InputDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 6149972860830513072L;

    private CardsPanel pnlBoard;
    private JLabel lblInfo;
    private CardsPanel pnlHand;
    private JButton btnFold;
    private JButton btnCheckCall;
    private SpinnerNumberModel spnAmountModel;
    private JSpinner spnAmount;
    private JButton btnRaise;
    private JButton btnEndGame;

    private Action wishedAction;

    protected InputDialog() {
        super(GameWindow.getInstance(), "Spieler", true);

        initGui();
    }

    private void initGui() {
        pnlBoard = new CardsPanel(5);

        lblInfo = new JLabel("Karten:");
        pnlHand = new CardsPanel(2);

        btnFold = new JButton("Fold");
        btnFold.addActionListener(this);

        btnCheckCall = new JButton("Call 1000 (All-In)");
        btnCheckCall.addActionListener(this);

        spnAmountModel = new SpinnerNumberModel(1, 1, 1000, 10);
        spnAmount = new JSpinner(spnAmountModel);

        btnRaise = new JButton("Raise");
        btnRaise.addActionListener(this);

        btnEndGame = new JButton("Spiel beenden");
        btnEndGame.addActionListener(this);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setLayout(new MigLayout("ins 5, wrap 1", "[grow, fill, center]"));

        add(pnlBoard);

        add(lblInfo, "growx, spanx, split 2");
        add(pnlHand);

        add(btnFold, "gaptop 10");
        add(btnCheckCall);
        add(spnAmount, "growy, split 2");
        add(btnRaise);

        add(btnEndGame, "gaptop 10");

        pack();
    }

    protected Action getAction(int id, GameState gs, Card[] hand, int callSize) {
        this.wishedAction = null;

        int ownStack = gs.getStack(id);

        setTitle("Spieler " + id);

        pnlBoard.setCards(gs.getBoard());

        lblInfo.setText("Stack: " + ownStack);
        pnlHand.setCards(hand);

        if (callSize == 0) {
            btnCheckCall.setText("Check");
        } else if (ownStack <= callSize) {
            btnCheckCall.setText("Call " + ownStack + " (All-In)");
        } else {
            btnCheckCall.setText("Call " + callSize);
        }

        int minChipsToRaise = callSize + gs.getMinimumRaise();
        if (ownStack <= minChipsToRaise) {
            spnAmountModel.setMinimum(0);
            spnAmountModel.setMaximum(0);
            spnAmountModel.setValue(0);

            spnAmount.setEnabled(false);
            btnRaise.setEnabled(false);
        } else {
            spnAmountModel.setMinimum(minChipsToRaise);
            spnAmountModel.setMaximum(ownStack);
            spnAmountModel.setValue(minChipsToRaise);

            spnAmount.setEnabled(true);
            btnRaise.setEnabled(true);
        }

        /*
         * Blocks the current thread (the running game) until the Dialog is closed.
         */
        setVisible(true);

        return wishedAction;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnFold) {
            wishedAction = new Action(PlayerAction.FOLD);

            setVisible(false);
        } else if (e.getSource() == btnCheckCall) {
            wishedAction = new Action(PlayerAction.CALL);

            setVisible(false);
        } else if (e.getSource() == btnRaise) {
            wishedAction = new Action(PlayerAction.RAISE, (Integer) spnAmountModel.getValue());

            setVisible(false);
        } else if (e.getSource() == btnEndGame) {
            GameWindow.getInstance().askForClose();
        }
    }

}
