package fitnessFunctions;


public class FitnessFunctions {

	public static double rastrigin(double x, double y){
		int a = 10;
		return (2*a + (x*x - a*Math.cos(2*Math.PI*x)) + (y*y - a*Math.cos(2*Math.PI*y)));
	}
	
	public static double schwefel(double x, double y){
		return -x*Math.sin(Math.sqrt(Math.abs(x))) + -y*Math.sin(Math.sqrt(Math.abs(y)));	
	}
	
	//4 Maxima, 2 Dimensionen
	public static double shekel(double x1, double x2 ){
		return shekel(x1, x2, 4);
	}
	
	//Variable Maxima, 2 Dimensionen
	private static double shekel(double x1, double x2 , int max){
	
		double[] c = new double[] {
			0.1,0.2,0.2,0.4,0.4,0.6,0.3,0.7,0.5,0.5};
		
		if(max > c.length){
			System.out.println("zu viele Maxima");
		}
		double[][] a = new double[][]
				{   {4,4,4,4},
					{1,1,1,1},
					{8,8,8,8},
					{6,6,6,6},
					{3,7,3,7},
					{2,9,2,9},
					{5,5,3,3},
					{8,1,8,1},
					{6,2,6,2},
					{7,3.6,7,3.6},
				};
		double[] x = new double[] {x1,x2};
		double result = 0;
		for(int m = 0; m < max; m++ ){
			double temp = 0;
			for(int n = 0; n < 2; n++) temp += (x[n] - a[m][n]) * (x[n] - a[m][n]);
				
			result +=  1/ (c[m] + temp);
		}
		
		return -result;
			
	}

}
