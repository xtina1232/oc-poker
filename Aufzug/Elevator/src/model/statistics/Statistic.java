package model.statistics;

import model.Config;
import model.Passenger;

/**
 * Collects statistics for the simulation.
 * 
 * @author Matthias Sommer
 * 
 */
public class Statistic {
	private static Statistic instance;
	private int step = 0;
	private int waiting = 0;
	private int arrived = 0;
	private int totalTime = 0;
	private int traveling = 0;
	private int todayArrived = 0;
	private int todayTotalTime = 0;
	private int waitingTime = 0;
	private int personsInBuilding = 0;
	
	private double waitingTimeLastHour = 0.0;
	private int arrivedLastHour = 0;

	public static Statistic getInstance() {
		if (instance == null) {
			instance = new Statistic();
		}
		return instance;
	}

	public void reportPassengerArrived(Passenger p, int floor) {
		this.traveling--;
		this.arrived++;
		this.todayArrived++;
		arrivedLastHour++;
		this.totalTime += p.getTime();

		if (floor == 0)
			if (this.personsInBuilding > 0)
				this.personsInBuilding--;
	}

	public float getAverageWaitingAndTravelingTime() {
		return (float) totalTime / (float) arrived;
	}

	public float getAverageWaitingTime() {
		return (float) waitingTime / (float) (traveling + arrived);
	}

	public float getTodaysAverageWaitingAndTravelingTime() {
		return (float) todayTotalTime / (float) todayArrived;
	}

	public void reportPassengerSpawn(Passenger passenger) {
		this.waiting++;
	}

	public void reportPassengerDeparture(Passenger passenger, int floor) {
		this.waiting--;
		this.traveling++;
		this.waitingTime += passenger.getTime();
		waitingTimeLastHour += passenger.getTime();
	}

	public int getWaitingPassengers() {
		return this.waiting;
	}

	public int getArrivedPassengers() {
		return this.arrived;
	}

	public void nextDay() {
		this.todayArrived = 0;
		this.todayTotalTime = 0;
	}

	public void step() {
		this.step++;
		if (this.step % Config.stepsPerDay == 0) {
			nextDay();
		}
	}

	public int getTodaysStep() {
		return this.step % Config.stepsPerDay;
	}

	public int getStep() {
		return this.step;
	}

	public void reportPassengerEntersBuilding(Passenger passenger) {
		this.personsInBuilding++;
	}

	public int getPersonsInBuilding() {
		return this.personsInBuilding;
	}
	
	public double getAverageWaitingTimeLastHour(){
		return (double) waitingTimeLastHour / (double) (traveling + arrivedLastHour);
	}
	
	public void nextHour(){
		waitingTimeLastHour = 0.0;
		arrivedLastHour = 0;
	}
}