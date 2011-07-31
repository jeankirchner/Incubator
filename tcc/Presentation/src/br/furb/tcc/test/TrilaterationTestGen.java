package br.furb.tcc.test;

import java.awt.geom.Point2D;
import java.util.Random;

import br.furb.tcc.maths.GeneralTrilaterationModel;
import br.furb.tcc.maths.Location;
import br.furb.tcc.maths.Trilateration;
import br.furb.tcc.maths.TrilaterationModelUtils;
import br.furb.tcc.sound.utils.Utils;

public class TrilaterationTestGen {

	private static Random	r	= new Random();

	public static TestModel genTest(double xMax, double yMax) {
		double x = (r.nextDouble() * Integer.MAX_VALUE) % xMax;
		double y = (r.nextDouble() * Integer.MAX_VALUE) % yMax;
		TestModel test = new TestModel(new Point2D.Double(x, y));
		test.testTrilateration();
		return test;
	}

	public static class TestModel {

		private final GeneralTrilaterationModel	model;
		private final Point2D					soundSource;
		private Trilateration					test;
		private Location						result;
		private double							dst;
		private double							direction;
		private Location						realSource;

		public TestModel(Point2D soundSource) {
			this.model = TrilaterationModelUtils.create2DModel(0, 0, 500);
			this.soundSource = soundSource;
		}

		public double getITD_R1_R2() {
			return (model.getReceptor1().distance(soundSource) - model.getReceptor2().distance(soundSource)) / Utils.speedOfSoundCmBySeconds();
		}

		public double getITD_R2_R3() {
			return (model.getReceptor2().distance(soundSource) - model.getReceptor3().distance(soundSource)) / Utils.speedOfSoundCmBySeconds();
		}

		public double getITD_R1_R3() {
			return (model.getReceptor1().distance(soundSource) - model.getReceptor3().distance(soundSource)) / Utils.speedOfSoundCmBySeconds();
		}

		public void testTrilateration() {
			this.test = new Trilateration(model);
			this.result = test.run(getITD_R1_R2(), getITD_R2_R3(), getITD_R1_R3());
			dst = soundSource.distance(model.getCenter());
			direction = Math.toDegrees(Math.asin((soundSource.getY() - model.getCenter().getY()) / dst));
			realSource = new Location(soundSource, direction, dst);
		}

		public GeneralTrilaterationModel getModel() {
			return model;
		}

		public Point2D getSoundSource() {
			return soundSource;
		}

		public Trilateration getTest() {
			return test;
		}

		public Location getResult() {
			return result;
		}

		public double getDst() {
			return dst;
		}

		public double getDirection() {
			return direction;
		}

		public Location getRealSource() {
			return realSource;
		}

	}

}
