package model.controller;

import java.util.ArrayList;
import java.util.List;

import model.Elevator;
import model.Floor;

/**
 * A LCS Controller
 * 
 * @author Alexander Poeppel, Peter Ittner, Dominik Schoeler
 * 
 */
public class LCSController implements ControllerInterface {
	protected List<Elevator> elevators;
	protected List<Floor> floors;
	protected ClassifierContainer classifierContainer;
	List<Classifier> previousActionSet;

	public LCSController(List<Elevator> elevators, List<Floor> floors) {
		this.elevators = elevators;
		this.floors = floors;
		this.classifierContainer = ClassifierContainer.getInstance();
		this.previousActionSet = null;
	}

	public void step() {
		
		// Get the state
		List<Boolean> state = new ArrayList<Boolean>();
		for(Floor f : floors) {
			state.add(f.getWaitingPassengersCount() != 0);
		}
		// TODO: Add more Variables of the Environment to state
		
		// Get the best fitting actions
		List<Classifier> actionSet = classifierContainer.getActionSet(state);
		
		// Execute those Actions
		for(Classifier c : actionSet) {
			c.getAction().execute();
		}
		
		// TODO: Assign Rewards to current and to previous ActionSet
		// Try to use: Statistic.getInstance().getAverageWaitingTime();
		
		if(previousActionSet != null) {
			
		}
		previousActionSet = actionSet;
		
		
		// Belegte fahrstühle in die gleiche richtung weiter, so lange noch
		// passagiere in diese richtung wollen
//		for (Elevator e : this.elevators) {
//			if (!e.isFree()) {
//				if (!e.rightDirection()) {
//					e.changeDirection();
//					
//				}
//			}
//		}
		

		// wartende Passagiere werden vom nächsten Fahrstuhl bedient
//		for (Floor floor : this.floors) {
//			if (floor.getWaitingPassengersCount() != 0) {
//				Elevator e = closestFreeElevator(floor.getId());
//				if (e != null) {
//					if (e.getCurrentFloor() - floor.getId() > 0)
//						e.setDirection(Directions.DOWN);
//					else
//						e.setDirection(Directions.UP);
//				}
//			}
//		}
	}

//	private Elevator closestFreeElevator(int floor) {
//		int shortestDistance = 100;
//		Elevator closestElevator = null;
//
//		for (Elevator e : elevators) {
//			if (e.isFree() && shortestDistance > Math.abs(floor - e.getCurrentFloor())) {
//				shortestDistance = Math.abs(floor - e.getCurrentFloor());
//				closestElevator = e;
//			}
//		}
//
//		return closestElevator;
//	}
}
