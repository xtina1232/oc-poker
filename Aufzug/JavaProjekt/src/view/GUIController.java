package view;

import java.util.List;

import model.Config;
import model.Elevator;
import model.ElevatorSimulation;
import model.Floor;
import model.statistics.Statistic;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Controller for the JavaFX GUI.
 * 
 * @author Matthias Sommer
 * 
 */
public class GUIController {
	@FXML
	private Label arrivedPassengersLabel;
	@FXML
	private Label averageTripTimeLabel;
	@FXML
	private Label averageWaitingTimeLabel;
	@FXML
	private GridPane elevatorGrid;
	private List<Elevator> elevators;
	@FXML
	private GridPane floorGrid;
	private List<Floor> floors;
	@FXML
	private TextField numberOfSteps;
	@FXML
	private Label personsInBuildingLabel;
	private ElevatorSimulation simulation;
	@FXML
	private Button startSimulationBtn;
	@FXML
	private Button setupSimulationBtn;
	@FXML
	private Button makeOneStepBtn;
	@FXML
	private Button stopSimulationBtn;
	@FXML
	private Label timeLabel;
	@FXML
	private Label waitingPassengerLabel;

	private Label[] waitingPersonLabels = new Label[10];
	private SimThread simulationThread;

	private int calculateCellPosition(int column, int row) {
		int size = this.elevatorGrid.getChildren().size() - 1;
		return size - (Config.numberOfElevators * row) - (Config.numberOfElevators - column);
	}

	private void createFloorPane(int floor) {
		GridPane grid = new GridPane();
		GridPane.setMargin(grid, new Insets(10));

		Label l1 = new Label("Waiting persons ");
		grid.add(l1, 0, 0);
		Label l2 = new Label("0");
		grid.add(l2, 1, 0);
		Label l3 = new Label("Floor ID ");
		grid.add(l3, 0, 1);
		Label l4 = new Label("" + floor);
		grid.add(l4, 1, 1);

		this.floorGrid.add(grid, 0, (9 - floor));

		this.waitingPersonLabels[floor] = l2;
	}

	private void getStatistics() {
		this.arrivedPassengersLabel.setText("" + Statistic.getInstance().getArrivedPassengers());

		float waitingTime = Statistic.getInstance().getAverageWaitingTime();
		if (!Float.isNaN(waitingTime))
			this.averageWaitingTimeLabel.setText("" + waitingTime);

		this.waitingPassengerLabel.setText("" + Statistic.getInstance().getWaitingPassengers());

		float tripTime = Statistic.getInstance().getAverageWaitingAndTravelingTime();
		if (!Float.isNaN(tripTime))
			this.averageTripTimeLabel.setText("" + tripTime);

		this.timeLabel.setText(this.simulation.getTime());
		this.personsInBuildingLabel.setText("" + Statistic.getInstance().getPersonsInBuilding());
	}

	@FXML
	protected void makeOneStep() {
		this.simulation.step();
		updateElevatorPositions();
		updateWaitingPersonsLabels();
		getStatistics();
	}

	private void setupFloorGUI() {
		for (int i = 0; i < this.floors.size(); i++) {
			createFloorPane(i);
		}
	}

	/**
	 * Initialise simulation with elevators, floors and passengers
	 */
	@FXML
	private void setupSimulation() {
		this.simulation = new ElevatorSimulation();
		this.elevators = this.simulation.getElevators();
		this.floors = this.simulation.getFloors();

		this.startSimulationBtn.setDisable(false);
		this.makeOneStepBtn.setDisable(false);
		this.stopSimulationBtn.setDisable(false);
		this.setupSimulationBtn.setDisable(true);

		updateElevatorPositions();
		setupFloorGUI();
	}

	@FXML
	private void step() {
		this.simulationThread = new SimThread(this);
		new Thread(this.simulationThread).start();
	}

	@FXML
	private void stopSimulation() {
		this.simulationThread.setRunning(false);
	}

	private void updateElevatorPositions() {
		for (Elevator elevator : this.elevators) {
			if (elevator.getLastFloor() != -1) {
				int oldCellPosition = calculateCellPosition(elevator.getId(), elevator.getLastFloor());
				Pane pane = (Pane) this.elevatorGrid.getChildren().get(oldCellPosition);
				pane.setStyle("-fx-background-color: #FFFFFF;");
				pane.getChildren().clear();
			}

			if (elevator.getLastFloor() != elevator.getCurrentFloor()) {
				int cellPosition = calculateCellPosition(elevator.getId(), elevator.getCurrentFloor());
				Pane pane = (Pane) this.elevatorGrid.getChildren().get(cellPosition);
				pane.getChildren().clear();

				Text text = new Text(20, 20, "No. pass.\n" + elevator.getPassengerCount() + "/"
						+ Config.elevatorCapacity);
				text.setFill(Color.WHITE);
				text.setFont(Font.font("Monospaced", 15));
				pane.getChildren().add(text);
				pane.setStyle("-fx-background-color: #336699;");
			}
		}
	}

	private void updateWaitingPersonsLabels() {
		for (Floor floor : this.floors) {
			Label label = this.waitingPersonLabels[floor.getId()];
			int waitingPassengers = floor.getWaitingPassengersCount();
			label.setText("" + waitingPassengers);
		}
	}
}