package model;

import java.util.List;
import java.util.Vector;

import model.controller.ControllerInterface;
import model.controller.LCSController;
//import model.controller.SimpleController;
import model.statistics.Statistic;

/**
 * Simulation class for the elevator scenario.
 * 
 * @author Matthias Sommer
 * 
 */
public class ElevatorSimulation {
	private ControllerInterface controller;
	private List<Elevator> elevators;
	private List<Floor> floors;

	/**
	 * Instantiates a new simulation
	 */
	public ElevatorSimulation() {
		this.floors = new Vector<>();
		for (int i = 0; i < Config.numberOfFloors; i++) {
			this.floors.add(new Floor(i));
		}

		this.elevators = new Vector<>();
		for (int i = 0; i < Config.numberOfElevators; i++) {
			this.elevators.add(new Elevator(floors, i));
		}

		//this.controller = new SimpleController(this.elevators, this.floors);
		this.controller = new LCSController(this.elevators, this.floors);
	}

	public List<Elevator> getElevators() {
		return elevators;
	}

	public List<Floor> getFloors() {
		return floors;
	}

	public String getTime() {
		int step = Statistic.getInstance().getStep();
		int stepsPerDay = Config.stepsPerDay;
		return "Day " + step / stepsPerDay + " Step " + step % stepsPerDay;
	}

	/**
	 * Runs one iteration of the simulation
	 */
	public void step() {
		Statistic.getInstance().step();

		for (Floor f : this.floors) {
			f.step();
		}

		this.controller.step();

		for (Elevator e : this.elevators) {
			e.step();
		}
	}
}