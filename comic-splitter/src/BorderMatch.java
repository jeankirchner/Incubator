/**
 * Classe para achar bordas em ângulos retos, com precisão de acerto.
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
public class BorderMatch {

	private static boolean isBorderMatched(double bound, int whiteCount, float precision) {
		return ((whiteCount / bound) >= precision);
	}

	private static boolean isValidBorderPosition(double position, double bound, float error) {
		double res = (position / bound);
		return res >= error && res <= 1 - error;
	}

	static int findFirstBorderInX(IImage image, Direction direction, float precisionOfMatch, float error) {
		if (Direction.ASCENDING == direction) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (!isValidBorderPosition(x, image.getOriginalImageWidth(), error)) {
					continue;
				}
				int xWhiteCount = 0;
				for (int y = 0; y < image.getHeight(); y++) {
					if (ImageUtils.isWhite(image.getPixel(x, y))) {
						xWhiteCount++;
					}
				}
				if (isBorderMatched(image.getHeight(), xWhiteCount, precisionOfMatch)) {
					return x;
				}
			}
			return -1;
		} else {
			for (int x = image.getWidth() - 1; x >= 0; x--) {
				if (!isValidBorderPosition(x, image.getOriginalImageWidth(), error)) {
					continue;
				}
				int xWhiteCount = 0;
				for (int y = 0; y < image.getHeight(); y++) {
					if (ImageUtils.isWhite(image.getPixel(x, y))) {
						xWhiteCount++;
					}
				}
				if (isBorderMatched(image.getHeight(), xWhiteCount, precisionOfMatch)) {
					return x;
				}
			}
			return -1;
		}
	}

	static int findFirstBorderInY(IImage image, Direction direction, float precisionOfMatch, float error) {
		if (Direction.ASCENDING == direction) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (!isValidBorderPosition(y, image.getOriginalImageHeight(), error)) {
					continue;
				}
				int whiteCount = 0;
				for (int x = 0; x < image.getWidth(); x++) {
					if (ImageUtils.isWhite(image.getPixel(x, y))) {
						whiteCount++;
					}
				}
				if (isBorderMatched(image.getWidth(), whiteCount, precisionOfMatch)) {
					return y;
				}
			}
			return -1;
		} else {
			for (int y = image.getHeight() - 1; y >= 0; y--) {
				if (!isValidBorderPosition(y, image.getOriginalImageHeight(), error)) {
					continue;
				}
				int whiteCount = 0;
				for (int x = 0; x < image.getWidth(); x++) {
					if (ImageUtils.isWhite(image.getPixel(x, y))) {
						whiteCount++;
					}
				}
				if (isBorderMatched(image.getWidth(), whiteCount, precisionOfMatch)) {
					return y;
				}
			}
			return -1;
		}
	}

}
