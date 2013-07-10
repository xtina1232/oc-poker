package evaluation;

import net.sourceforge.jannealer.AnnealingScheme;
import net.sourceforge.jannealer.ObjectiveFunction;
import pso.PsoOptimizer;
import anneal.SaOptimizer;
import fitnessFunctions.FitnessFunctions;
import fitnessFunctions.RastriginFitnessFunction;
import fitnessFunctions.SchwefelFitnessFunction;
import fitnessFunctions.ShekelFitnessFunction;

public class Evaluator {

	public static void eval()
	{
		Test();
		evalPso();
		evalSa();
		expectedResults();		
	}
	
	
	
	private static void evalPso() {
		int iterations = 1000;

		System.out.println("\n\nI Particle Swarm Optimization\n");
		PsoOptimizer.Optimize(new RastriginFitnessFunction(), iterations);
		PsoOptimizer.Optimize(new SchwefelFitnessFunction(), iterations);
		PsoOptimizer.Optimize(new ShekelFitnessFunction(), iterations);
	}
	
	private static void evalSa() {
		int iterations = 1000;

		System.out.println("\n\nII Simulated Annealing\n");
		SaOptimizer.Optimize(new RastriginFitnessFunction(), iterations);
		SaOptimizer.Optimize(new SchwefelFitnessFunction(), iterations);
		SaOptimizer.Optimize(new ShekelFitnessFunction(), iterations);
	}
	
	private static void expectedResults() {
		System.out.println("\n\nErwartete Ergebnisse");

		System.out
				.println("\n\nRastrigin-Funktion: Erwartetes Minimum bei x(i) =: 0	  => f(x) =0"
						+ " - Aus Rastrigin-Funktion: "
						+ FitnessFunctions.rastrigin(0, 0));

		System.out
				.println("\n\nSchwefel-Funktion: Erwartetes Minimum bei x(i) =: 420.9867  => -2* 418.9829  = f(x) = "
						+ (-2 * 418.9829)
						+ "  - Aus Schwefel-Funktion: "
						+ FitnessFunctions.schwefel(420.9687, 420.9687));

		System.out
				.println("\n\nShekel-Funktion: Erwartetes Minimum bei  x1 = 4, x2 = 4 : f(x) ="
						+ FitnessFunctions.shekel(4, 4));
	}
	
	private static void Test() {
        AnnealingScheme scheme = new AnnealingScheme();
        
        scheme.setFunction(new ObjectiveFunction()
        {
              public int getNdim()
              {
                    return 1;
              }
              /* this function must return a number which is a measure of
               * the goodness of the proposed solution
               */ 
              public double distance(double[] vertex)
              {
            	  double x = vertex[0];
                    return 1* (x-30)*(x-30)+6;
              }
        });
       
        scheme.setIterations(1000);
        /* this is where we search for a solution */
        scheme.anneal();
       
        /* print solution */
        double[] offset;
        offset = scheme.getSolution();
        System.out.println("Solution:");
        for (int i = 0; i < offset.length; i++)
        {
              System.out.println(offset[i]);
        }

	}
}
