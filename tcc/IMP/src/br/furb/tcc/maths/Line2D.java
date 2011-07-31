package br.furb.tcc.maths;

import java.awt.geom.Point2D;

import br.furb.tcc.sound.utils.Utils;

public class Line2D {

	private final Point2D	point;
	private final double	angularCoeficient;

	public Line2D(Point2D point, double angle) {
		this.point = point;
		this.angularCoeficient = Math.tan(angle);
	}

	public double getAngularCoeficient() {
		return angularCoeficient;
	}

	public double getLinearCoeficient() {
		return -(angularCoeficient * point.getX()) + point.getY();
	}

	public Point2D intercept(Line2D line) {
		// são paralelas
		if (Utils.doubleEq(angularCoeficient, line.angularCoeficient)) {
			return new Point2D.Double(Double.NaN, Double.NaN);
		}

		/*
		 * x = (b2-b1) / (a1-a2)
		 */
		double x = (line.getLinearCoeficient() - getLinearCoeficient()) / (getAngularCoeficient() - line.getAngularCoeficient());
		/*
		 * y = ax + b
		 */
		double y = getAngularCoeficient() * x + getLinearCoeficient();
		return new Point2D.Double(x, y);
	}

}
