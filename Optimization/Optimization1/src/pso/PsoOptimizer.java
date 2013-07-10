package pso;

import net.sourceforge.jannealer.AnnealingScheme;
import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Swarm;

public class PsoOptimizer {

	public static double[] Optimize(FitnessFunction function, int iter) {
		double[] results = new double[100];
		Swarm swarm = null;

		for (int j = 0; j < 100; j++) {

			swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES,
					new MyParticle(), function);
			// Set position (and velocity) constraints.
			// i.e.: where to look for solutions
			swarm.setMaxPosition(500);
			swarm.setMinPosition(-500);
			// Optimize a few times
			for (int i = 0; i < iter; i++)
				swarm.evolve();
			results[j] = swarm.getBestFitness();
		}

		double sum = 0;
		for (double number : results) {
			sum += number;
		}
		double average = sum / results.length;
		
		sum = 0;
		for ( int i = 0; i < 100; i++) {
			sum += ((average - results [ i ]) * ( average - results [ i ]));
		}
		double standardabw = Math.sqrt(sum / (100 - 1.0));

		// Print results
		System.out.println("Optimized " + function.toString());
		System.out.println("ø: " + average);
		System.out.println("stdabw: " + standardabw);
		System.out.println(swarm.toStringStats());
		return swarm.getBestParticle().getBestPosition();
	}
}
