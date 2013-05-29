package poker;

import poker.model.AbstractPlayer;
import poker.model.Game;
import poker.model.logic.Table;
import poker.players.FoldPlayer;
import poker.players.Peter;

/**
 * @author Ittner
 */
public class ConsoleLauncher {

	public static void main(String[] args) {
		Table.setDebugPrint(true);

		AbstractPlayer[] players = getPlayerConfiguration();
		int gameCount = getGameCount();
		boolean updateUI = false;
		new Game(players, gameCount, updateUI);

	}

	// Configuration

	private static AbstractPlayer[] getPlayerConfiguration() {
		AbstractPlayer[] players = new AbstractPlayer[] { new Peter(),
				new FoldPlayer() };
		return players;
	}

	private static int getGameCount() {
		return 100;
	}

}
