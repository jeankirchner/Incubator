package br.furb.tcc.presentation.ux;

/**
 * Representa uma cor, onde vai de 0 at� 1<br>
 * Considere este range como uma porcentagem de 0 � 255
 * 
 * @author jeank
 * 
 */
public class Color {

	public static Color	BLACK	= new Color(0, 0, 0);
	public static Color	WHITE	= new Color(1, 1, 1);
	public static Color	GRAY	= new Color(0.5, 0.5, 0.5);
	public static Color	RED		= new Color(1, 0, 0);
	public static Color	GREEN	= new Color(0, 1, 0);
	public static Color	BLUE	= new Color(0, 0, 1);

	/**
	 * representa porcentagem de vermelho nesta cor
	 */
	public double		red;
	/**
	 * representa porcentagem de verde nesta cor
	 */
	public double		green;
	/**
	 * representa porcentagem de azul nesta cor
	 */
	public double		blue;

	public double		alpha;

	/**
	 * Cria uma cor que representa uma cor do openGl
	 * 
	 * @param red
	 *            pocentagem de vermelho
	 * @param green
	 *            porcentagem de verde
	 * @param blue
	 *            porcentagem de azul
	 */
	public Color(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		alpha = 255;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getAlpha() {
		return alpha;
	}

}
