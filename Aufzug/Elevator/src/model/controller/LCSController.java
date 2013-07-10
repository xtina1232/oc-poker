package model.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import model.Elevator;
import model.Elevator.Directions;
import model.Floor;
import model.controller.nn.ParameterProvider;
import model.statistics.Statistic;
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
	private LinkedList<List<Classifier>> history;
	private int[] passengerCounts;
	private final float BETA = 0.3f;
	private final float GAMMA = 0.71f;
	private float lastAverageTripTime = 0;
	
	
	public LCSController(List<Elevator> elevators, List<Floor> floors) {
		this.elevators = elevators;
		this.floors = floors;
		this.classifierContainer = ClassifierContainer.getInstance();
		this.history = new LinkedList<List<Classifier>>();
		this.passengerCounts = new int[]{0, 0, 0, 0};
	}

	private long counter = 0;
	
	public void step() {

		if(ParameterProvider.PRINT) {
			if(counter % 1000 == 0) {
				classifierContainer.debugClassifiers();
			}
		}
		
		// Umgebungszustand einlesen
		List<Integer> state = getState();
		
		// Get the best fitting actions
		List<Classifier> actionSet = classifierContainer.getActionSet(state);

		if(ParameterProvider.DEBUG) {
		
			System.out.println("ActionSet in this Step contains: " + actionSet.size() + " Classifiers");
		
		}
		
		// Execute the Actions
		if (actionSet.size() > 0) {
			history.addFirst(actionSet);
			if (history.size() >= 10) {
				history.removeLast();
			}
			lastAverageTripTime = Statistic.getInstance().getAverageWaitingAndTravelingTime();
			performAction(actionSet.get(0).getAction());
			
		}
		
		
		
		// Bewertung
		boolean doEvaluate = false;
		for(int i=0; i<4; i++) {
			if(passengerCounts[i] != elevators.get(i).getPassengerCount()) {
				doEvaluate = true;
				passengerCounts[i] = elevators.get(i).getPassengerCount();
			}
		}
		
		if(doEvaluate) {
			
			// Vereinfachte Variante der Bewertung
			if(history.size() > 0) {
				List<Classifier> currentActionSet = history.getFirst();
				float currentAverageTripTime = Statistic.getInstance().getAverageWaitingAndTravelingTime();
				if(currentAverageTripTime < lastAverageTripTime) {
					if(ParameterProvider.PRINT) {
						System.out.println("Belohne Action:" + currentActionSet.get(0).getAction());
					}
					
					for(Classifier c : currentActionSet) {
						c.setFitness(c.getFitness() + 10); 
					}
				} else {
					if(ParameterProvider.PRINT) {
						System.out.println("Bestrafe Action:" + currentActionSet.get(0).getAction());
					}
					
					for(Classifier c : currentActionSet) {
						c.setFitness(Math.max(c.getFitness() - 2,0)); 
					}
				}
				previousWaitingTime = currentWaitingTime;
			}
			
			
//			Alte Bewertung die noch nicht funktioniert, aber abbilded was eigentlich zu tun ist
//			=> Vorherige ActionSets immer weniger belohnen oder bestrafen
			
//			if(history.size() > 0) {
//				List<Classifier> lastActionSet = history.get(0);
//				int wholeFitness = calculateFitness(lastActionSet);
//				evaluateActionSet(lastActionSet, wholeFitness);
//				
//				int currentFitness = Math.round(wholeFitness * GAMMA);
//				for(int i=0; i<history.size(); i++) {
//					if (currentFitness <= 1)
//						break;
//					evaluateActionSet(history.get(i), currentFitness);
//					currentFitness = Math.round(currentFitness * GAMMA);
//				}
//			}
		}
		
		
		// Mutate zu 50 % Wahrscheinlichkeit
		
		Random rand = new Random();
		if(rand.nextFloat() < 0.5) {
			this.classifierContainer.mutate();	
		}
		
		counter++;
	}
	
	
	private int calculateFitness (List<Classifier> actionSet) {
		int wholeFitness = 0;
		for(Classifier c : actionSet) {
			int deltaFitness = Math.round(c.getFitness() * BETA);
			c.setFitness(c.getFitness() - deltaFitness);
			wholeFitness += deltaFitness;
		}
		return wholeFitness;
	}
	
	private void evaluateActionSet (List<Classifier> actionSet, int amount) {
		int len = actionSet.size();
		int subAmount = amount / len;
		for(Classifier c : actionSet) {
			c.setFitness(subAmount);
		}
	}

	private List<Integer> getState() {
		List<Integer> state = new ArrayList<Integer>();
		for (Floor f : floors) {
			state.add(f.getWaitingPassengersCount() > 0 ? 1 : 0);
		}
		for (Elevator e : elevators) {
			state.add(e.getCurrentFloor());
			state.add(e.getDirection().ordinal());
		}
		return state;
	}

	/**
	 * Führt eine Aktion aus
	 */
	private void performAction(int action) {
		
		if(ParameterProvider.PRINT) {
		
			System.out.println("Performing Action:" + action);
		
		}
		
		Elevator e1 = elevators.get(0);
		Elevator e2 = elevators.get(1);
		Elevator e3 = elevators.get(2);
		Elevator e4 = elevators.get(3);
		
		switch (action) {
		case 0:
			break;
		case 1:
			e1.setDirection(Directions.UP);
			break;
		case 2:
			e2.setDirection(Directions.UP);
			break;
		case 3:
			e1.setDirection(Directions.UP);
			e2.setDirection(Directions.UP);
			break;
		case 4:
			e3.setDirection(Directions.UP);
			break;
		case 5:
			e1.setDirection(Directions.UP);
			e3.setDirection(Directions.UP);
			break;
		case 6:
			e2.setDirection(Directions.UP);
			e3.setDirection(Directions.UP);
			break;
		case 7:
			e1.setDirection(Directions.UP);
			e2.setDirection(Directions.UP);
			e3.setDirection(Directions.UP);
			break;
		case 8:
			e4.setDirection(Directions.UP);
			break;
		case 9:
			e1.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 10:
			e2.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 11:
			e1.setDirection(Directions.UP);
			e2.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 12:
			e3.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 13:
			e1.setDirection(Directions.UP);
			e3.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 14:
			e2.setDirection(Directions.UP);
			e3.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 15:
			e1.setDirection(Directions.UP);
			e2.setDirection(Directions.UP);
			e3.setDirection(Directions.UP);
			e4.setDirection(Directions.UP);
			break;
		case 17:
			e1.setDirection(Directions.DOWN);
			break;
		case 18:
			e2.setDirection(Directions.DOWN);
			break;
		case 19:
			e1.setDirection(Directions.DOWN);
			e2.setDirection(Directions.DOWN);
			break;
		case 20:
			e3.setDirection(Directions.DOWN);
			break;
		case 21:
			e1.setDirection(Directions.DOWN);
			e3.setDirection(Directions.DOWN);
			break;
		case 22:
			e2.setDirection(Directions.DOWN);
			e3.setDirection(Directions.DOWN);
			break;
		case 23:
			e1.setDirection(Directions.DOWN);
			e2.setDirection(Directions.DOWN);
			e3.setDirection(Directions.DOWN);
			break;
		case 24:
			e4.setDirection(Directions.DOWN);
			break;
		case 25:
			e1.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 26:
			e2.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 27:
			e1.setDirection(Directions.DOWN);
			e2.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 28:
			e3.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 29:
			e1.setDirection(Directions.DOWN);
			e3.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 30:
			e2.setDirection(Directions.DOWN);
			e3.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 16:
			e1.setDirection(Directions.DOWN);
			e2.setDirection(Directions.DOWN);
			e3.setDirection(Directions.DOWN);
			e4.setDirection(Directions.DOWN);
			break;
		case 31:
			e1.setDirection(Directions.NONE);
			break;
		case 32:
			e2.setDirection(Directions.NONE);
			break;
		case 33:
			e1.setDirection(Directions.NONE);
			e2.setDirection(Directions.NONE);
			break;
		case 34:
			e3.setDirection(Directions.NONE);
			break;
		case 35:
			e1.setDirection(Directions.NONE);
			e3.setDirection(Directions.NONE);
			break;
		case 36:
			e2.setDirection(Directions.NONE);
			e3.setDirection(Directions.NONE);
			break;
		case 37:
			e1.setDirection(Directions.NONE);
			e2.setDirection(Directions.NONE);
			e3.setDirection(Directions.NONE);
			break;
		case 38:
			e4.setDirection(Directions.NONE);
			break;
		case 39:
			e1.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		case 40:
			e2.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		case 41:
			e1.setDirection(Directions.NONE);
			e2.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		case 42:
			e3.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		case 43:
			e1.setDirection(Directions.NONE);
			e3.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		case 44:
			e2.setDirection(Directions.NONE);
			e3.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		case 45:
			e1.setDirection(Directions.NONE);
			e2.setDirection(Directions.NONE);
			e3.setDirection(Directions.NONE);
			e4.setDirection(Directions.NONE);
			break;
		}
	}

	/*private Elevator closestFreeElevator(int floor) {
		int shortestDistance = 100;
		Elevator closestElevator = null;

		for (Elevator e : elevators) {
			if (e.isFree() && shortestDistance > Math.abs(floor -
					e.getCurrentFloor())) {
				shortestDistance = Math.abs(floor - e.getCurrentFloor());
				closestElevator = e;
			}
		}

		return closestElevator;
	}*/
}
