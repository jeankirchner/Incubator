package br.furb;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import br.furb.rna.IBSBListener;
import br.furb.rna.Matrix;

/**
 * Responsável por fazer a construção do formulário para a rede BSB. <br>
 * Também tem um configurador que sabe configurar a rede BSB
 * 
 * @author jeank
 * 
 */
public class BSBForm extends JFrame {

	/** Configurações da rede neural */

	// helper para configurar a rede neural, ele também contém o objeto da rede neural
	private BSBConfigurator configurator;

	// Matriz modelo, para adicionar novos padrões de treinamento
	private Matrix model;

	// Matriz de consulta, para desenhar alguma coisa e mandar reconhecer na rede
	private Matrix consult;

	/*** REFERENTE A CONFIGURACAO VISUAL DA TELA *****/
	// icones para botoes
	private static final Icon WHITE = new ImageIcon(BSBForm.class.getResource("white.gif"));
	private static final Icon BLACK = new ImageIcon(BSBForm.class.getResource("black.gif"));
	private static final Icon GRAY = new ImageIcon(BSBForm.class.getResource("gray.gif"));

	// painel de configuracao
	private JPanel config;

	private JButton record;
	private JButton randomize;
	private JButton recognize;
	private JButton clear;
	private JButton clearStore;
	private JButton defaultTraining;
	private JToggleButton animate;

	private JTextArea infoArea;

	private JTextField deltaField;
	private JTextField learnFactorField;

	ButtonGrid storeMatrix;
	ButtonGrid consultMatrix;

	public BSBForm() {
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.configurator = new BSBConfigurator(new IBSBListener() {

			@Override
			public void stateChanged(Matrix state) {
				BSBForm.this.consult = state;
				BSBForm.this.paintConsultGrid();
				try {
					if (BSBForm.this.animate.isSelected()) {
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {}
			}

			@Override
			public void finish() {
				BSBForm.this.infoArea.setText("Concluído!");
				BSBForm.this.enableButtons();
			}
		});

		this.model = this.configurator.newMatrix();
		this.model.fill(1);
		this.consult = this.configurator.newMatrix();
		this.consult.fill(1);

		this.setVisible(true);

		this.initComponents();
		this.paintStoreGrid();
		this.paintConsultGrid();

		this.paramsChanged();
	}

	private void initComponents() {
		this.record = new JButton("Gravar");
		this.record.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				BSBForm.this.configurator.store(BSBForm.this.model);
				BSBForm.this.model = BSBForm.this.configurator.newMatrix();
				BSBForm.this.model.fill(1);
				BSBForm.this.paintStoreGrid();
			}
		});

