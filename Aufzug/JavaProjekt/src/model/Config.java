package model;

/**
 * Configuration parameters for the simulation
 * 
 * @author Matthias Sommer
 * 
 */
public class Config {
	// number of steps for one day
	public final static int stepsPerDay = 8640;
	public final static int numberOfFloors = 10;
	public final static int numberOfElevators = 4;
	public final static int elevatorCapacity = 7;
	// should elevator be initialised randomly or at the basement
	public final static boolean generateElevatorsRandom = false;
}
