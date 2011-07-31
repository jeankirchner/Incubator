package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Enumera��o com os tipos de figuras poss�veis de serem criadas
 * 
 * @author jeank
 * 
 */
public enum ShapeKind {

	/**
	 * Figura livre, pode ir criando pontos a vontade.
	 * 
	 * @see FreeShape
	 */
	FREE_SHAPE {

		@Override
		public int getNeededPoints() {
			return -1;
		}
	},

	/**
	 * Figura livre, pode ir criando pontos a vontade.
	 * 
	 * @see FreeShape
	 */
	FREE_CLOSED_SHAPE {

		@Override
		public int getNeededPoints() {
			return -1;
		}
	},

	/**
	 * Um ret�ngulo.
	 * 
	 * @see Rectangle
	 * 
	 */
	RECTANGLE {

		@Override
		public int getNeededPoints() {
			return 2;
		}
	},
	/**
	 * Um c�rculo.
	 * 
	 * @see Circle
	 */
	CIRCLE {

		@Override
		public int getNeededPoints() {
			return 2;
		}

	},

	AXIS_2D {

		@Override
		public int getNeededPoints() {
			return 0;
		}
	},
	TRIANGLE {

		@Override
		public int getNeededPoints() {
			return 3;
		}
	};

	public abstract int getNeededPoints();

	protected void renderPoints(GL2 gl, int mode, List<Point2D> points, float lineWidth) {
		gl.glLineWidth(lineWidth);
		gl.glBegin(mode);
		for (Point2D p : points) {
			gl.glVertex2d(p.getX(), p.getY());
		}
		gl.glEnd();
	}

	protected void renderShape(Shape shape, GL2 gl) {
		Color c = shape.color();
		gl.glColor4d(c.red, c.green, c.blue, c.alpha);
		renderPoints(gl, shape.drawMode(), shape.points(), shape.getLineWidth());
	}

	/**
	 * Renderiza uma figura {@link Shape}
	 * 
	 * @param shape
	 *            a figura a ser renderizada
	 * @param gl
	 *            objeto do openGl onde ser� renderizado as estruturas
	 * @param glut
	 */
	public void render(Shape shape, GL2 gl, GLUT glut) {
		renderShape(shape, gl);
	}

}
