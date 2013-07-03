package model.controller;

import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.activation.ActivationSigmoid;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.util.simple.EncogUtility;

public class ANNController implements ControllerInterface {

	@Override
	public void step() {
		// TODO Auto-generated method stub
	}

	/**
	 * The input necessary for XOR.
	 */
	public static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
			{ 0.0, 1.0 }, { 1.0, 1.0 } };

	/**
	 * The ideal data necessary for XOR.
	 */
	public static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };

	public static void main(String[] args) {
		BasicNetwork method = new BasicNetwork();
		method.addLayer(new BasicLayer(null, true, 2));
		method.addLayer(new BasicLayer(new ActivationSigmoid(), true, 4));
		method.addLayer(new BasicLayer(new ActivationSigmoid(), false, 1));
		method.getStructure().finalizeStructure();
		method.reset();

		MLDataSet dataSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
		// train to 1%
		EncogUtility.trainToError(method, dataSet, 0.01);
		// evaluate
		EncogUtility.evaluate(method, dataSet);
	}
}