		this.randomize = new JButton("Sortear consulta");
		this.randomize.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				BSBForm.this.consult = BSBForm.this.consult.random().round();
				BSBForm.this.paintConsultGrid();
			}
		});

		this.recognize = new JButton("Reconhecer");
		this.recognize.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				BSBForm.this.model.fill(BSBForm.this.consult);
				BSBForm.this.paintStoreGrid();
				BSBForm.this.infoArea.setText("Processando...");
				BSBForm.this.disableButtons();
				BSBForm.this.paramsChanged();
				new Thread(new Runnable() {

					@Override
					public void run() {
						BSBForm.this.configurator.recognize(BSBForm.this.consult);
					}
				}).start();
			}
		});
		this.clear = new JButton("Limpar matrizes");
		this.clear.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				BSBForm.this.clearViews();
			}
		});

		this.clearStore = new JButton("Limpar todo o treinamento");
		this.clearStore.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				BSBForm.this.configurator.clearStore();
			}
		});

		this.defaultTraining = new JButton("Restaurar treinamento padrão (de A até J)");
		this.defaultTraining.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				BSBForm.this.configurator.defaultTraining();
			}
		});

		this.animate = new JToggleButton("Animar reconhecimento: Off");
		this.animate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (BSBForm.this.animate.isSelected()) {
					BSBForm.this.animate.setText("Animar reconhecimento: On");
				} else {
					BSBForm.this.animate.setText("Animar reconhecimento: Off");
				}
			}
		});

		this.config = new JPanel(new GridLayout(9, 1));
		this.config.add(this.record);
		this.config.add(this.randomize);
		this.config.add(this.recognize);
		this.config.add(this.clear);
		this.config.add(this.clearStore);
		this.config.add(this.defaultTraining);
		this.config.add(this.animate);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.config, BorderLayout.WEST);
		JPanel matrixPanel = new JPanel(new GridLayout(1, 3));
		panel.add(matrixPanel, BorderLayout.CENTER);

		this.storeMatrix = new ButtonGrid(this.configurator.getRows(), this.configurator.getCols(), new ButtonGridListener() {

			@Override
			public void cellChanged(int x, int y, JButton but) {
				BSBForm.this.storeMatrixChanged(x, y, but);
			}
		});
		matrixPanel.add(this.storeMatrix.getPanel());

		JPanel middle = new JPanel(new GridLayout(1, 1));
		this.infoArea = new JTextArea("Matriz da esquerda:\n  matriz de treinamento, \n  mude as células e clique em gravar\nMatriz da direita: \n  matriz de consulta:\n  gere uma aleatória ou\n  faça sua própria consulta");
		this.infoArea.setWrapStyleWord(true);
		middle.add(this.infoArea);

		JPanel editors = new JPanel(new GridLayout(1, 2));

		JLabel learnFactorLabel = new JLabel("Taxa de aprendizagem:");
		this.learnFactorField = new JTextField("0.1");
		this.learnFactorField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BSBForm.this.paramsChanged();
			}
		});
		editors.add(learnFactorLabel);
		editors.add(this.learnFactorField);
		this.config.add(editors);

		JLabel deltaLabel = new JLabel("Precisão (delta):");
		this.deltaField = new JTextField("0.0001");
		this.deltaField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BSBForm.this.paramsChanged();
			}
		});
		editors = new JPanel(new GridLayout(1, 2));
		editors.add(deltaLabel);
		editors.add(this.deltaField);
		this.config.add(editors);

		matrixPanel.add(middle);

		this.consultMatrix = new ButtonGrid(this.configurator.getRows(), this.configurator.getCols(), new ButtonGridListener() {

			@Override
			public void cellChanged(int x, int y, JButton but) {
				BSBForm.this.consultMatrixChanged(x, y, but);
			}
		});
		matrixPanel.add(this.consultMatrix.getPanel());

		this.add(panel, BorderLayout.CENTER);

		this.setBounds(100, 100, 1000, 300);
	}

	private void storeMatrixChanged(int x, int y, JButton but) {
		int el = (int) this.model.el(x, y);
		el = el == 1 ? -1 : 1;
		// inverte
		this.model.setEl(x, y, el);
		this.paintButton(but, el);
	}

	private void consultMatrixChanged(int x, int y, JButton but) {
		int el = (int) this.consult.el(x, y);
		el = el == 1 ? -1 : 1;
		// inverte
		this.consult.setEl(x, y, el);

		this.paintButton(but, el);
	}

	private void paintStoreGrid() {
		JButton[][] grid = this.storeMatrix.getGrid();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// A idéia é que sejam inteiros
				this.paintButton(grid[i][j], (int) this.model.el(i, j));
			}
		}
	}

	private void paintConsultGrid() {
		JButton[][] grid = this.consultMatrix.getGrid();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				// A idéia é que sejam inteiros
				this.paintButton(grid[i][j], (int) this.consult.el(i, j));
			}
		}
	}

	private void paintButton(JButton button, int el) {
		Icon ic;
		if (el == 1) {
			ic = WHITE;
		} else if (el == -1) {
			ic = BLACK;
		} else {
			ic = GRAY;
		}

		button.setIcon(ic);
		button.setPressedIcon(ic);
		button.setSelectedIcon(ic);
		button.setRolloverIcon(ic);
		button.setRolloverSelectedIcon(ic);
	}

	private void clearViews() {
		this.model.fill(1);
		this.consult.fill(1);
		this.paintConsultGrid();
		this.paintStoreGrid();
	}

	private void disableButtons() {
		this.record.setEnabled(false);
		this.randomize.setEnabled(false);
		this.recognize.setEnabled(false);
		this.clear.setEnabled(false);
		this.clearStore.setEnabled(false);
		this.defaultTraining.setEnabled(false);
		this.animate.setEnabled(false);
		this.learnFactorField.setEnabled(false);
		this.deltaField.setEnabled(false);
	}

	private void enableButtons() {
		this.record.setEnabled(true);
		this.randomize.setEnabled(true);
		this.recognize.setEnabled(true);
		this.clear.setEnabled(true);
		this.clearStore.setEnabled(true);
		this.defaultTraining.setEnabled(true);
		this.animate.setEnabled(true);
		this.learnFactorField.setEnabled(true);
		this.deltaField.setEnabled(true);
	}

	private void paramsChanged() {
		double delta = Double.parseDouble(this.deltaField.getText());
		double learnFactor = Double.parseDouble(this.learnFactorField.getText());
		this.configurator.setParameters(delta, learnFactor);
	}

}
