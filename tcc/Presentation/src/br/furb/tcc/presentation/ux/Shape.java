package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Interface para as figuras 2d, comportamentos que todas elas devem ter
 * 
 * @author jeank
 * 
 */
public abstract class Shape {

	private Color			defaultColor	= Color.BLUE;
	protected Color			color			= defaultColor;
	protected int			drawMode		= GL.GL_POINTS;
	protected List<Point2D>	controlPoints	= new ArrayList<Point2D>();
	private float			lineWidth		= 1;

	Shape(Point2D p, Point2D p2) {
		controlPoints.add(p);
		controlPoints.add(p2);
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public float getLineWidth() {
		return lineWidth;
	}

	/**
	 * @return os pontos da figura geom�trica
	 */
	public abstract List<Point2D> points();

	public void addPoint(Point2D p) {
		if (getKind().getNeededPoints() > 0 && controlPoints.size() > getKind().getNeededPoints()) {
			throw new IllegalStateException("The limit of points for this shape has reached");
		}
		controlPoints.add(p);
	}

	/**
	 * @return a cor que a figura geom�trica ser� renderizada
	 */
	public Color color() {
		return color;
	}

	/**
	 * Informa uma nova cor para esta figura
	 */
	public void setColor(Color c) {
		assert c != null;
		color = c;
	}

	/**
	 * Modo de renderiza��o, pode ser:<br>
	 * GL.GL_POINTS, GL.GL_LINES, GL.GL_LINE_LOOP, GL.GL_LINE_STRIP, GL.GL_TRIANGLES, GL.GL_TRIANGLE_FAN, GL.GL_TRIANGLE_STRIP, GL.GL_QUADS, GL.GL_QUAD_STRIP, GL.GL_POLYGON
	 * 
	 * @return O modo que a figura geom�trica ser� renderizado
	 */
	public int drawMode() {
		return drawMode;
	}

	protected abstract ShapeKind getKind();

	/**
	 * Renderiza esta figura
	 */
	public void render(GL2 gl, GLUT glut) {
		getKind().render(this, gl, glut);
	}

	public void setDefaultColor(Color currentColor) {
		defaultColor = currentColor;
	}

}
