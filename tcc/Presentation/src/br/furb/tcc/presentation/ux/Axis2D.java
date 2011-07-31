package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Axis2D extends Shape {

	Axis2D() {
		super(null, null);
	}

	@Override
	public List<Point2D> points() {
		return null;
	}

	@Override
	public void render(GL2 gl, GLUT glut) {
		gl.glColor3d(color.red, color.green, color.blue);
		xAxis(gl, glut);
		yAxis(gl, glut);
		gl.glLineWidth(1);
	}

	private void yAxis(GL2 gl, GLUT glut) {
		double tWidth = 20;
		double tHeight = 60;
		double end = 1000 - tHeight;

		Triangle t1 = new Triangle(Utils._2d(-tWidth, end), Utils._2d(tWidth, end), Utils._2d(0, end + tHeight));
		Triangle t2 = new Triangle(Utils._2d(-tWidth, -end), Utils._2d(tWidth, -end), Utils._2d(0, -end - tHeight));

		gl.glLineWidth(2);
		t1.render(gl, glut);
		t2.render(gl, glut);

		gl.glLineWidth(2);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex2d(0, -end);
		gl.glVertex2d(0, end);
		gl.glEnd();

		gl.glRasterPos2d(3 * tWidth, end);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Y");
	}

	private void xAxis(GL2 gl, GLUT glut) {
		double tWidth = 20;
		double tHeight = 60;
		double end = 1000 - tHeight;

		Triangle t1 = new Triangle(Utils._2d(end, -tWidth), Utils._2d(end, tWidth), Utils._2d(end + tHeight, 0));
		Triangle t2 = new Triangle(Utils._2d(-end, -tWidth), Utils._2d(-end, tWidth), Utils._2d(-end - tHeight, 0));

		gl.glLineWidth(2);
		t1.render(gl, glut);
		t2.render(gl, glut);

		gl.glLineWidth(3);
		gl.glBegin(GL.GL_LINE_STRIP);
		gl.glVertex2d(-end, 0);
		gl.glVertex2d(end, 0);
		gl.glEnd();

		gl.glRasterPos2d(end, -3 * tWidth);
		glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "X");
	}

	@Override
	protected ShapeKind getKind() {
		return ShapeKind.AXIS_2D;
	}

}
