/**
 * Implementação do operador de erosão para processamento de imagens
 * http://en.wikipedia.org/wiki/Mathematical_morphology#Erosion
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
class Erosion {
	public static IImage erode(IImage image) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		ImageData imageDestine = new ImageData(image.getWidth(), image.getHeight());

		for (int x = 1; x < imageWidth - 1; x++) {
			for (int y = 1; y < imageHeight - 1; y++) {
				if (image.getPixel(x - 1, y) != ImageUtils.WHITE
						|| image.getPixel(x + 1, y) != ImageUtils.WHITE
						|| image.getPixel(x, y - 1) != ImageUtils.WHITE
						|| image.getPixel(x, y + 1) != ImageUtils.WHITE) {
					imageDestine.setPixel(x, y, ImageUtils.BLACK);
				} else {
					imageDestine.setPixel(x, y, ImageUtils.WHITE);
				}
			}
		}

		return imageDestine;
	}
}