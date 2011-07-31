package br.furb.tcc.sound.utils;

import java.awt.geom.Point2D;

import br.furb.tcc.sound.format.WavFile;


public class Utils {

	public static int convertMilisToFrame(int frameCount, int frameRate, int milis) {
		double secs = milis / (double) 1000;
		double frameSecs = frameCount / frameRate;
		double percent = secs / frameSecs;
		int frame = (int) (frameCount * percent);
		return frame;
	}

	public static WavFile newWav(WavFile prj, int from, int to) {
		WavFile _wav = new WavFile(prj.getNumChannels(), to - from, prj.getValidBits(), prj.getSampleRate());
		for (int frame = from; frame < to; frame++) {
			for (int ch = 0; ch < prj.getNumChannels(); ch++) {
				_wav.setSample(ch, frame - from, prj.getSample(ch, frame));
			}
		}
		return _wav;
	}

	public static double speedOfSoundCmBySeconds() {
		double speed = 343.2; // m por segundo
		speed *= 100; // cm por segundo
		return speed;
	}

	public static double distance(Point2D a, Point2D b) {
		return a.distance(b);
	}

	public static Point2D averagePoint(Point2D... points) {
		double avgX = 0;
		double avgY = 0;

		for (Point2D point : points) {
			avgX += point.getX();
			avgY += point.getY();
		}
		avgX /= points.length;
		avgY /= points.length;
		return new Point2D.Double(avgX, avgY);
	}

	/**
	 * Normaliza dentro de 2 PI e positivo
	 * 
	 * @param radians
	 * @return
	 */
	public static double normalizeRadians(double radians) {
		return Math.toRadians(Utils.normalizeDegrees(Math.toDegrees(radians)));
	}

	/**
	 * Normaliza dentro de 360 e positivo
	 * 
	 * @param degrees
	 * @return
	 */
	public static double normalizeDegrees(double degrees) {
		degrees %= 360;
		if (degrees < 0) {
			degrees = 360 + degrees;
		}
		return degrees;
	}

	public static boolean doubleEq(double a, double b) {
		return Math.abs(a - b) < 0.0000000001;
	}

}
