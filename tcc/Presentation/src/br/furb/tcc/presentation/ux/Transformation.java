package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;

/**
 * Class que cuida das transforma��es de pontos em escala, rota��o e transla��o
 * 
 * @author jeank
 * 
 */
public class Transformation {

	private double			scale				= 1;
	private double			rotate				= 0;
	private final Point2D	translate			= Utils._2d(0, 0);

	private final Matrix	resultMatrix		= new Matrix(3, 3);
	private final Matrix	translatorMatrix	= new Matrix(3, 3);
	private final Matrix	rotateMatrix		= new Matrix(3, 3);
	private final Matrix	scaleMatrix			= new Matrix(3, 3);

	public Transformation() {
		// elemento neutro 1, coloca a matriz de escala em identidade
		scaleMatrix.sumIdentity();
	}

	/**
	 * Aumenta ou diminui a escala
	 */
	public void scale(double s) {
		scale += s;
	}

	/**
	 * Faz rota��o para direita ou para esquerda
	 */
	public void rotate(double radius) {
		rotate += radius;
	}

	/**
	 * Faz transla��o para este objeto
	 */
	public void translate(Point2D point) {
		double x = point.getX();
		double y = point.getY();
		translate.setLocation(translate.getX() + x, translate.getY() + y);
	}

	/**
	 * Transforma��es de um ponto
	 */
	public void globalTransform(Point2D p) {
		// para um ponto apenas, prepara as matrizes e n�o limpa, sendo que pode ser utilizado para mais pontos
		beginTransform();

		// faz a transforma��o do ponto
		Matrix point = Matrix.fromVector(new double[] { p.getX(), p.getY(), 1 });
		point.mul(resultMatrix).mul(rotateMatrix);
		p.setLocation(point.el(0, 0), point.el(0, 1));
	}

	/**
	 * Come�a uma nova transforma��o
	 */
	private void beginTransform() {
		// faz limpeza para garantir caso n�o tenha sido feito antes
		double s = scale;
		double r = rotate;
		double x = translate.getX();
		double y = translate.getY();

		endTransform();

		scale = s;
		rotate = r;
		translate.setLocation(x, y);

		// convers�o de graus para radianos
		r = Utils.degreeToRadians(r);

		// limpa
		resultMatrix.fill(0);
		resultMatrix.sumIdentity();

		// mant�m a escala para ambox, x e y
		if (scale < 0) {
			// caso � zoom out, vai inverter, ent�o tem que usar 1/S
			scaleMatrix.setEl(0, 0, 1 / scale);
			scaleMatrix.setEl(1, 1, 1 / scale);
		} else {
			// se n�o apenas S
			scaleMatrix.setEl(0, 0, scale);
			scaleMatrix.setEl(1, 1, scale);
		}

		// ajusta os senos e cosenos dos radianos
		rotateMatrix.setEl(0, 0, Math.cos(r));
		rotateMatrix.setEl(0, 1, -Math.sin(r));
		rotateMatrix.setEl(1, 0, Math.sin(r));
		rotateMatrix.setEl(1, 1, Math.cos(r));
		rotateMatrix.setEl(2, 2, 1);

		// coloca o de transla��o para ficar com a diagonal com 1, ou seja, identidade
		translatorMatrix.sumIdentity();
		// seta os pontos de transla��o na matriz
		translatorMatrix.setEl(2, 0, translate.getX());
		translatorMatrix.setEl(2, 1, translate.getY());

		// acumula a escala e transla��o em uma matriz, rota��o n�o, pois pode desejar ser feito em rela��o a um ponto
		resultMatrix.mul(scaleMatrix).mul(translatorMatrix);
	}

	/**
	 * Discarta os dados atuais para come�ar novamente
	 */
	private void endTransform() {
		scaleMatrix.fill(0);
		scaleMatrix.sumIdentity();
		rotateMatrix.fill(0);
		translatorMatrix.fill(0);
		scale = 1;
		rotate = 0;
		translate.setLocation(0, 0);
	}

	public void clear() {
		endTransform();
	}

	public double getScale() {
		double s = scale;
		scale = 1;
		return s;
	}

	public Point2D getTranslate() {
		return translate;
	}

	public double getRotate() {
		return rotate;
	}

}
