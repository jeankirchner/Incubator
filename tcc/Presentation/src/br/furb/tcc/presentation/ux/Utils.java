package br.furb.tcc.presentation.ux;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Classe com fun��es utilit�rias
 * 
 * @author jeank
 * 
 */
public class Utils {

	/**
	 * 360� em radianos
	 */
	public static double	_360	= (Math.PI * 2);

	/**
	 * 45� em radianos
	 */
	public static double	_45		= _360 / 8;

	/**
	 * Ele um n�mero ao quadrado
	 * 
	 * @param a
	 *            o n�mero
	 * @return o quadrado do n�mero
	 */
	public static double sqr(double a) {
		return a * a;
	}

	/**
	 * Dist�ncia entre dois pontos
	 * 
	 * @param x1
	 *            x do ponto 1
	 * @param x2
	 *            x do ponto 2
	 * @param y1
	 *            y do ponto 1
	 * @param y2
	 *            y do ponto 2
	 * @return a dist�ncia entre os dois pontos
	 */
	public static double distance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(sqr(x1 - x2) + sqr(y1 - y2));
	}

	/**
	 * Dist�ncia entre dois pontos
	 * 
	 * @param a
	 *            ponto 1
	 * @param b
	 *            ponto 2
	 * @return a dist�ncia entre os dois pontos
	 */
	public static double distance(Point2D a, Point2D b) {
		return distance(a.getX(), b.getX(), a.getY(), b.getY());
	}

	/**
	 * Cria um ponto 2d com x e y double e retorna, esse m�todo � apenas um facilitador
	 * 
	 * @param x
	 *            a ser utilizado
	 * @param y
	 *            a ser utilizado
	 * @return uma estrutura com pontos x e y do tipo double
	 */
	public static Point2D _2d(double x, double y) {
		return new Point2D.Double(x, y);
	}

	/**
	 * Dados 2 pontos, encontra o quadrante no plano do segundo em rela��o ao primeiro (como se o primeiro fosse origem)
	 * 
	 * @param x0
	 *            x do ponto 0
	 * @param y0
	 *            y do ponto 0
	 * @param x1
	 *            x do ponto 1
	 * @param y1
	 *            y do ponto 1
	 * @return o quadrante, 1 2 3 ou 4
	 */
	public static int quadrantFrom(double x0, double y0, double x1, double y1) {
		if (x1 > x0) {
			if (y1 > y0) {
				return 1;
			} else {
				return 4;
			}
		} else {
			if (y1 > y0) {
				return 2;
			} else {
				return 3;
			}
		}
	}

	/**
	 * Retorna um ponto no formato x,y do tipo double, com o menor x da lista, e o menor y da lista
	 * 
	 * @param points
	 *            a lista de pontos
	 * @return um ponto de acordo com {@link Point2D}
	 */
	public static Point2D min(Iterable<Point2D> points) {
		double x = Double.MAX_VALUE;
		double y = Double.MAX_VALUE;
		for (Point2D p : points) {
			if (p.getX() < x) {
				x = p.getX();
			}
			if (p.getY() < y) {
				y = p.getY();
			}
		}
		return _2d(x, y);
	}

	/**
	 * Retorna um ponto no formato x,y do tipo double, com o maior x da lista, e o maior y da lista
	 * 
	 * @param points
	 *            a lista de pontos
	 * @return um ponto de acordo com {@link Point2D}
	 */
	public static Point2D max(Iterable<Point2D> points) {
		double x = -Double.MAX_VALUE;
		double y = -Double.MAX_VALUE;
		for (Point2D p : points) {
			if (p.getX() > x) {
				x = p.getX();
			}
			if (p.getY() > y) {
				y = p.getY();
			}
		}
		return _2d(x, y);
	}

	/**
	 * Converte os graus de celsius para radianos
	 * 
	 * @return os graus em radianos
	 */
	public static double degreeToRadians(double degree) {
		// regra de 3 simples
		return (_360 * degree) / 360;
	}

	/**
	 * Converte os graus de radianos para celsius
	 * 
	 * @return os graus em celsius
	 */
	public static double radiansToDegree(double degree) {
		// regra de 3 simples
		return (360 * degree) / _360;
	}

	/**
	 * Faz a adi��o de dois pontos, retornando o resultado em b;
	 */
	public static void sumPoints(Point2D a, Point2D b) {
		double x = b.getX();
		double y = b.getY();
		b.setLocation(a.getX() + x, a.getY() + y);
	}

	/**
	 * Faz a subtra��o de dois pontos, retornando o resultado em b;
	 * 
	 * @return
	 */
	public static Point2D subtractPoints(Point2D a, Point2D b) {
		double x = b.getX();
		double y = b.getY();
		return _2d(a.getX() - x, a.getY() - y);
	}

	/**
	 * Se a dist�ncia entre o clique e algum ponto for menor ou igual do que raio 1, ent�o aceita o clique
	 */
	public static Point2D selectPoint(Point2D p, List<Point2D> points) {

		for (Point2D pt : points) {
			if (Utils.distance(p, pt) <= 1) {
				return pt;
			}
		}

		return null;

	}

	/**
	 * Verifica se uma coordenada est� dentro de uma figura irregular
	 */
	public static boolean scanLineSelectShape(List<Point2D> shape, Point2D psel) {

		int n = 0;
		Point2D intersect = null;

		for (int i = 0; i < shape.size(); i++) {

			Point2D p1 = shape.get(i);
			Point2D p2 = shape.get((i + 1) % shape.size());

			if (p1.getY() != p2.getY()) {
				intersect = intersectCoordinateOnX(p1, p2, psel);
				if (intersect != null) {
					if (psel.getX() != intersect.getX()) {
						if (intersect.getX() > psel.getX() && intersect.getY() > Math.min(p1.getY(), p2.getY()) && intersect.getY() <= Math.max(p1.getY(), p2.getY())) {
							n++;
						}
					}
				}
			}

		}

		// se for �mpar est� dentro da figura, se n�o, n�o est�
		return n % 2 != 0;

	}

	/**
	 * Faz a interse��o do ponto sel na reta p1p2. � utilizado o Y como fixo e varia o x, portanto achando o x da intersec��o.
	 */
	public static Point2D intersectCoordinateOnX(Point2D p1, Point2D p2, Point2D sel) {
		double t = (sel.getY() - p1.getY()) / (p2.getY() - p1.getY());
		if (t < 0 || t > 1) {
			return null;
		}
		double x = p1.getX() + (p2.getX() - p1.getX()) * t;

		return Utils._2d(x, sel.getY());
	}

}
