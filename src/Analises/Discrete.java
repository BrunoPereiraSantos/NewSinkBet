package Analises;
/*************************************************************************
 * Compilation: javac Discrete.java Execution: java Discrete N
 * 
 * Implementation of algorithm for sampling from a discrete probability N-vector
 * X[1], X[2], ..., X[N]. Runs nn O(1) worst case time per sample, using one
 * integer array and one double array of size N for storage. Preprocessing
 * consumes O(N) time and temporarily uses one additional integer array of size
 * N for bookkeeping.
 * 
 * This code is a Java version of Warren D. Smith's WDSsampler.c
 * 
 * This method was originally developed by Walker and improved by Kronmal and
 * Peterson (1979).
 * 
 *************************************************************************/

public class Discrete {
	
	private double[] distribution;
	double sum = 0.0;
	Discrete d;
	
	public Discrete getDiscretePoissonOneToN(int N, double lambda) {
		N = N + 2;
		distribution = new double[N];
		sum = 0.0;
		for(int i = 1; i < N-1; i ++){
			distribution[i] = poisson(lambda, i);
			sum += distribution[i];
		}
		
		for(int i = 1; i < N-1; i ++)
			distribution[i] = distribution[i]/sum;
		
		return new Discrete(distribution);
	}
	
	private double[] Y;
	private int[] A;
	private int N;

	// Walker's sampling algorithm
	// choose i between 1 and N uniformly at random
	// return i with prob Y[i]; otherwise return i
	public int random() {
		// i = random uniform integer from { 1, 2, ..., N }
		int i = 1 + (int) (N * Math.random());
		double r = Math.random();
		if (r > Y[i])
			i = A[i];
		return i;
	}

	public Discrete(double[] X) {
		N = X.length - 2;
		Y = new double[N + 2];
		for (int i = 1; i <= N; i++)
			Y[i] = X[i];
		A = new int[N + 2];
		int[] B = new int[N + 2]; // for bookkeeping
		for (int i = 1; i <= N; i++) {
			A[i] = B[i] = i; // initial destins = stay there
			Y[i] = Y[i] * N; // scale probability vector
		}

		// sentinels
		B[0] = 0;
		B[N + 1] = N + 1;
		Y[0] = 0.0;
		Y[N + 1] = 2.0;

		int i = 0;
		int j = N + 1;
		while (true) {
			do {
				i++;
			} while (Y[B[i]] < 1.0); // find i so X[B[i]] needs more
			do {
				j--;
			} while (Y[B[j]] >= 1.0); // find j so X[B[j]] wants less
			if (i >= j)
				break;
			int k = B[i];
			B[i] = B[j];
			B[j] = k; // swap B[i] and B[j]
		}
		i = j;
		j++;
		while (i > 0) {
			while (Y[B[j]] <= 1.0) {
				j++;
			} // find j so X[B[j]] needs more
			if (j > N)
				break;
			Y[B[j]] -= 1.0 - Y[B[i]]; // B[i] will donate to B[j] to fix up
			A[B[i]] = B[j];
			if (Y[B[j]] < 1.0) { // X[B[j]] now wants less so readjust ordering
				int k = B[i];
				B[i] = B[j];
				B[j] = k; // swap B[j] and B[i]
				j++;
			} else {
				i--;
			}
		}
	}

	public static double poisson(double lambda, int k) {
		return (Math.pow(Math.E, -lambda) * Math.pow(lambda, k)) / fatorial(k);
	}

	public static int fatorial(int n) {
		int fact = 1;
		for (int c = 1; c <= n; c++)
			fact = fact * c;
		return fact;
	}

	
	
	public static void main(String[] args) {
		
		int N = 12;
		double[] X = new double[N];
		double sum = 0.0;
		
		for(int i = 1; i < N-1; i ++){
			X[i] = poisson(10, i);
			sum += X[i];
			System.out.println(i + " "+ X[i]);
		}
		
		for(int i = 1; i < N-1; i ++){
			X[i] = X[i]/sum;
		}
		
		System.out.println("Normalizado");
		sum = 0;
		for(int i = 1; i < N-1; i++){
			System.out.println(i + " "+ X[i]);
			sum +=  X[i];
		}
		System.out.println("Normalizado "+sum);
		
		Discrete d = new Discrete(X);
		
		for (int i = 1; i < 10; i++)
			System.out.println(i + " "+ d.random());
		
//		/*int N = Integer.parseInt(args[0]);
//		double[] X = { 0, .1, .4, .12, .38, 0 };
//		int[] hist = new int[N + 2];
//		Discrete d = new Discrete(X);
//		for (int i = 0; i < N; i++)
//			hist[d.random()]++;
//
//		for (int i = 1; i <= X.length - 2; i++)
//			System.out.println(i + ":  " + X[i] + " " + (1.0 * hist[i] / N));*/

	}

}