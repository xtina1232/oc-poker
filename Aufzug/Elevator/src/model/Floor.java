package model;

import java.util.List;
import java.util.Vector;

import model.statistics.Statistic;

/**
 * A floor has an unique id and none or many passengers waiting for the
 * elevator.
 * 
 * @author Matthias Sommer
 * 
 */
public class Floor {
	private List<Passenger> waitingPassengers;
	private int id;

	public Floor(int floorNumber) {
		this.waitingPassengers = new Vector<>();
		this.id = floorNumber;
	}

	public void sendFirstPassengerToElevator(Elevator elevator) {
		if (!waitingPassengers.isEmpty()) {
			Passenger passenger = this.waitingPassengers.remove(0);
			elevator.addPassenger(passenger);
			Statistic.getInstance().reportPassengerDeparture(passenger, this.id);
		}
	}

	public int getWaitingPassengersCount() {
		return this.waitingPassengers.size();
	}

	public int getId() {
		return this.id;
	}

	public void step() {
		for (Passenger p : this.waitingPassengers) {
			p.step();
		}
		createPassenger();
	}

	private void createPassenger() {
		double probability = .0;
		if (id == 0) {
			probability = Math.max(
					0.05,
					Math.exp(Math.pow(Statistic.getInstance().getTodaysStep() - 3240, 2) / -18000.0)
							+ Math.exp(Math.pow(Statistic.getInstance().getTodaysStep() - 4680, 2) / -18000.0) / 1.1);
			if (Math.random() < probability) {
				Passenger passenger = new Passenger(id);
				this.waitingPassengers.add(passenger);
				Statistic.getInstance().reportPassengerSpawn(passenger);
				Statistic.getInstance().reportPassengerEntersBuilding(passenger);
			}
		} else {
			probability = Math.max(
					0.005,
					(Math.exp(Math.pow(Statistic.getInstance().getTodaysStep() - 6300, 2) / -18000.0) + Math.exp(Math
							.pow(Statistic.getInstance().getTodaysStep() - 4320, 2) / -18000.0)) / 10.5);
			if (Math.random() < probability) {
				Passenger passenger = new Passenger(id);
				this.waitingPassengers.add(passenger);
				Statistic.getInstance().reportPassengerSpawn(passenger);
			}
		}

	}
}