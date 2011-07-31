package br.furb.tcc.test;

import br.furb.tcc.maths.SlowCrossCorrelation;



public class SlowCorrelationTest {

	public static void main(String[] args) {

		double[] a = new double[] { 1, 2, 3, 4, 5, 6, 7 };
		double[] b = new double[] { 4, 5, 6, 7, 1, 1, 1 };

		System.out.println(SlowCrossCorrelation.run(a, b));
		System.out.println(SlowCrossCorrelation.run(b, a));

	}
}