package br.furb.tcc.maths;

/*
 * Copyright (c) 2005 DocJava, Inc. All Rights Reserved.
 */

/**
 * User: lyon Date: Nov 13, 2005 Time: 8:00:08 AM Copyright DocJava, Inc. 2005.
 */
public class ComplexDouble1d {
	public double	re[];
	public double	im[];

	public ComplexDouble1d() {
	}

	/**
	 * Copy the s input into the real number plane and set the imaginary plane to zero.
	 * 
	 * @param s
	 */
	public ComplexDouble1d(double s[]) {
		int n = s.length;
		re = s;
		im = new double[n];
		for (int x = 0; x < n; x++) {
			re[x] = s[x];
		}
	}

	public short[][] getRealAs2dShort(int w, int h) {
		short s[][] = new short[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				s[x][y] = (short) re[x + y * w];
			}
		}
		return s;
	}

	public ComplexDouble1d(int n) {
		re = new double[n];
		im = new double[n];
	}

	// swap Zi with Zj
	public void swapInt(int i, int j) {
		double tempr;
		int ti;
		int tj;
		ti = i - 1;
		tj = j - 1;
		// System.out.print("swapInt " + ti+","+tj+"\t");
		// System.out.println(Integer.toBinaryString(ti)+","+Integer.toBinaryString(tj));
		// System.out.println(Integer.toBinaryString(ti)+"bitr="+Integer.toBinaryString(bitr(ti)));
		tempr = re[tj];
		re[tj] = re[ti];
		re[ti] = tempr;
		tempr = im[tj];
		im[tj] = im[ti];
		im[ti] = tempr;
	}

	public ComplexDouble1d(double[] realPart, double[] imaginaryPart) {
		re = realPart;
		im = imaginaryPart;
	}

	// a = a + b, a=a.plus(b)
	public void plus(ComplexDouble1d b) {
		for (int i = 0; i < re.length; i++) {
			re[i] += b.getRe(i);
		}
		for (int i = 0; i < re.length; i++) {
			im[i] += b.getIm(i);
		}
	}

	/**
	 * Mask the image kernal with the real and imaginary numbers. Assume the image kernal ranges from 0..255. Make it a unitary filter by dividing by 255.
	 * 
	 * @param r
	 */
	public void scaleAndMask(short r[][]) {
		int w = r.length;
		int h = r[0].length;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int i = x + y * w;
				re[i] *= r[x][y] / 255f;
				im[i] *= r[x][y] / 255f;
			}
		}
	}

	public void scale(double sc) {
		for (int i = 0; i < re.length; i++) {
			re[i] = re[i] * sc;
		}
		for (int i = 0; i < re.length; i++) {
			im[i] = im[i] * sc;
		}
	}

	// todo test the below code...
	// a = a * b, a=a.times(b);
	// (x1 + iy1)(x2 + iy2) =
	// x1 * x2 - y1 * y2 +
	// i(y1 * x2 + x1 * y2)
	public void mult(ComplexDouble1d b) {
		double realT;
		for (int i = 0; i < re.length; i++) {

			double x1 = re[i];
			double x2 = b.getRe(i);
			double y1 = im[i];
			double y2 = b.getIm(i);
			realT = x1 * x2 - y1 * y2;
			im[i] = y1 * x2 + x1 * y2;
			re[i] = realT;
		}
	}

	// a = a - b, a=a.minus(b)
	public void minus(ComplexDouble1d b) {
		for (int i = 0; i < re.length; i++) {
			re[i] -= b.getRe(i);
		}
		for (int i = 0; i < re.length; i++) {
			im[i] -= b.getIm(i);
		}
	}

	public double[] logScaleMagnitude(double magScale) {
		int N = re.length;
		double result[] = new double[N];
		for (int i = 0; i < N; i++) {
			result[i] = (magScale * Math.log(1 + Math.sqrt(re[i] * re[i] + im[i] * im[i])));
			if (result[i] > 255) {
				result[i] = 255;
			}
		}
		return result;
	} // End of function magnitude().

	public double[] getIm() {
		return im;
	}

	public double getIm(int i) {
		return im[i];
	}

	public void setIm(double[] im) {
		this.im = im;
	}

	public double getRe(int i) {
		return re[i];
	}

	public double[] getRe() {
		return re;
	}

	public int getLength() {
		return re.length;
	}

	public void setRe(double[] re) {
		this.re = re;
	}

	public void swap(int i, int j) {
		double tempr = re[j];
		re[j] = re[i];
		re[i] = tempr;
		tempr = im[j];
		im[j] = im[i];
		im[i] = tempr;
	}

	public double[] getPSD() {
		return getPSD(re, im);
	}

	/**
	 * Compute the power spectral density of the input arrays
	 * 
	 * @param in_r
	 *            real part of an fft
	 * @param in_i
	 *            imaginary part of an fft
	 * @return the psd.
	 */
	public static double[] getPSD(double in_r[], double in_i[]) {
		int N = in_r.length;
		double[] mag = new double[N];
		for (int i = 0; i < in_r.length; i++) {
			mag[i] = Math.sqrt(in_r[i] * in_r[i] + in_i[i] * in_i[i]);
		}
		return mag;
	}

	public void center() {
		double alternateSign;
		int n = re.length;
		for (int i = 0; i < n; i++) {

			// Calculate (-1)**(i)
			alternateSign = (i % 2 == 0) ? -1 : 1;
			// 1.
			// (-1)**(i) is to put the zero frequency in the
			// center of the image when it is displayed.
			re[i] = (re[i] * alternateSign);
			im[i] = (im[i] * alternateSign);
		}
	}

	public void setRe(short re[]) {
		this.re = new double[re.length];
		for (int i = 0; i < re.length; i++) {
			this.re[i] = re[i];
		}

	}

	public void print() {
		System.out.println("re[i],im[i]");
		for (int i = 0; i < re.length; i++) {
			System.out.println(re[i] + "," + im[i]);
		}
	}

	public void normalize() {
		scale(1f / re.length);
	}

	public void conjugate() {
		for (int i = 0; i < im.length; i++) {
			im[i] = -im[i];
		}
	}

	public int getReMaxIndex() {
		double max = Double.NEGATIVE_INFINITY;
		int maxIndex = -1;
		for (int i = 0; i < re.length; i++) {
			if (max < re[i]) {
				max = re[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

}
