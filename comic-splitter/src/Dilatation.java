/**
 * Implementação do operador morfológico de dilatação.
 * http://en.wikipedia.org/wiki/Mathematical_morphology#Dilation
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
class Dilatation {
	static IImage dilate(IImage image) {
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (image.getPixel(i, j) == ImageUtils.WHITE) {
					if (i > 0 && image.getPixel(i - 1, j) == ImageUtils.BLACK) {
						image.setPixel(i - 1, j, 2);
					}
					if (j > 0 && image.getPixel(i, j - 1) == ImageUtils.BLACK) {
						image.setPixel(i, j - 1, 2);
					}
					if (i + 1 < image.getWidth() && image.getPixel(i + 1, j) == ImageUtils.BLACK) {
						image.setPixel(i + 1, j, 2);
					}
					if (j + 1 < image.getHeight() && image.getPixel(i, j + 1) == ImageUtils.BLACK) {
						image.setPixel(i, j + 1, 2);
					}
				}
			}
		}

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (image.getPixel(i, j) == 2) {
					image.setPixel(i, j, ImageUtils.WHITE);
				}
			}
		}
		return image;
	}
}