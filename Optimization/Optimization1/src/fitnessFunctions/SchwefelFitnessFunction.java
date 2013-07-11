package fitnessFunctions;

import net.sourceforge.jannealer.ObjectiveFunction;
import net.sourceforge.jswarm_pso.FitnessFunction;

public class SchwefelFitnessFunction extends FitnessFunction implements ObjectiveFunction{
	
	// für PSO
	public double evaluate(double position[]) { 
		return -FitnessFunctions.schwefel(position[0], position[1]);
	}
	
	@Override
	public String toString() {
		return "Schwefel";
	}
	
	// für SA
	@Override
	public double distance(double[] position) {
		return evaluate(position);
	}

	@Override
	public int getNdim() {
		return 2;
	}
}
