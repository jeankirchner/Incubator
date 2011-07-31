package br.furb.tcc.presentation.ux;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * Formul�rio para facilitar a execu��o dos exerc�cios
 * 
 * @author jeank
 * 
 */
public class TCCPresentation extends JFrame {

	private static int				DEFAULT_BOUNDS		= 700;
	private static final long		serialVersionUID	= 1L;
	private final GLCapabilities	glCaps;
	private final EventHandler		handler;

	/**
	 * Cria um executor de exercicios
	 * 
	 */
	public TCCPresentation() {
		super("Demonstração de resultados");
		handler = new EventHandler();

		// Cria essa janela posicionado um pouco ao lado da lista
		this.setBounds(50, 100, DEFAULT_BOUNDS, DEFAULT_BOUNDS);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		// Cria os componentes
		initComponents();

		/*
		 * Cria um objeto GLCapabilities para especificar o n�mero de bits por pixel para RGBA
		 */
		glCaps = new GLCapabilities(null);

		// usar double buffer
		glCaps.setDoubleBuffered(true);

		// usar placa gr�fica
		glCaps.setHardwareAccelerated(true);

		glCaps.setRedBits(16);
		glCaps.setBlueBits(16);
		glCaps.setGreenBits(16);
		glCaps.setAlphaBits(16);
	}

	private void initComponents() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());

		JPanel center = new JPanel(new BorderLayout());
		JPanel toolbar = new JPanel();
		mountToolbar(toolbar);

		add(center, BorderLayout.CENTER);
		add(toolbar, BorderLayout.EAST);
		toolbar.setBounds(0, 0, 100, toolbar.getHeight());

		/*
		 * Cria um canvas, adiciona ao frame e objeto "ouvinte" para os eventos Gl, de mouse e teclado
		 */
		GLCanvas canvas = new GLCanvas(glCaps);

		// adciona os eventos
		canvas.addGLEventListener(handler);
		canvas.addKeyListener(handler);
		canvas.addMouseListener(handler);
		canvas.addMouseMotionListener(handler);
		canvas.addComponentListener(handler);
		canvas.addMouseWheelListener(handler);

		// adiciona neste formul�rio
		center.add(canvas, BorderLayout.CENTER);

		canvas.requestFocus();
	}

	private void mountToolbar(JPanel toolbar) {
		JButton genTestCase = new JButton();
		genTestCase.setText("Gerar caso de teste");
		toolbar.add(genTestCase);
		genTestCase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handler.generateTestCaseAction();
			}
		});
	}
}
