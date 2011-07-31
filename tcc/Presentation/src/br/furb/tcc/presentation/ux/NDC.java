package br.furb.tcc.presentation.ux;

/**
 * 
 * Convers�o de coordenadas de um objeto origem para um destino
 * 
 * Simplificada:
 * 
 * dO= DELTAO (objeto origem) = (c-a) dD = DELTAD (objeto destino) = (f-d)
 * 
 * e = ((b-a) * (dD/dO)) + d <br>
 * b = ((e-d) * (dD/dO)) + a
 * 
 * @author jeank
 * 
 */
public class NDC {

	private double	fromO, toO, fromD, toD;
	private double	deltaO, deltaD;

	/**
	 * Cria um ndc com as coordenadas iniciais
	 * 
	 * @param fromO
	 *            ponto origem do dispositivo origem
	 * @param fromD
	 *            ponto destino do dispositivo origem
	 * @param toO
	 *            ponto origem do dispositivo destino
	 * @param toD
	 *            ponto destino do dispositivo destino
	 */
	public NDC(double fromO, double fromD, double toO, double toD) {
		changeCoordinates(fromO, fromD, toO, toD);
	}

	/**
	 * Altera as coordenadas
	 * 
	 * @param fromO
	 *            ponto origem do dispositivo origem
	 * @param fromD
	 *            ponto destino do dispositivo origem
	 * @param toO
	 *            ponto origem do dispositivo destino
	 * @param toD
	 *            ponto destino do dispositivo destino
	 */
	public void changeCoordinates(double fromO, double fromD, double toO, double toD) {
		this.fromO = fromO;
		this.toO = toO;
		this.fromD = fromD;
		this.toD = toD;
		calcDelta();
	}

	public void changeOriginCoordinates(double fromO, double toO) {
		this.fromO = fromO;
		this.toO = toO;
		calcDelta();
	}

	public void changeDestineCoordinates(double fromD, double toD) {
		this.fromD = fromD;
		this.toD = toD;
		calcDelta();
	}

	private void calcDelta() {
		deltaO = toO - fromO;
		deltaD = toD - fromD;
	}

	/**
	 * Ponto ser� convertido do sistema de coordenadas origem para destino, informado para este conversor
	 * 
	 * @param toConv
	 *            a coordenada a ser convertida
	 * @return A coordenada convertida
	 */
	public double fromTo(double toConv) {
		return ((toConv - fromO) * (deltaD / deltaO)) + fromD;
	}

	/**
	 * Ponto ser� convertido do sistema de coordenadas destino para origem, informado para este conversor
	 * 
	 * @param toConv
	 *            a coordenada a ser convertida
	 * @return A coordenada convertida
	 */
	public double toFrom(double toConv) {
		return ((toConv - fromD) * (deltaD / deltaO)) + fromO;
	}

	/**
	 * Retorna a dimens�o do dispositivo de destino
	 * 
	 * @return a dimens�o do dispositivo de destino
	 */
	public double deltaD() {
		return deltaD;
	}

	/**
	 * Retorna a dimens�o do dispositivo de origem
	 * 
	 * @return a dimens�o do dispositivo de origem
	 */
	public double deltaO() {
		return deltaD;
	}

	/**
	 * Calcula a rela��o entre a dimens�o de origem e a de destino
	 * 
	 * @return a rela��o entre a dimens�o de origem e a de destino
	 */
	public double aspectRatio() {
		return deltaD / deltaO;
	}

}
