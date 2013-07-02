package model;

/**
 * A passenger know his destination.
 * 
 * @author Matthias Sommer
 * 
 */
public class Passenger {
	private int destination;
	private int timeNeededToDestination = 0;

	// Most passengers start from the ground floor or have this one as
	// destination
	protected Passenger(int startFloor) {
		if (startFloor == 0) {
			this.destination = (int) (Math.random() * 9.0) + 1;
		} else {
			if (Math.random() < 0.9) {
				this.destination = 0;
			} else {
				this.destination = startFloor;
				while (startFloor == this.destination) {
					this.destination = (int) (Math.random() * 9.0) + 1;
				}
			}
		}
	}

	protected int getDestination() {
		return this.destination;
	}

	public int getTime() {
		return this.timeNeededToDestination;
	}

	protected void step() {
		this.timeNeededToDestination++;
	}
}
