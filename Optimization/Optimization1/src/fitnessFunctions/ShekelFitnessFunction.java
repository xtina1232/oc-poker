package fitnessFunctions;

import net.sourceforge.jannealer.ObjectiveFunction;
import net.sourceforge.jswarm_pso.FitnessFunction;

public class ShekelFitnessFunction extends FitnessFunction implements ObjectiveFunction{
	public double evaluate(double position[]) { 
		return -FitnessFunctions.shekel(position[0], position[1]);
	}
	
	@Override
	public String toString() {
		return "Shekel";
	}
	
	@Override
	public double distance(double[] position) {
		return -evaluate(position);
	}

	@Override
	public int getNdim() {
		return 2;
	}
	
}
