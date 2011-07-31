package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.opengl.GL;

/**
 * Esta classe representa um circulo, e tem funcionalidades para c�lculos referentes ao c�rculo
 * 
 * @author jeank
 * 
 */
public class Circle extends Shape {

	private final Point2D	center;
	private Point2D			border;
	private double			radius;

	private List<Point2D>	points		= new ArrayList<Point2D>();

	// precis�o de 36 pontos
	private final int		precision	= 36;

	Circle(Point2D p, Point2D p2) {
		super(p, p2);
		drawMode = GL.GL_LINE_LOOP;
		// pontos de manipula��o
		center = p;
		border = p2;
		radius = Double.NaN;
	}

	public Circle(Point2D p, double radius) {
		this(p, Utils._2d(p.getX(), p.getY() + radius));
	}

	public void setRadius(double radius) {
		this.radius = radius;
		this.border = Utils._2d(center.getX(), center.getY() + radius);
	}

	/**
	 * @return O raio deste circulo
	 */
	public double getRadius() {
		return radius;
	}

	private void buildPoints() {
		if (points.size() == 0) {
			for (int i = 0; i < precision; i++) {
				points.add(Utils._2d(0, 0));
			}
			// uma vez construido, n�o pode mais ser alterado
			points = Collections.unmodifiableList(points);
			// se mudou o raio recalcula
		}
		double dist = Utils.distance(center, border);
		if (dist != radius) {
			radius = dist;

			// this.translate(this.center);
			// this.center.setLocation(x, y);

			// Faz a constru��o dos pontos do c�rculo
			double delta = Utils._360 / precision;
			double theta = 0;

			for (Point2D p : points) {
				// n�o soma o x e y do centro, pois isso vai ser a transla��o da figura, ent�o s� considera apenas o raio, e no desenho faz a trasla��o
				double x = (Math.cos(theta) * radius) + center.getX();
				double y = (Math.sin(theta) * radius) + center.getY();
				p.setLocation(x, y);

				theta += delta;
			}
		}
	}

	@Override
	public List<Point2D> points() {
		buildPoints();
		return points;
	}

	@Override
	protected ShapeKind getKind() {
		return ShapeKind.CIRCLE;
	}

}
