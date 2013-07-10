package anneal;

import net.sourceforge.jannealer.AnnealingScheme;
import net.sourceforge.jannealer.ObjectiveFunction;

public class SaOptimizer {

	public static double[] Optimize(ObjectiveFunction function, int iterations) {
		
		AnnealingScheme scheme = new AnnealingScheme();
		scheme.setFunction(function);
		scheme.setIterations(iterations);
		scheme.anneal();
		double[] solution;
		solution = scheme.getSolution();

		System.out.println("Optimized " + function.toString());
		System.out.println("Best fitness: " + function.distance(solution));
		System.out.println("Best position: 	[" + solution[0] + ", " + solution[1] + "]");
		System.out.println("Number of evaluations: " + iterations);
		System.out.println();
		
		return solution;
	}
}
