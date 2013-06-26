package poker.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.*;
// import net.miginfocom.swing.MigLayout.*; // original

import poker.model.AbstractPlayer;
import poker.model.Card;
import poker.model.Game;
import poker.model.GameState;
import poker.model.logic.Table;

/**
 * @author Becker
 */
public class GameWindow extends JFrame implements ActionListener, ChangeListener {

    private static GameWindow INSTANCE = null;

    public static GameWindow getInstance() {
        if (INSTANCE == null) INSTANCE = new GameWindow();
        return INSTANCE;
    }

    private static final long serialVersionUID = -7940476308412032079L;
    private static final int NUMBER_OF_PLAYERS = 10;
    private static final int GAMECOUNT = 2000;

    private JSpinner spnNumberGames;
    private JSpinner spnStartStack;
    private JSpinner spnStartBlind;
    private JSpinner spnIncreaseBlind;

    private PlayerSelection[] cmbPlayers;
    private JCheckBox[] chkDealer;
    private JLabel[] lblStacks;
    private JLabel[] lblPot;
    private JLabel[] lblWins;

    private CardsPanel pnlBoard;
    private JLabel lblPotSize;
    private JLabel lblGameCount;

    private JButton btnPlay;
    private JButton btnReset;
    private JButton btnClose;

    private GameWindow() {
        super("Poker (SRA)");

        initGui();
    }

    private void initGui() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.setLayout(new MigLayout("ins 10, wrap 5", "[]20[center]10[center]10[center]20[center]"));

        this.add(new JLabel("Player:"));
        this.add(new JLabel("Dealer:"));
        this.add(new JLabel("Size of Stack:"));
        this.add(new JLabel("Chips in Pot:"));
        this.add(new JLabel("Games won:"));

        this.add(new JSeparator(), "spanx, growx, pushx");

        this.cmbPlayers = new PlayerSelection[NUMBER_OF_PLAYERS];
        this.chkDealer = new JCheckBox[NUMBER_OF_PLAYERS];
        this.lblStacks = new JLabel[NUMBER_OF_PLAYERS];
        this.lblPot = new JLabel[NUMBER_OF_PLAYERS];
        this.lblWins = new JLabel[NUMBER_OF_PLAYERS];

        for (int i = 0; i < NUMBER_OF_PLAYERS; ++i) {
            this.add(cmbPlayers[i] = new PlayerSelection());

            this.add(chkDealer[i] = new JCheckBox());
            chkDealer[i].setEnabled(false);

            this.add(lblStacks[i] = new JLabel("0"));

            this.add(lblPot[i] = new JLabel("0"));

            this.add(lblWins[i] = new JLabel("0"));
        }

        this.add(new JSeparator(), "spanx, growx, pushx");

        this.add(pnlBoard = new CardsPanel(5), "spanx 3, spany 2, alignx center");

        this.add(new JLabel("Potsize:"));

        this.add(new JLabel("Game Count:"));

        this.add(lblPotSize = new JLabel("0"));

        this.add(lblGameCount = new JLabel("0"));

        this.add(new JSeparator(), "spanx, growx, pushx, gapbottom 5");

        this.add(createSettingsPanel(), "spanx, growx, pushx");

        this.add(new JSeparator(), "spanx, growx, pushx, gapbottom 5");

        btnPlay = new JButton("Play");
        btnPlay.addActionListener(this);
        this.add(btnPlay, "spanx, split 3, alignx right, sizegroup buttons");

        btnReset = new JButton("Reset");
        btnReset.addActionListener(this);
        this.add(btnReset, "gapleft 10, sizegroup buttons");

        btnClose = new JButton("Close");
        btnClose.addActionListener(this);
        this.add(btnClose, "gapleft 10, sizegroup buttons");

        this.pack();

