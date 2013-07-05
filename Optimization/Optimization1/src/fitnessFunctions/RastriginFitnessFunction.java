package fitnessFunctions;

import net.sourceforge.jannealer.ObjectiveFunction;
import net.sourceforge.jswarm_pso.FitnessFunction;

public class RastriginFitnessFunction extends FitnessFunction implements ObjectiveFunction{
	public double evaluate(double position[]) { 
		return -FitnessFunctions.rastrigin(position[0], position[1]);
	}
	
	@Override
	public String toString() {
		return "Rastrigin";
	}

	@Override
	public double distance(double[] position) {
		return evaluate(position);
	}

	@Override
	public int getNdim() {
		return 2;
	}
}
