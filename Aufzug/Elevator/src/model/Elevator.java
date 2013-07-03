package model;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import model.statistics.Statistic;

/**
 * Represents an elevator.
 * 
 * @author Matthias Sommer
 * 
 */
public class Elevator {
	public enum Directions {
		DOWN, NONE, UP
	}

	private int currentFloor;
	private Directions direction;
	private List<Floor> floors;
	private int id;
	private int lastFloor = -1;
	private List<Passenger> passengers;

	public Elevator(List<Floor> floors, int id) {
		this.id = id;
		this.floors = floors;
		this.direction = Directions.NONE;
		this.passengers = new Vector<>();
		if (Config.generateElevatorsRandom)
			this.currentFloor = (int) (Math.random() * floors.size());
		else
			this.currentFloor = 0;
	}

	protected void addPassenger(Passenger passenger) {
		this.passengers.add(passenger);
	}

	private void adjustMovingDirection() {
		if (this.passengers.isEmpty()) {
			this.direction = Directions.NONE;
		} else if (!rightDirection()) {
			changeDirection();
		}
	}

	public void changeDirection() {
		if (this.direction == Directions.DOWN)
			this.direction = Directions.UP;
		else if (this.direction == Directions.UP)
			this.direction = Directions.DOWN;
	}

	public int getCurrentFloor() {
		return this.currentFloor;
	}

	public int getId() {
		return this.id;
	}

	public int getLastFloor() {
		return this.lastFloor;
	}

	public int getPassengerCount() {
		return this.passengers.size();
	}

	public boolean isEmpty() {
		return this.passengers.isEmpty();
	}

	public boolean isFree() {
		return this.direction == Directions.NONE;
	}

	private boolean isFull() {
		return this.passengers.size() >= Config.elevatorCapacity;
	}

	private void moveElevator() {
		switch (this.direction) {
		case UP:
			if (this.currentFloor != this.floors.size() - 1) {
				this.lastFloor = this.currentFloor;
				this.currentFloor++;
			}
			break;
		case DOWN:
			if (this.currentFloor != 0) {
				this.lastFloor = this.currentFloor;
				this.currentFloor--;
			}
			break;
		case NONE:
			break;
		default:
			System.err.println("No valid direction!");
			break;
		}
	}

	private void passengersEnterElevator() {
		Floor floor = this.floors.get(this.currentFloor);
		while (floor.getWaitingPassengersCount() != 0 && !isFull()) {
			floor.sendFirstPassengerToElevator(this);
		}
	}

	/**
	 * Passenger(s) arrived at destination.
	 */
	private void passengersLeaveElevator() {
		Iterator<Passenger> it = this.passengers.iterator();
		while (it.hasNext()) {
			Passenger passenger = it.next();
			if (passenger.getDestination() == this.currentFloor) {
				Statistic.getInstance().reportPassengerArrived(passenger, currentFloor);
				it.remove();
			}
		}
	}

	/**
	 * 
	 * @return true if there is a passenger in the elevator that wants to go in
	 *         the current direction
	 */
	public boolean rightDirection() {
		boolean up = false;
		boolean down = false;

		for (Passenger p : this.passengers) {
			if (p.getDestination() - currentFloor > 0) {
				up = true;
			} else if (p.getDestination() - currentFloor < 0) {
				down = true;
			}
		}

		if (this.direction == Directions.UP) {
			return up;
		}

		return down;
	}

	public void setDirection(Directions d) {
		System.out.println("Setting direction for elevator "+this.getId()+" to "+d.ordinal());
		this.direction = d;
	}

	public Directions getDirection() {
		return direction;
	}
	
	protected void step() {
		for (Passenger p : this.passengers) {
			p.step();
		}

		moveElevator();
		passengersLeaveElevator();
		passengersEnterElevator();
		adjustMovingDirection();
	}
}