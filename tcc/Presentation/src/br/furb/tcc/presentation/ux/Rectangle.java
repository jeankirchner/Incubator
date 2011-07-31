package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL;

/**
 * Esta classe representa um ret�ngulo, e tem funcionalidades para c�lculos referentes ao ret�ngulo
 * 
 * @author jeank
 * 
 */
public class Rectangle extends Shape {

	private Point2D	tl	= null;
	private Point2D	tr	= null;
	private Point2D	bl	= null;
	private Point2D	br	= null;

	public Rectangle(Point2D p, Point2D p2) {
		super(p, p2);
		drawMode = GL.GL_LINE_LOOP;
		tl = p;
		br = p2;
	}

	private double left() {
		return tl.getX();
	}

	private double right() {
		return br.getX();
	}

	private double top() {
		return tl.getY();
	}

	private double bottom() {
		return bl.getY();
	}

	private void buildPoints() {
		if (controlPoints.size() == 4) {
			tr.setLocation(br.getX(), tl.getY());
			bl.setLocation(tl.getX(), br.getY());
		} else {
			tr = Utils._2d(br.getX(), tl.getY());
			bl = Utils._2d(tl.getX(), br.getY());
			controlPoints.clear();
			// adiciona os pontos na ordem certa
			controlPoints.add(tl);
			controlPoints.add(tr);
			controlPoints.add(br);
			controlPoints.add(bl);
		}
	}

	@Override
	public List<Point2D> points() {
		buildPoints();
		return controlPoints;
	}

	@Override
	protected ShapeKind getKind() {
		return ShapeKind.RECTANGLE;
	}

}