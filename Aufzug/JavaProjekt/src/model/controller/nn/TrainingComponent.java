package model.controller.nn;

import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;

/**
 * Trains network (weights of neurons) for specified training and validation set
 * 
 * @author Matthias Sommer
 * 
 */
public class TrainingComponent {
	/**
	 * Trains network based on different training strategies.
	 * 
	 * @param network
	 * @param validationSet
	 *            data set is used to minimize overfitting.
	 * @param trainingSet
	 *            data set is used to adjust the weights on the neural network.
	 */
	public static void learnTrainingSet(BasicNetwork network, MLDataSet validationSet, MLDataSet trainingSet) {
		// TODO Auto-generated method stub
		
	}

}
