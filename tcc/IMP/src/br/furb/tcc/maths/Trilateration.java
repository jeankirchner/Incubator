package br.furb.tcc.maths;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.furb.tcc.sound.utils.Utils;

public class Trilateration {

	private final GeneralTrilaterationModel	model;
	private List<Line2D>					lines;
	private List<Point2D>					intersections;

	public Trilateration(GeneralTrilaterationModel model) {
		this.model = model;
	}

	public Location run(double tdoa12, double tdoa23, double tdoa13) {

		// distancia = velocidade * diferenca de tempo de chegada
		double vTdoa12 = tdoa12 * Utils.speedOfSoundCmBySeconds();
		double vTdoa23 = tdoa23 * Utils.speedOfSoundCmBySeconds();
		double vTdoa13 = tdoa13 * Utils.speedOfSoundCmBySeconds();

		Point2D mid1 = Utils.averagePoint(model.getReceptor1(), model.getReceptor2());
		double alpha = -Math.asin(vTdoa12 / Utils.distance(model.getReceptor1(), model.getReceptor2())) + Math.toRadians(30);
		double alpha1 = alpha;
		double alpha2 = (Math.toRadians(180) - alpha);

		Point2D mid2 = Utils.averagePoint(model.getReceptor2(), model.getReceptor3());
		double beta = -Math.asin(vTdoa23 / Utils.distance(model.getReceptor2(), model.getReceptor3()));
		double beta1 = Math.toRadians(90) - beta;
		double beta2 = Math.toRadians(180) - (Math.toRadians(90) - beta);

		Point2D mid3 = Utils.averagePoint(model.getReceptor1(), model.getReceptor3());
		double gama = -Math.asin(vTdoa13 / Utils.distance(model.getReceptor1(), model.getReceptor3())) - Math.toRadians(30);
		double gama1 = gama;
		double gama2 = (Math.toRadians(180) - gama);

		lines = new ArrayList<Line2D>();
		lines.add(new Line2D(mid1, alpha1));
		lines.add(new Line2D(mid1, alpha2));

		lines.add(new Line2D(mid2, beta1));
		lines.add(new Line2D(mid2, beta2));

		lines.add(new Line2D(mid3, gama1));
		lines.add(new Line2D(mid3, gama2));

		intersections = getIntersections();

		Point2D source = findSoundSource(intersections);
		double distance = model.getCenter().distance(source);
		double direction = Math.asin((source.getY() - model.getCenter().getY()) / distance);
		direction = Math.toDegrees(direction);

		return new Location(source, direction, distance);

	}

	public List<Line2D> getLines() {
		return lines;
	}

	private static Comparator<Point2D>	byX	= new Comparator<Point2D>() {
												@Override
												public int compare(Point2D o1, Point2D o2) {
													return (int) (o1.getX() - o2.getX());
												}
											};

	/**
	 * Vai achar o triângulo de menor área, e tomar como fonte sonora
	 * 
	 * @param intersections
	 * @return
	 */
	private Point2D findSoundSource(List<Point2D> intersections) {
		intersections = model.filterNear(intersections);

		Collections.sort(intersections, byX);

		double smallestDistance = Double.POSITIVE_INFINITY;

		List<Point2D> pts = new ArrayList<Point2D>();

		for (int i = 0; i < intersections.size(); i++) {
			for (int j = i + 1; j < intersections.size(); j++) {
				for (int k = j + 1; k < intersections.size(); k++) {
					Point2D pt1 = intersections.get(i);
					Point2D pt2 = intersections.get(j);
					Point2D pt3 = intersections.get(k);

					double dst = distances(pt1, pt2, pt3);

					if (dst < smallestDistance) {
						smallestDistance = dst;
						pts.clear();
						pts.add(pt1);
						pts.add(pt2);
						pts.add(pt3);
					}
				}
			}
		}

		return Utils.averagePoint(pts.toArray(new Point2D[0]));
	}

	private double distances(Point2D pt1, Point2D pt2, Point2D pt3) {
		double dst = pt1.distance(pt2);
		dst += pt2.distance(pt3);
		dst += pt3.distance(pt1);
		return dst;
	}

	public List<Point2D> getIntersections() {
		List<Point2D> intersections = new ArrayList<Point2D>();
		for (int i = 0; i < lines.size(); i++) {
			for (int j = i + 1; j < lines.size(); j++) {
				intersections.add(lines.get(i).intercept(lines.get(j)));
			}
		}
		return intersections;
	}

}