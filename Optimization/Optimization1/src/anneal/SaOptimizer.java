package anneal;

import net.sourceforge.jannealer.AnnealingScheme;
import net.sourceforge.jannealer.ObjectiveFunction;

public class SaOptimizer {

	public static double[] Optimize(ObjectiveFunction function, int iterations) {
		double[] results = new double[10];
		double[] solution = {0.0, 0.0};

		for(int i = 0; i < 10; i++) {
			AnnealingScheme scheme = new AnnealingScheme();
			scheme.setFunction(function);
			scheme.setTemperature(1000.0);
			scheme.setCoolingRate(50.0);
			scheme.setIterations(iterations);
			scheme.anneal();
			solution = scheme.getSolution();
			results[i] = function.distance(solution);
		}
		
		double sum = 0;
		for (double number : results) {
	        sum += number;
	    }
	    double average =  sum / results.length;

		System.out.println("Optimized " + function.toString());
		System.out.println("Best fitness: " + function.distance(solution));
		System.out.println("Best position: 	[" + solution[0] + ", " + solution[1] + "]");
		System.out.println("Number of evaluations: " + iterations);
		System.out.println("ø: " + average);
		System.out.println();
		
		return solution;
	}
}
	