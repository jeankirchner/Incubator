package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;

import javax.media.opengl.GL;

public class FreeClosedShape extends FreeShape {

	public FreeClosedShape(Point2D p, Point2D p2) {
		super(p, p2);
		drawMode = GL.GL_LINE_LOOP;
	}

}
