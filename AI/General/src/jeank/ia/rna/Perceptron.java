package jeank.ia.rna;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author jeank
 * 
 */
public class Perceptron {

	private static double EPSILON = 0.0000001;

	private int currentEpic = 0;

	private double[] pounds;
	private double delta;
	private double learnFactor;
	private ArrayList<Double[]> input;
	private ArrayList<Integer> expected;
	private double s;
	private int y;

	private boolean infoEnabled = false;

	private int variables;

	public Perceptron(int variables, double[][] input, int[] expected, double[] pounds) {
		assert variables > 0;
		assert input.length > 0;
		assert input[0].length > 0;
		assert expected.length == input.length;
		assert pounds == null || pounds.length - 1 == variables;

		this.variables = variables;

		// erro
		this.delta = 1.0;
		// taxa de aprendizagem
		this.learnFactor = 1.0;
		// taxa de ativação
		this.s = 0.0;

		// dados de treinamento
		this.input = new ArrayList<Double[]>();
		for (int i = 0; i < input.length; i++) {
			Double[] temp = new Double[variables + 1];
			temp[0] = 1.0;
			for (int j = 1; j <= input[0].length; j++) {
				temp[j] = input[i][j - 1];
			}
			this.input.add(temp);
		}

		// respostas esperadas para o treinamento
		this.expected = new ArrayList<Integer>();
		for (int i = 0; i < expected.length; i++) {
			this.expected.add(expected[i]);
		}
		// saida
		this.y = 0;

		// pesos iniciais
		this.pounds = pounds;
	}

	public Perceptron(int variables, double[][] input, int[] expected) {
		this(variables, input, expected, null);
		this.pounds = new double[variables + 1];
		Random r = new Random();
		for (int i = 0; i < this.pounds.length; i++) {
			this.pounds[i] = r.nextDouble();
		}
	}

	public void setInfoEnabled(boolean infoEnabled) {
		this.infoEnabled = infoEnabled;
	}

	private int comp(double a, double b) {
		double x = a - b;
		if (Math.abs(x) < EPSILON) {
			return 0;
		} else if (x < 0) {
			return -1;
		}
		return 1;
	}

	private void calcS(int registry) {
		this.s = 0.0;
		Double[] reg = this.input.get(registry);
		for (int i = 0; i < this.pounds.length; i++) {
			this.s += this.pounds[i] * reg[i];
		}
	}

	private void calcPounds(int registry) {
		this.pounds[0] = this.pounds[0] + this.learnFactor * this.delta;
		Double[] in = this.input.get(registry);
		for (int i = 1; i < this.pounds.length; i++) {
			this.pounds[i] = this.pounds[i] + this.learnFactor * in[i - 1] * this.delta;
		}
	}

	public void calculateAnEpic() {
		this.currentEpic++;

		this.outLn("Epic: " + this.currentEpic);
		for (int i = 0; i < this.input.size(); i++) {
			this.calcS(i);
			this.y = this.comp(this.s, 0.0) < 0 ? -1 : 1;
			this.delta = this.expected.get(i) - this.y;
			this.out(i + ": ");
			this.print();
			this.calcPounds(i);
		}
		this.learnFactor *= 0.9999;
		this.outLn("");
	}

	private void print() {
		int w = 0;
		this.out("W0: " + this.pounds[0]);
		for (int i = 1; i < this.pounds.length; i++, w++) {
			this.out(" W" + w + " : " + this.pounds[i]);
		}
		this.out(" s: " + this.s + " y: " + this.y + " delta: " + this.delta);
		this.outLn("");
	}

	public void addInput(int expc, double... newIn) {
		Double[] temp = new Double[this.variables + 1];
		temp[0] = 1.0;
		for (int i = 0; i < this.variables; i++) {
			temp[i + 1] = newIn[i];
		}
		this.input.add(temp);
		this.expected.add(expc);
	}

	private void out(Object msg) {
		if (this.infoEnabled) {
			System.out.print(msg);
		}
	}

	private void outLn(Object msg) {
		this.out(msg);
		this.out('\n');
	}

}
