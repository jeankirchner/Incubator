package br.furb.tcc.maths;

public class FFT {

	private double				r_data[]	= null;
	private double				i_data[]	= null;

	// if forward = true then perform a forward fft
	// else perform a backward fft;
	private boolean				forward		= true;
	private int					nu;									// number of bits needed to represent the index for r_data
	private static final float	twoPI		= (float) (2 * Math.PI);

	public FFT(int N) {
		r_data = new double[N];
		i_data = new double[N];
	}

	public FFT() {
	}

	// swap Zi with Zj
	private void swapInt(int i, int j) {
		double tempr;
		int ti;
		int tj;
		ti = i - 1;
		tj = j - 1;
		tempr = r_data[tj];
		r_data[tj] = r_data[ti];
		r_data[ti] = tempr;
		tempr = i_data[tj];
		i_data[tj] = i_data[ti];
		i_data[ti] = tempr;
	}

	public static double getMaxValue(double in[]) {
		double max;
		max = -0.99e30;
		for (double element : in) {
			if (element > max) {
				max = element;
			}
		}
		return max;

	}

	private void bitReverse() {
		/* bit reversal */
		int n = r_data.length;
		int j = 1;

		int k;

		for (int i = 1; i < n; i++) {

			if (i < j) {
				swapInt(i, j);
			}
			k = n / 2;
			while (k >= 1 && k < j) {

				j = j - k;
				k = k / 2;
			}
			j = j + k;
		} // for
	}

	public void reverseFFT(double in_r[], double in_i[]) {
		forward = false;
		forwardFFT(in_r, in_i);
		forward = true;
		// centering(in_r);
	}

	public void forwardFFT(double in_r[], double in_i[]) {
		int id;

		int localN;
		double wtemp, Wjk_r, Wjk_i, Wj_r, Wj_i;
		double theta, tempr, tempi;

		int numBits = log2(in_r.length);
		if (forward) {
			// centering(in_r);
		}

		// Truncate input data to a power of two
		int length = 1 << numBits; // length = 2**nu
		int n = length;
		int nby2;

		// Copy passed references to variables to be used within
		// fft routines & utilities
		r_data = in_r;
		i_data = in_i;

		bitReverse();
		for (int m = 1; m <= numBits; m++) {
			// localN = 2^m;
			localN = 1 << m;

			nby2 = localN / 2;
			Wjk_r = 1;
			Wjk_i = 0;

			theta = Math.PI / nby2;

			// for recursive comptutation of sine and cosine
			Wj_r = Math.cos(theta);
			Wj_i = -Math.sin(theta);
			if (forward == false) {
				Wj_i = -Wj_i;
			}

			for (int j = 0; j < nby2; j++) {
				// This is the FFT innermost loop
				// Any optimizations that can be made here will yield
				// great rewards.
				for (int k = j; k < n; k += localN) {
					id = k + nby2;
					tempr = Wjk_r * r_data[id] - Wjk_i * i_data[id];
					tempi = Wjk_r * i_data[id] + Wjk_i * r_data[id];

					// Zid = Zi -C
					r_data[id] = r_data[k] - tempr;
					i_data[id] = i_data[k] - tempi;
					r_data[k] += tempr;
					i_data[k] += tempi;
				}

				// (eq 6.23) and (eq 6.24)

				wtemp = Wjk_r;

				Wjk_r = Wj_r * Wjk_r - Wj_i * Wjk_i;
				Wjk_i = Wj_r * Wjk_i + Wj_i * wtemp;
			}
		}

	}

	public static int log2(int n) {
		return (int) (Math.log(n) / Math.log(2.0));
	}

	public static double[] arrayCopy(double[] in) {
		int N = in.length;
		double out[] = new double[N];
		for (int i = 0; i < N; i++) {
			out[i] = in[i];
		}
		return out;
	}

	public double[] getReal() {
		return r_data;
	}

	public double[] getImaginary() {
		return i_data;
	}

	// assume that r_data and i_data are
	// set. Also assume that the real
	// value is to be returned
	public double[] ifft() {
		int i, m, j, id;
		int N; // the radix 2 number of samples
		double wtemp, wr, wpr, wpi, wi, theta, tempr, tempi;

		// length is the number of input samples
		int length = r_data.length;

		// how many bits do we need?
		nu = (int) (Math.log(length) / Math.log(2.0));
		// Truncate input data to a power of two
		length = 1 << nu; // length = 2**nu

		int n = length;

		for (m = 1; m <= nu; m++) {

			// k = 2^m;
			N = 1 << m;
			theta = twoPI / N;
			// theta = - 2Pi/(2^m)

			wr = 1.0;
			wi = 0.0;
			wpr = Math.cos(theta);

			// ifft uses - sin(theta);
			wpi = -Math.sin(theta);

			for (j = 1; j <= N / 2; j++) {
				for (i = j; i <= n; i = i + N) {

					id = i + N / 2;
					tempr = wr * r_data[id - 1] - wi * i_data[id - 1];
					tempi = wr * i_data[id - 1] + wi * r_data[id - 1];

					// Zid-1 = Zi-1 - C(tempr,tempi)
					r_data[id - 1] = r_data[i - 1] - tempr;
					i_data[id - 1] = i_data[i - 1] - tempi;
					r_data[i - 1] += tempr;
					i_data[i - 1] += tempi;
				}
				wtemp = wr;
				// W = W * WP
				// W = (wr + i wi) (wpr + i wpi)
				// W = wr * wpr - wi * wpi + i (wi * wpr + wr * wpi)
				wr = wr * wpr - wi * wpi;
				wi = wi * wpr + wtemp * wpi;
			}
		}
		return (arrayCopy(r_data));
	}

}
