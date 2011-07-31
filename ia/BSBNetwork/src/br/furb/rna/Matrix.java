package br.furb.rna;

import java.util.Arrays;
import java.util.Random;

public class Matrix {

	double[][] M;

	/**
	 * Cria uma matrix linhas por colunas (linesXcolumns)
	 */
	public Matrix(int lines, int columns) {
		if (lines <= 0 || columns <= 0) {
			throw new IllegalArgumentException("Lines and columns count must be > 0");
		}
		this.M = new double[lines][columns];
	}

	// verificações de bounds
	private void checkLineColumn(int line, int column) {
		if (line > this.M.length) {
			throw new IllegalArgumentException("Line must be in range of: [0-" + (this.M.length - 1) + "]");
		}
		if (column > this.M[0].length) {
			throw new IllegalArgumentException("Column must be in range of: [0-" + (this.M[0].length - 1) + "]");
		}
	}

	public void setEl(int line, int column, double val) {
		this.checkLineColumn(line, column);
		this.M[line][column] = val;
	}

	public double el(int line, int column) {
		this.checkLineColumn(line, column);
		return this.M[line][column];
	}

	public int linesCount() {
		return this.M.length;
	}

	public int colsCount() {
		return this.M[0].length;
	}

	public int size() {
		return this.M.length * this.M[0].length;
	}

