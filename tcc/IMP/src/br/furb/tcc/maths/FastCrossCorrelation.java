package br.furb.tcc.maths;

/**
 * Created by IntelliJ IDEA. User: lyondo Date: Nov 28, 2005 Time: 5:34:46 PM To change this template use File | Settings | File Templates.
 */
public class FastCrossCorrelation {

	public static int run(double[] x, double[] y) {
		ComplexDouble1d a = new ComplexDouble1d(x);
		ComplexDouble1d b = new ComplexDouble1d(y);
		
		FFT f1 = new FFT();
		FFT f2 = new FFT();
		
		f1.forwardFFT(a.getRe(), a.getIm());
		f2.forwardFFT(b.getRe(), b.getIm());
		
		a.conjugate();
		a.mult(b);
		
		f1.reverseFFT(a.getRe(), a.getIm());

		return a.getReMaxIndex();
	}

}
