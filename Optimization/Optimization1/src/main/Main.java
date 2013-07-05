package main;

import pso.PsoOptimizer;
import anneal.SaOptimizer;
import fitnessFunctions.FitnessFunctions;
import fitnessFunctions.RastriginFitnessFunction;
import fitnessFunctions.SchwefelFitnessFunction;
import fitnessFunctions.ShekelFitnessFunction;

public class Main {

	public static void main(String[] args) {
		int iterations = 1000;
		System.out.println("\n\nI Particle Swarm Optimization\n");
		PsoOptimizer.Optimize(new RastriginFitnessFunction(),iterations);
		PsoOptimizer.Optimize(new SchwefelFitnessFunction(),iterations);
		PsoOptimizer.Optimize(new ShekelFitnessFunction(),iterations);
		
		System.out.println("\n\nII Simulated Annealing\n");
		SaOptimizer.Optimize(new RastriginFitnessFunction(),iterations);
		SaOptimizer.Optimize(new SchwefelFitnessFunction(),iterations);
		SaOptimizer.Optimize(new ShekelFitnessFunction(),iterations);
		
		
		System.out.println("\n\nErwartete Ergebnisse");
		
		System.out.println("\n\nRastrigin-Funktion: Erwartetes Minimum bei x(i) =: 0	  => f(x) =0"+
				" - Aus Rastrigin-Funktion: " +  FitnessFunctions.rastrigin(0,0));
		
		System.out.println("\n\nSchwefel-Funktion: Erwartetes Minimum bei x(i) =: 420.9867  => -2* 418.9829  = f(x) = "+ (-2*418.9829) +
				"  - Aus Schwefel-Funktion: " +  FitnessFunctions.schwefel(420.9687,420.9687));
	
		System.out.println("\n\nShekel-Funktion: Erwartetes Minimum bei  x1 = 4, x2 = 4 : f(x) =" +FitnessFunctions.shekel(4,4) );
	}
}
