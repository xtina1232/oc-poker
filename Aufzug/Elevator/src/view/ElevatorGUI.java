package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Starts the GUI.
 * 
 * @author Matthias Sommer
 * 
 */
public class ElevatorGUI extends Application {
	public static void main(String[] args) {
		Application.launch(ElevatorGUI.class, args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("elevator_scenario.fxml"));

		stage.setTitle("Elevator scenario");
		stage.setScene(new Scene(root));
		stage.show();
	}
}
