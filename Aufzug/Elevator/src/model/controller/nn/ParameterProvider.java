package model.controller.nn;

public class ParameterProvider {
	public static final boolean DEBUG = true;
	public static final boolean PRINT = true;

	// neural network
	public static final int FEED_FORWARD = 0;
	public static final int ELMAN_RECURRENT = 1;
	public static final int JORDAN_RECURRENT = 2;
	public static int NEURAL_NETWORK = FEED_FORWARD;

	// Training
	public static float maximalTrainingError = 0.002f;
	public static int maximalEpoche = 300;
}

