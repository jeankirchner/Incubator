package br.furb.rna;

import java.util.ArrayList;
import java.util.List;

/**
 * Rede neural BSB
 * 
 * @author jeank
 * 
 */
public class BSBNetwork {

	// utiliza como padrão inicialmente
	private double delta = 0.0001d;
	private double learnFactor = 0.1d;

	// estrutura model, para validações e conversões
	private final Matrix patternStructure;

	// matriz de neuronios
	private final Matrix neurons;

	// objetos que estão assistindo esta rede
	private List<IBSBListener> listeners = new ArrayList<IBSBListener>();

	public BSBNetwork(Matrix patternStructure) {
		this.patternStructure = patternStructure;
		// um peso para cada entrada, mais a saida de cada ep
		this.neurons = new Matrix(patternStructure.size(), patternStructure.size());
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}

	public void setLearnFactor(double learnFactor) {
		this.learnFactor = learnFactor;
	}

	public void trainNetwork(Matrix... inputs) {
		// quando for treinar a rede, limpa o treinamento antigo
		this.neurons.clear();

		// se não tiver entrada nao faz nada
		if (inputs.length == 0) {
			return;
		}

		// transforma as entradas em vetores
		inputs = this.checkInput(inputs);

		// matriz de correção de pesos
		Matrix weightCorrection = null;
		// vetor de ativações
		Matrix s = null;
		do {
			// para cada entrada
			for (Matrix x : inputs) {
				// calcule s(k) = Wx(k)
				// multiplica a entrada pelos pesos e armazena em s
				s = this.neurons.mul(x, /* otimização para não recriar s sempre */s);

				// calcule (delta)W = N(x(k) – s(k))x(k)’
				weightCorrection = x.sub(s).mul(this.learnFactor).tMul(x, weightCorrection);

				this.neurons.sum(weightCorrection);
			}
		} while (weightCorrection.absMax() > this.delta);

		this.neurons.sumIdentity();

		// mostra os neuronios
		System.out.println(this.neurons);
	}

	private Matrix[] checkInput(Matrix... inputs) {
		for (int i = 0; i < inputs.length; i++) {
			Matrix input = inputs[i];
			if (input.linesCount() != this.patternStructure.linesCount() || input.colsCount() != this.patternStructure.colsCount()) {
				throw new IllegalArgumentException("Input structure must have the same structure of the pattern used to create this BSB");
			}
			inputs[i] = Matrix.fromVector(input.vector());
		}
		return inputs;
	}

	public void recognize(Matrix input) {
		Matrix x1 = this.checkInput(input)[0];
		Matrix x2 = null;

		Matrix s = null;
		while (!x1.equals(x2)) {

			s = this.neurons.mul(x1, s);
			x2 = this.f(s, x2);

			Matrix temp = x1;
			x1 = x2;
			x2 = temp;

			input.fill(x1);
			this.fireStateChanged(input);
		}

		input.fill(x2);

		this.fireFinish();
	}

	/**
	 * 
	 * Função de transferência "Rampa"
	 * 
	 * f(s) = <br>
	 * -1 se si(k) < -1 <br>
	 * +1 se si(k) > 1 <br>
	 * si(k) se -1 <= si(k) <= 1
	 * 
	 */
	private Matrix f(Matrix s, Matrix x2) {
		if (x2 == null) {
			x2 = new Matrix(s.linesCount(), s.colsCount());
		}
		for (int i = 0; i < s.linesCount(); i++) {
			for (int j = 0; j < s.colsCount(); j++) {
				double x = s.el(i, j);
				if (x < -1) {
					x = -1;
				} else if (x > 1) {
					x = 1;
				}
				x2.setEl(i, j, x);
			}
		}
		return x2;
	}

	/**
	 * Adiciona um observador
	 */
	public void addBSBListener(IBSBListener l) {
		this.listeners.add(l);
	}

	/**
	 * Dispara evento de reconhecimento
	 */
	private void fireStateChanged(Matrix state) {
		for (IBSBListener l : this.listeners) {
			l.stateChanged(state);
		}
	}

	/**
	 * Dispara evento que terminou o reconhecimento
	 */
	private void fireFinish() {
		for (IBSBListener l : this.listeners) {
			l.finish();
		}
	}

	@Override
	public String toString() {
		return this.neurons.toString();
	}

}
