package br.furb.tcc.presentation.ux;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import br.furb.tcc.maths.Line2D;
import br.furb.tcc.test.TrilaterationTestGen.TestModel;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Gerenciador de figuras geom�tricas
 * 
 * � um singleton
 * 
 * @author jeank
 * 
 */
public class TrilaterationPresentation {

	private final boolean			showRealSource		= true;
	private final boolean			showEstimatedSource	= true;
	private final boolean			showlines			= true;

	private final Transformation	globalTransform		= new Transformation();
	private final List<Shape>		shapes				= new ArrayList<Shape>();

	public TrilaterationPresentation() {
		clearScreen();
	}

	private void addShape(Shape s) {
		assert s != null;
		this.shapes.add(s);
	}

	/**
	 * Aqui vai acontecer a renderiza��o do estado atual dos componentes
	 */
	public void render(GL2 gl, GLUT glut) {
		for (Shape s : shapes) {
			s.render(gl, glut);
		}
	}

	private void scale(double x) {
		globalTransform.scale(-x);
	}

	private void rotate(double x) {
		globalTransform.rotate(x);
	}

	private void translate(double x, double y) {
		globalTransform.translate(Utils._2d(-x, -y));
	}

	public void keyPressed(int i) {
		switch (i) {
		case '=':
		case '+':
		case KeyEvent.VK_PLUS:
			scale(0.03);
			break;
		case '-':
			scale(-0.03);
			break;
		case KeyEvent.VK_LEFT:
			translate(-10, 0);
			break;
		case KeyEvent.VK_RIGHT:
			translate(10, 0);
			break;
		case KeyEvent.VK_DOWN:
			translate(0, -10);
			break;
		case KeyEvent.VK_UP:
			translate(0, 10);
			break;
		}
	}

	public void mouseWhellMoved(int wheelRotation) {
		rotate(wheelRotation);
	}

	private void clearScreen() {
		shapes.clear();
		globalTransform.clear();
		addShape(new Axis2D());
	}

	public Transformation getGlobalTransform() {
		return globalTransform;
	}

	public void renderNewTrilaterationtest(TestModel test) {
		clearScreen();

		Triangle triangle = new Triangle(test.getModel().getReceptor1(), test.getModel().getReceptor2(), test.getModel().getReceptor3());
		triangle.setLineWidth(2);
		triangle.setColor(Color.BLACK);
		addShape(triangle);

		Circle c = new Circle(test.getModel().getCenter(), test.getModel().getSide());
		c.setColor(Color.RED);
		addShape(c);

		if (showEstimatedSource) {
			renderEstimatedSource(test);
		}
		if (showRealSource) {
			renderRealSource(test);
		}
		if (showlines) {
			renderLines(test);
		}

	}

	private void renderPoints(List<Point2D> points) {
		for (Point2D p : points) {
			Circle c = new Circle(p, Utils._2d(p.getX(), p.getY() + 20));
			c.setColor(Color.BLUE);
			addShape(c);
		}
	}

	private void renderLines(TestModel test) {
		List<Line2D> lines = test.getTest().getLines();
		List<Point2D> points = test.getTest().getIntersections();

		for (Line2D l : lines) {
			double x1 = 100000;
			double y1 = l.getAngularCoeficient() * x1 + l.getLinearCoeficient();
			double x2 = -100000;
			double y2 = l.getAngularCoeficient() * x2 + l.getLinearCoeficient();
			FreeShape shape = new FreeShape(Utils._2d(x1, y1), Utils._2d(x2, y2));
			shape.setColor(Color.GRAY);
			addShape(shape);
		}

		renderPoints(points);
	}

	private void renderRealSource(TestModel test) {
		Point2D p = test.getRealSource().getSource();
		Circle c = new Circle(p, 50);
		c.setLineWidth(3);
		c.setColor(Color.GREEN);
		addShape(c);

		Text t = new Text(p, "Fonte REAL");
		t.setColor(Color.BLACK);
		addShape(t);
	}

	private void renderEstimatedSource(TestModel test) {
		Point2D p = test.getResult().getSource();
		Circle c = new Circle(p, 50);
		c.setLineWidth(3);
		c.setColor(Color.RED);
		addShape(c);

		Text t = new Text(p, "Fonte ESTIMADA");
		t.setColor(Color.BLACK);
		addShape(t);
	}
}