package br.furb.tcc.maths;

import java.awt.geom.Point2D;

import br.furb.tcc.sound.utils.Utils;

public class TrilaterationModelUtils {

	public static GeneralTrilaterationModel create2DModel(double xCenter, double yCenter, double distance) {
		distance = Math.abs(distance);
		double distanceUnder2 = distance / 2;

		double x1 = distanceUnder2 + xCenter;
		double x2 = xCenter - distanceUnder2;
		double x3 = xCenter;

		// distancia entre microfones = distance, usando teorema de pitagoras para achar a altura de y do microfone do meio
		double y3 = Math.sqrt(Math.pow(distance, 2) - Math.pow(((x1 - x2) / 2), 2)) / 2;

		// como os centros dos microfones fica em (0,0)
		double y1 = yCenter - y3;
		double y2 = y1;

		// até aqui y3 era a distância
		y3 = yCenter + y3;

		Point2D receiver1 = new Point2D.Double(x3, y3);
		Point2D receiver2 = new Point2D.Double(x1, y1);
		Point2D receiver3 = new Point2D.Double(x2, y2);

		Point2D avg = Utils.averagePoint(receiver1, receiver2, receiver3);

		double xAdj = xCenter - avg.getX();
		double yAdj = yCenter - avg.getY();

		receiver1.setLocation(receiver1.getX() + xAdj, receiver1.getY() + yAdj);
		receiver2.setLocation(receiver2.getX() + xAdj, receiver2.getY() + yAdj);
		receiver3.setLocation(receiver3.getX() + xAdj, receiver3.getY() + yAdj);

		return new GeneralTrilaterationModel(receiver1, receiver2, receiver3);
	}

}
