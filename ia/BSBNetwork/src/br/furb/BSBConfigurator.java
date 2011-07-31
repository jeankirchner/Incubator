package br.furb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.furb.rna.BSBNetwork;
import br.furb.rna.IBSBListener;
import br.furb.rna.Matrix;

/**
 * Classe que sabe configurar a rede neural, além de ter o treinamento padrão de A até J
 * 
 * @author jeank
 * 
 */
public class BSBConfigurator {

	// dimensões das matrizes para rede neural
	private static final int rows = 7;
	private static final int cols = 5;

	// matriz estrutura, apenas vai ajudar em outras construções
	private Matrix struct = new Matrix(this.rows, this.cols);

	// Lista de matrizes para uso no treinamento da rede neural
	List<Matrix> trainData = new ArrayList<Matrix>();

	// A rede neural propriamente dita
	private BSBNetwork bsb = new BSBNetwork(this.struct);

	// só treina a rede neural novamente se ouver alteração nos parâmetros (delta e taxa apre.), ou se tem mais padrões a serem treinados
	private boolean changed = false;

	// listener para conseguir assistir o reconhecimento da rede neural
	public BSBConfigurator(IBSBListener listener) {
		this.bsb.addBSBListener(listener);
	}

	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	/**
	 * Armazena uma matriz nova para o treinamento
	 */
	public void store(Matrix m) {
		assert m.linesCount() == this.struct.linesCount() && m.colsCount() == this.struct.colsCount();
		this.changed = true;
		this.trainData.add(m);
	}

	/**
	 * Cria uma matriz baseado na estrutura da matriz struct
	 */
	public Matrix newMatrix() {
		return this.struct.newMatrix();
	}

	/**
	 * Mandar reconhecer uma imagem. <br>
	 * Se esta rede neural ainda não foi treinada, será treinada neste momento
	 */
	public void recognize(Matrix m) {
		if (this.changed) {
			Matrix[] input = Arrays.copyOf(this.trainData.toArray(), this.trainData.size(), Matrix[].class);
			// manda uma lista de matrizes para reconhecimento
			this.bsb.trainNetwork(input);
			this.changed = false;
		}
		// tenta reconhecer a consulta
		this.bsb.recognize(m);
	}

	/**
	 * Altera os parametros delta e taxa de aprendizagem
	 */
	public void setParameters(double delta, double learnFactor) {
		this.bsb.setDelta(delta);
		this.bsb.setLearnFactor(learnFactor);
		this.changed = true;
	}

	/**
	 * Limpa a lista de treinamentos
	 */
	public void clearStore() {
		this.changed = true;
		this.trainData.clear();
	}

	/**
	 * Limpa a lista de treinamentos e adiciona a lista padrão de A até J
	 */
	public void defaultTraining() {
		this.clearStore();
		Matrix[] data = DefaultTraining.defaultTraining();
		for (Matrix x : data) {
			this.store(x);
		}
	}

	/**
	 * Apenas para armazenar as estruturas de treinamento
	 * 
	 */
	private static class DefaultTraining {

		private static Matrix _J() {
			Matrix x = new Matrix(rows, cols);
			double[] values = new double[] { -1.0, -1.0, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, -1.0, 1.0, 1.0, -1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0 };
			x.fill(values);
			return x;
		}

		private static Matrix _I() {
			double[][] values = { { -1, +1, +1, +1, -1 }, { -1, -1, +1, -1, -1 }, { -1, -1, +1, -1, -1 }, { -1, -1, +1, -1, -1 }, { -1, -1, +1, -1, -1 }, { -1, -1, +1, -1, -1 }, { -1, +1, +1, +1, -1 }, };
			return newMatrix(values);
		}

		private static Matrix _H() {
			double[][] values = { { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, +1, +1, +1, +1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, };
			return newMatrix(values);
		}

		private static Matrix _G() {
			double[][] values = { { +1, +1, +1, +1, +1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, +1, +1 }, { +1, -1, -1, -1, +1 }, { +1, +1, +1, +1, +1 }, };
			return newMatrix(values);
		}

		private static Matrix _F() {
			double[][] values = { { +1, +1, +1, +1, +1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, +1, +1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, };
			return newMatrix(values);
		}

		private static Matrix _E() {
			double[][] values = { { +1, +1, +1, +1, +1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, +1, +1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, +1, +1, +1, +1 }, };
			return newMatrix(values);
		}

		private static Matrix _D() {
			double[][] values = { { +1, +1, +1, -1, -1 }, { +1, -1, -1, +1, -1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, +1, -1 }, { +1, +1, +1, -1, -1 }, };
			return newMatrix(values);
		}

		private static Matrix _C() {
			double[][] values = { { +1, +1, +1, +1, +1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, -1, -1, -1, -1 }, { +1, +1, +1, +1, +1 }, };
			return newMatrix(values);
		}

		private static Matrix _B() {
			double[][] values = { { +1, +1, +1, +1, -1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, +1, +1, +1, -1 }, { +1, -1, -1, -1, +1 }, { +1, -1, -1, -1, +1 }, { +1, +1, +1, +1, -1 }, };
			return newMatrix(values);
		}

		private static Matrix _A() {
			Matrix x = new Matrix(rows, cols);
			double[] values = new double[] { 1.0, 1.0, -1.0, 1.0, 1.0, 1.0, -1.0, 1.0, -1.0, 1.0, -1.0, 1.0, 1.0, 1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, -1.0, -1.0, 1.0, 1.0, 1.0, -1.0 };
			x.fill(values);
			return x;
		}

		private static Matrix newMatrix(double[][] values) {
			Matrix m = new Matrix(values.length, values[0].length);

			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < values[i].length; j++) {
					m.setEl(i, j, values[i][j]);
				}
			}
			return m;
		}

		/**
		 * Retorna uma lista contento as matrizes que representam A..J
		 */
		private static Matrix[] defaultTraining() {
			Matrix[] inputs = { _A(), _B(), _C(), _D(), _E(), _F(), _G(), _H(), _I(), _J() };
			return inputs;
		}

	}

}
