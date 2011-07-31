package br.furb.tcc.maths;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import br.furb.tcc.sound.utils.Utils;

public class GeneralTrilaterationModel {

	private final Point2D	receptor1;
	private final Point2D	receptor2;
	private final Point2D	receptor3;
	private Point2D			center;
	private final double	side;

	public GeneralTrilaterationModel(Point2D receptor1, Point2D receptor2, Point2D receptor3) {
		assert (receptor1.distance(receptor2) == receptor1.distance(receptor3));
		assert (receptor1.distance(receptor2) == receptor2.distance(receptor3));

		this.receptor1 = receptor1;
		this.receptor2 = receptor2;
		this.receptor3 = receptor3;
		this.side = receptor1.distance(receptor2);
	}

	public Point2D getReceptor1() {
		return receptor1;
	}

	public Point2D getReceptor2() {
		return receptor2;
	}

	public Point2D getReceptor3() {
		return receptor3;
	}

	public double getSide() {
		return side;
	}

	public Point2D getCenter() {
		if (this.center == null) {
			this.center = Utils.averagePoint(receptor1, receptor2, receptor3);
		}
		return this.center;
	}

	List<Point2D> filterNear(List<Point2D> points) {
		List<Point2D> filtered = new ArrayList<Point2D>();
		for (Point2D p : points) {
			if (p.distance(getCenter()) / side > 1) {
				filtered.add(p);
			}
		}
		return filtered;
	}

}
