package model.controller.nn;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.util.simple.EncogUtility;

import model.controller.ControllerInterface;

/**
 * Main class for the neural network.
 * 
 * @author Matthias Sommer
 * 
 */
public class NeuralNetwork implements ControllerInterface {
	private BasicNetwork network;
	
	public NeuralNetwork(BasicNetwork network) {
		this.network = network;
	}
	
	public NeuralNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons) {
		switch (ParameterProvider.NEURAL_NETWORK) {
		case ParameterProvider.FEED_FORWARD:
			createFeedForwardNetwork(inputNeurons, hiddenNeurons, outputNeurons);
			break;
		case ParameterProvider.ELMAN_RECURRENT:
			createElmanNetwork(inputNeurons, hiddenNeurons, outputNeurons);
			break;
		case ParameterProvider.JORDAN_RECURRENT:
			createJordanNetwork(inputNeurons, hiddenNeurons, outputNeurons);
			break;
		default:
			createFeedForwardNetwork(inputNeurons, hiddenNeurons, outputNeurons);
		}
	}

	/**
	 * only one hidden layer is created
	 * 
	 * @param inputNeurons
	 * @param hiddenNeurons
	 * @param outputNeurons
	 */
	private void createFeedForwardNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons) {
		this.network = new BasicNetwork();
		network.addLayer(new BasicLayer(inputNeurons));
		network.addLayer(new BasicLayer(hiddenNeurons));
		network.addLayer(new BasicLayer(outputNeurons));
		network.getStructure().finalizeStructure();
		network.reset();
	 
//	    MLDataSet dataSet = new MLDataSet(XOR_INPUT, XOR_IDEAL);
	    // train to 1%
//	    EncogUtility.trainToError(method, dataSet, 0.01);
	    // evaluate
//	    EncogUtility.evaluate(method, dataSet);

	}

	private void createElmanNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons) {

	}

	private void createJordanNetwork(int inputNeurons, int hiddenNeurons, int outputNeurons) {

	}

	public BasicNetwork getNetwork() {
		return this.network;
	}

	/**
	 * Run Prediction for a whole MLDataSet
	 * 
	 * @param testingSet
	 */
	public void runPrediction(MLDataSet testingSet) {
	}

	public void shutdown() {
		Encog.getInstance().shutdown();
	}

	public void trainNetwork(MLDataSet validationSet, MLDataSet trainingSet) {
		TrainingComponent.learnTrainingSet(this.network, validationSet, trainingSet);
	}
	
	@Override
	public void step() {
		// TODO Auto-generated method stub
		
	}
}