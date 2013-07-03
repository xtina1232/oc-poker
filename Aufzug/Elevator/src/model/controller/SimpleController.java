package model.controller;

import java.util.List;

import model.Elevator;
import model.Elevator.Directions;
import model.Floor;

/**
 * Simple controller for the elevator scenario.
 * 
 * @author Stefan Rudolph
 * 
 */
public class SimpleController implements ControllerInterface {
	protected List<Elevator> elevators;
	protected List<Floor> floors;

	public SimpleController(List<Elevator> elevators, List<Floor> floors) {
		this.elevators = elevators;
		this.floors = floors;
	}

	public void step() {
		// Belegte fahrstühle in die gleiche richtung weiter, so lange noch
		// passagiere in diese richtung wollen
		for (Elevator e : this.elevators) {
			if (!e.isFree()) {
				if (!e.rightDirection()) {
					e.changeDirection();
				}
			}
		}

		// wartende Passagiere werden vom nächsten Fahrstuhl bedient
		for (Floor floor : this.floors) {
			if (floor.getWaitingPassengersCount() != 0) {
				Elevator e = closestFreeElevator(floor.getId());
				if (e != null) {
					if (e.getCurrentFloor() - floor.getId() > 0)
						e.setDirection(Directions.DOWN);
					else
						e.setDirection(Directions.UP);
				}
			}
		}
	}

	private Elevator closestFreeElevator(int floor) {
		int shortestDistance = 100;
		Elevator closestElevator = null;

		for (Elevator e : elevators) {
			if (e.isFree() && shortestDistance > Math.abs(floor - e.getCurrentFloor())) {
				shortestDistance = Math.abs(floor - e.getCurrentFloor());
				closestElevator = e;
			}
		}

		return closestElevator;
	}
}
