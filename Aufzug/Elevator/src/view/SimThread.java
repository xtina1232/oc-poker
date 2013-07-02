package view;

import javafx.application.Platform;

/**
 * Executes the simulation in a separate thread
 * 
 * @author Matthias Sommer
 * 
 */
public class SimThread extends Thread {
	private GUIController controller;
	private boolean running = true;

	protected void setRunning(boolean running) {
		this.running = running;
	}

	protected SimThread(GUIController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		while (running) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					controller.makeOneStep();
				}
			});

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				interrupt();
				e.printStackTrace();
			}
		}
	}
}