	/**
	 * Retorna esta matriz como vetor
	 */
	public double[] vector() {
		double[] values = new double[this.size()];
		int k = 0;
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++, k++) {
				values[k] = this.M[i][j];
			}
		}
		return values;
	}

	/**
	 * Constrói uma matrix baseada no número de linhas e colunas desta, baseado em um vetor de valores
	 */
	public static Matrix fromVector(double[] values) {
		if (values.length == 0) {
			throw new IllegalArgumentException("Values must have at least one element");
		}
		Matrix C = new Matrix(values.length, 1);
		C.fill(values);
		return C;
	}

	/**
	 * Cria matriz baseado na estrutura desta
	 */
	public Matrix newMatrix() {
		return new Matrix(this.linesCount(), this.colsCount());
	}

	/**
	 * Cria matriz baseado na estrutura desta, mas com os valores passados como parâmetro
	 */
	public Matrix newFromVector(double[] values) {
		Matrix C = this.newMatrix();
		C.fill(values);
		return C;
	}

	/**
	 * Vai somar a matrix B nesta matriz
	 * 
	 * @return
	 */
	public Matrix sum(Matrix B) {
		if (this.linesCount() != B.linesCount() || this.colsCount() != B.colsCount()) {
			throw new IllegalArgumentException("Matrix B must be of the same order: " + this.linesCount() + " x " + this.colsCount());
		}
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.linesCount(); j++) {
				this.M[i][j] += B.el(i, j);
			}
		}
		return this;
	}

	/**
	 * Vai subtrair a matrix B nesta matriz
	 */
	public Matrix sub(Matrix B) {
		if (this.linesCount() != B.linesCount() || this.colsCount() != B.colsCount()) {
			throw new IllegalArgumentException("Matrix B must be of the same order: " + this.linesCount() + " x " + this.colsCount());
		}
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				this.M[i][j] -= B.el(i, j);
			}
		}
		return this;
	}

	/**
	 * Vai multiplicar essa matrix e a matrix B, nesta matrix, e retornar
	 */
	public Matrix mul(Matrix B, Matrix C) {
		if (this.colsCount() != B.linesCount()) {
			throw new IllegalArgumentException("Columns count of B, must have the same count as lines count of this matrix");
		}
		if (C == null) {
			C = new Matrix(this.linesCount(), B.colsCount());
		} else if (C.linesCount() != this.linesCount() || C.colsCount() != B.colsCount()) {
			throw new IllegalArgumentException("Matrix C bounds is wrong according dimensions of A and B on operation A * B");
		}
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < B.colsCount(); j++) {
				double res = 0.0;
				for (int k = 0; k < this.colsCount(); k++) {
					res += this.el(i, k) * B.el(k, j);
				}
				C.setEl(i, j, res);
			}
		}
		return C;
	}

	/**
	 * Multiplica todos os elementos desta matriz pelo valor passado como parâmetro
	 */
	public Matrix mul(double x) {
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				this.M[i][j] *= x;
			}
		}
		return this;
	}

	/**
	 * Faz a multiplicação pela trasposta de B, sem alterar a estrutura interna de B <br>
	 * Ao invés de multiplicar as linhas desta matriz pelas colunas de B, passamos a multiplicar pelas linhas de B,
	 * "simulando a multiplicação pela transposta de B"
	 */
	public Matrix tMul(Matrix B, Matrix C) {
		if (this.colsCount() != B.colsCount()) {
			throw new IllegalArgumentException("Lines count of B, must have the same count as lines count of this matrix");
		}
		// usa cols de B assim como desta matrix, por estar usando como transposta
		if (C == null) {
			C = new Matrix(this.linesCount(), B.linesCount());
		} else if (C.linesCount() != this.linesCount() || C.colsCount() != B.linesCount()) {
			throw new IllegalArgumentException("Matrix C bounds is wrong according dimensions of A and B on operation (A * B') ");
		}
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < B.linesCount(); j++) {
				double res = 0.0;
				for (int k = 0; k < this.colsCount(); k++) {
					res += this.el(i, k) * B.el(j, k);
				}
				C.setEl(i, j, res);
			}
		}
		return C;

	}

	/**
	 * Retorna o valor máximo absoluto desta matriz
	 */
	public double absMax() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				max = Math.max(max, Math.abs(this.M[i][j]));
			}
		}
		return max;
	}

	/**
	 * Zera esta matriz
	 */
	public void clear() {
		for (int i = 0; i < this.linesCount(); i++) {
			Arrays.fill(this.M[i], 0);
		}
	}

	/**
	 * Soma uma identidade a esta matriz
	 */
	public void sumIdentity() {
		if (this.linesCount() != this.colsCount()) {
			throw new IllegalStateException("Lines and cols of this matrix must have the same count for a sum with yours identity");
		}
		for (int i = 0; i < this.linesCount(); i++) {
			this.M[i][i] += 1;
		}
	}

	/**
	 * Compara as matrizes e diz se é igual
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Matrix) {
			Matrix m = (Matrix) obj;
			if (this.linesCount() != m.linesCount() || this.colsCount() != m.colsCount()) {
				return false;
			}
			for (int i = 0; i < this.linesCount(); i++) {
				for (int j = 0; j < this.colsCount(); j++) {
					if (this.M[i][j] != m.el(i, j)) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Preenche esta matriz com os valores da matriz de B, não precisando ter a mesma estrutura mas sim o mesmo número de elementos
	 */
	public void fill(Matrix B) {
		if (this.size() != B.size()) {
			throw new IllegalArgumentException("Matrix B must have the same number of elements that this matrix");
		}
		this.fill(B.vector());
	}

	/**
	 * Preenche esta matriz com os valores do vetor de B, necessário ter o mesmo número de elementos
	 */
	public void fill(double[] values) {
		if (this.size() != values.length) {
			throw new IllegalArgumentException("Matrix B must have the same number of elements that this matrix");
		}
		int k = 0;
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++, k++) {
				this.M[i][j] = values[k];
			}
		}
	}

	/**
	 * Preenche cada elemento desta matriz com o valor de x
	 */
	public void fill(double x) {
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				this.M[i][j] = x;
			}
		}
	}

	/**
	 * Gera uma matriz com a mesma estrutura desta, com valores sorteados
	 */
	public Matrix random() {
		Random r = new Random();
		Matrix C = new Matrix(this.linesCount(), this.colsCount());
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				// entre -1 e 1
				C.M[i][j] = (r.nextDouble() * 2) - 1;
			}
		}
		return C;
	}

	/**
	 * Faz arredondamento de cada elemento desta matriz de tal forma que os valores possam ser [-1, 0, 1]
	 */
	public Matrix round() {
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				this.M[i][j] = Math.round(this.M[i][j]);
			}
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.linesCount(); i++) {
			sb.append("|\t");
			sb.append("  ");
			for (int j = 0; j < this.colsCount(); j++) {
				sb.append(this.M[i][j]);
				sb.append("  ");
			}
			sb.append("\t|\n");
		}
		return sb.toString();
	}

	// para ajudar a desenhar as figuras, ignorar
	public String toArrayString() {
		StringBuilder sb = new StringBuilder();
		sb.append("new double[] {");
		for (int i = 0; i < this.linesCount(); i++) {
			for (int j = 0; j < this.colsCount(); j++) {
				sb.append(" ");
				sb.append(this.M[i][j]);
				sb.append(" ,");
			}
		}
		sb.setCharAt(sb.length() - 1, '}');
		return sb.toString();
	}
}
