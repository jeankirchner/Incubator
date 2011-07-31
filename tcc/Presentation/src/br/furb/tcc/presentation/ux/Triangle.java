package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL2;

public class Triangle extends Shape {

	Triangle(Point2D p1, Point2D p2, Point2D p3) {
		super(p1, p2);
		controlPoints.add(p3);
		color = Color.BLACK;
		drawMode = GL2.GL_LINE_LOOP;
	}

	@Override
	public List<Point2D> points() {
		return controlPoints;
	}

	@Override
	protected ShapeKind getKind() {
		return ShapeKind.TRIANGLE;
	}

}
