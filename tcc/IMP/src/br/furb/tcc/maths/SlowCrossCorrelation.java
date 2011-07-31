package br.furb.tcc.maths;

public class SlowCrossCorrelation {

	/**
	 * Faz a variação do sinal data no tempo em shiftCount frames
	 */
	public static double[] shift(double[] data, int shiftCount) {
		double c[] = new double[data.length];
		for (int i = 0; i < data.length; i++) {
			if ((shiftCount + i >= 0) && (shiftCount + i < data.length)) {
				c[i] = data[shiftCount + i];
			}
		}
		return c;
	}

	/**
	 * Correlação cruzada entre os sinais a e b
	 * 
	 * @return A diferença dos sinais em frames
	 */
	public static int run(double[] a, double[] b) {
		double correlation = Double.NEGATIVE_INFINITY;
		int frame = 0;

		// Começa a percorrer de -b, o sinal b vai variar, comparando cada variação com o sinal a
		for (int i = -b.length + 1; i < b.length; i++) {
			double slidingY[] = shift(b, i);
			double res = dot(a, slidingY);
			if (res > correlation) {
				frame = i;
				correlation = res;
			}
		}

		return frame;
	}

	private static double dot(final double[] data, final double[] slide) {
		double sum = 0;
		for (int i = 0; i < Math.min(data.length, slide.length); i++) {
			sum += slide[i] * data[i];
		}
		return sum;
	}
}
