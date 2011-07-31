package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Text extends Shape {

	private final String	text;
	private final Point2D	pos;

	Text(Point2D p, String s) {
		super(p, null);
		pos = p;
		this.text = s;
	}

	@Override
	public void render(GL2 gl, GLUT glut) {
		gl.glColor3d(color.red, color.green, color.blue);
		gl.glRasterPos2d(pos.getX(), pos.getY());
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, this.text);
	}

	@Override
	public List<Point2D> points() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ShapeKind getKind() {
		// TODO Auto-generated method stub
		return null;
	}

}
