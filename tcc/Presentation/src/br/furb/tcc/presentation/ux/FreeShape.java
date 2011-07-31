package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL;

/**
 * Figura livre aberta, vai adicionando pontos e fazendo liga��es entre eles, sem fazer liga��o entre o primeiro e o �ltimo
 * 
 * @author jeank
 * 
 */
public class FreeShape extends Shape {

	public FreeShape(Point2D p, Point2D p2) {
		super(p, p2);
		drawMode = GL.GL_LINE_STRIP;
	}

	@Override
	public List<Point2D> points() {
		return controlPoints;
	}

	@Override
	protected ShapeKind getKind() {
		return ShapeKind.FREE_SHAPE;
	}

}
