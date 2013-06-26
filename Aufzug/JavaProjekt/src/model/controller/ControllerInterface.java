package model.controller;

/**
 * Interface for controllers for the elevator scenario
 * 
 * @author Matthias Sommer
 * 
 */
public interface ControllerInterface {
	/**
	 * Method is called every iteration by the main simulation class
	 */
	public void step();
}
