package br.furb.tcc.maths;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

import br.furb.tcc.sound.utils.Utils;

public class Location {
	private final Point2D	source;
	private final double	direction;
	private final double	distance;

	public Location(Point2D source, double direction, double distance) {
		this.source = source;
		this.direction = Utils.normalizeDegrees(direction);
		this.distance = distance;
	}

	public double getDirection() {
		return direction;
	}

	public double getDistance() {
		return distance;
	}

	public Point2D getSource() {
		return source;
	}

	private String getDoubleString(double a) {
		if (Double.isInfinite(a)) {
			return "Infinite";
		}
		if (Double.isNaN(a)) {
			return "NaN";
		}
		return new BigDecimal(a).toString();
	}

	private String getDirectionString() {
		return getDoubleString(direction);
	}

	private String getDistanceString() {
		return getDoubleString(distance);
	}

	private String getSourceXString() {
		return getDoubleString(source.getX());
	}

	private String getSourceYString() {
		return getDoubleString(source.getY());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Source: ( ").append(getSourceXString()).append(" , ").append(getSourceYString()).append(" )");
		sb.append('\n');
		sb.append("Direction: ").append(getDirectionString());
		sb.append('\n');
		sb.append("Distance: ").append(getDistanceString()).append(" cm");
		return sb.toString();
	};

}