        this.setLocationRelativeTo(null);
    }

    private JPanel createSettingsPanel() {
        JPanel pnlSettings = new JPanel(new MigLayout("ins 0, wrap 4", "[grow,fill][grow,fill][grow,fill][grow,fill]"));

        pnlSettings.add(new JLabel("Number of Games:"));

        pnlSettings.add(new JLabel("Startstack:"));

        pnlSettings.add(new JLabel("Startblind:"));

        pnlSettings.add(new JLabel("Increase Blinds:"));

        pnlSettings.add(spnNumberGames = new JSpinner(new SpinnerNumberModel(GAMECOUNT, 1, 100000, 1)), "sizegroup spinner");

        pnlSettings.add(spnStartStack = new JSpinner(new SpinnerNumberModel(Table.DEFAULT_START_STACK, 100, 100000, 50)), "sizegroup spinner");
        spnStartStack.addChangeListener(this);

        pnlSettings.add(spnStartBlind = new JSpinner(new SpinnerNumberModel(Table.DEFAULT_START_BLIND, 2, 25000, 2)), "sizegroup spinner");

        pnlSettings.add(spnIncreaseBlind = new JSpinner(new SpinnerNumberModel(Table.DEFAULT_INCREASE_BLIND, 1, 100, 1)), "sizegroup spinner");

        return pnlSettings;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPlay) {
            int playersInGame = 0;
            final AbstractPlayer[] players = new AbstractPlayer[NUMBER_OF_PLAYERS];
            for (int i = 0; i < NUMBER_OF_PLAYERS; ++i) {
                players[i] = cmbPlayers[i].getSelectedPlayer();
                if (players[i] != null) ++playersInGame;
            }

            if (playersInGame > 1) {
                final int gameCount = (Integer) spnNumberGames.getValue();
                final int startStack = (Integer) spnStartStack.getValue();
                final int startBlind = (Integer) spnStartBlind.getValue();
                final int increaseBlind = (Integer) spnIncreaseBlind.getValue();

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        final int[] ranking = new Game(players, gameCount, startStack, startBlind, increaseBlind, true).getWinCount();
                        reset();
                        setWins(gameCount, ranking);
                    }

                }, "GameRunner").start();
            } else {
                JOptionPane.showMessageDialog(this, "Bitte mindestens zwei Spieler auswaehlen!");
            }
        } else if (e.getSource() == btnReset) {
            reset();
        } else if (e.getSource() == btnClose) {
            askForClose();
        }
    }

    private void reset() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < NUMBER_OF_PLAYERS; ++i) {
                    chkDealer[i].setSelected(false);
                    lblStacks[i].setText("0");
                    lblStacks[i].setEnabled(true);
                    lblPot[i].setText("0");
                    lblPot[i].setEnabled(true);
                    lblWins[i].setText("0");
                }

                pnlBoard.setCards(new Card[5]);
                lblPotSize.setText("0");
                lblGameCount.setText("0");
            }

        });
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == spnStartStack) {
            ((SpinnerNumberModel) spnStartBlind.getModel()).setMaximum(Integer.parseInt(spnStartStack.getValue().toString()));
            if ((Integer) spnStartBlind.getValue() > (Integer) ((SpinnerNumberModel) spnStartBlind.getModel()).getMaximum()) {
                spnStartBlind.setValue(((SpinnerNumberModel) spnStartBlind.getModel()).getMaximum());
            }
        }
    }

    public void askForClose() {
        if (JOptionPane.showConfirmDialog(this, "Spiel wirklich beenden?", "Beenden?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (PlayerSelection ps : cmbPlayers) {
            ps.setChangeable(enabled);
        }

        spnNumberGames.setEnabled(enabled);
        spnStartStack.setEnabled(enabled);
        spnStartBlind.setEnabled(enabled);
        spnIncreaseBlind.setEnabled(enabled);

        btnPlay.setEnabled(enabled);
        btnReset.setEnabled(enabled);
    }

    public void updateComponents(final GameState gs) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < NUMBER_OF_PLAYERS; ++i) {
                    boolean inGame = gs.isPlayerIn(i);

                    if (inGame) {
                        chkDealer[i].setSelected(i == gs.getDealer());
                        lblStacks[i].setText(String.valueOf(gs.getStack(i)));
                        lblPot[i].setText(String.valueOf(gs.getChipsInPot(i)));
                    } else {
                        chkDealer[i].setSelected(false);
                        lblStacks[i].setText("0");
                        lblPot[i].setText("0");
                    }

                    lblStacks[i].setEnabled(inGame);
                    lblPot[i].setEnabled(inGame);
                }

                pnlBoard.setCards(gs.getBoard());
                lblPotSize.setText(String.valueOf(gs.getPotSize()));
            }

        });
    }

    public void setWins(final int gameCount, final int[] wins) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                lblGameCount.setText(String.valueOf(gameCount));
                for (int i = 0; i < NUMBER_OF_PLAYERS; ++i) {
                    lblWins[i].setText(String.valueOf(wins[i]));
                }
            }

        });
    }

}
