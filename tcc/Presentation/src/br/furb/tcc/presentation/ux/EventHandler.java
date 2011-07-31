package br.furb.tcc.presentation.ux;

import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import br.furb.tcc.test.TrilaterationTestGen;
import br.furb.tcc.test.TrilaterationTestGen.TestModel;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Respons�vel por tratar os eventos de usu�rio e repassar para os interessados
 * 
 * @author jeank
 * 
 */
public final class EventHandler extends EventsAdapter {

	// bounds iniciais para o opengl
	private double							defaultBounds	= 1000.0d;

	// objetos de convers�o de dispositivos
	private final NDC						x;
	private final NDC						y;

	// obejtos opengl
	private GL2								gl;
	private GLU								glu;

	// se j� foi inicializado
	private boolean							started			= false;

	// gerenciador de figuras
	private final TrilaterationPresentation	shapeManager	= new TrilaterationPresentation();

	private GLUT							glut;

	private GLAutoDrawable					drawable;

	/**
	 * Cria a classe principal para o trabalho 03 de CG
	 */
	public EventHandler() {
		// ratio 1:1, ou 500:500
		x = new NDC(0, -defaultBounds, 500, defaultBounds);
		y = new NDC(0, -defaultBounds, 500, defaultBounds);
	}

	@Override
	public void init(final GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();

		gl.glEnable(GL.GL_LINE_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);

		// liimpa as cores
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		drawable.setGL(new DebugGL2(gl));
		glu = new GLU();
		glut = new GLUT();

		this.drawable = drawable;

		// se passou pelo init
		started = true;
	}

	private void reconfigure() {

		if (!started) {
			return;
		}

		// reinicia o opengl
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
		gl.glLoadIdentity();

		// pega objeto de transforma��o global do gerenciador de figuras
		Transformation t = shapeManager.getGlobalTransform();

		// faz a escala e pan
		defaultBounds *= t.getScale();
		Point2D move = t.getTranslate();

		glu.gluOrtho2D(-defaultBounds + move.getX(), defaultBounds + move.getX(), -defaultBounds + move.getY(), defaultBounds + move.getY());

		gl.glRotated(t.getRotate(), 0, 0, 1);

		// reconfigura os dispositivos das dimens�es x e y, assim a convers�o de pontos de um dispositivo para outro vai considerar inclusive a escala e o pan
		x.changeDestineCoordinates(-defaultBounds, defaultBounds);
		y.changeDestineCoordinates(-defaultBounds, defaultBounds);

		// configurar cor de desenho (valores r, g, b)
		gl.glColor3f(0.0f, 0.0f, 0.0f);

	}

	@Override
	public void display(GLAutoDrawable arg0) {
		if (!started) {
			return;
		}
		// reconfigura o opengl
		reconfigure();

		// manda renderizar o gerenciador de figuras
		shapeManager.render(gl.getGL2(), glut);

		// joga pra tela
		gl.glFlush();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		double w = e.getComponent().getWidth();
		double h = e.getComponent().getHeight();

		// a raz�o entre as dimens�es x e y
		double xyRatio = x.aspectRatio() / y.aspectRatio();

		// a nova raz�o mediante ao redimensionamento
		double newRatio = w / h;

		// se a nova � menor, � porque aumentou a altura em rela��o a largura, se n�o o contr�rio
		if (newRatio < xyRatio) {

			// ent�o vamos manter a largura e ajustar a altura
			h = w * y.deltaO() / x.deltaO();

		} else {

			// sent�o vamos manter a altura e ajustar a largura
			w = h * x.deltaO() / y.deltaO();

		}

		// redimensiona o componente
		e.getComponent().setSize((int) w, (int) h);

		// atualiza os dispositivos com as novas dimens�es
		x.changeCoordinates(0, -defaultBounds, w, defaultBounds);
		y.changeCoordinates(0, -defaultBounds, h, defaultBounds);

		if (drawable != null) {
			drawable.display();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!started) {
			return;
		}
		shapeManager.keyPressed(e.getKeyCode());
		drawable.display();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int x = 1 + ((e.isControlDown() ? 3 : 0) + (e.isAltDown() ? 3 : 0) + (e.isShiftDown() ? 3 : 0));
		shapeManager.mouseWhellMoved(e.getWheelRotation() * x);
		drawable.display();
	}

	public void generateTestCaseAction() {
		TestModel test = TrilaterationTestGen.genTest(defaultBounds, defaultBounds);
		shapeManager.renderNewTrilaterationtest(test);
		drawable.display();
	}
}
