package pso;

import net.sourceforge.jswarm_pso.FitnessFunction;
import net.sourceforge.jswarm_pso.Swarm;

public class PsoOptimizer {
	
	public static void Optimize(FitnessFunction function, int iter) {
		Swarm swarm = new Swarm(Swarm.DEFAULT_NUMBER_OF_PARTICLES,
				new MyParticle(), function);
		// Set position (and velocity) constraints.
		// i.e.: where to look for solutions
		swarm.setMaxPosition(500);
		swarm.setMinPosition(-500);
		// Optimize a few times
		for (int i = 0; i < iter; i++)
			swarm.evolve();
		// Print en results
		System.out.println("Optimized " + function.toString());
		System.out.println(swarm.toStringStats());
	}
}
