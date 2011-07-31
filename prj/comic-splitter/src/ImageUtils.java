import java.awt.Color;

/**
 * Utilitários para o {@link IImage}
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
class ImageUtils {

	static final int	THRESHOLD		= 40;

	static final int	IMAGE_OPERATOR	= 0xff;
	static final int	WHITE			= Color.WHITE.getRGB();	// 0xffffffff;
	static final int	BLACK			= Color.BLACK.getRGB();
	static final int	GREEN			= Color.GREEN.getRGB();
	static final int	BLUE			= Color.BLUE.getRGB();
	static final int	YELLOW			= Color.YELLOW.getRGB();

	static void threshold(IImage original) {
		for (int x = 0; x < original.getWidth(); x++) {
			for (int y = 0; y < original.getHeight(); y++) {
				if ((original.getPixel(x, y) & IMAGE_OPERATOR) >= THRESHOLD) {
					original.setPixel(x, y, WHITE);
				} else {
					original.setPixel(x, y, BLACK);
				}
			}
		}
	}

	static boolean isWhite(int value) {
		return value == WHITE;
	}

	static int whiteCount(IImage image) {
		int whiteCount = 0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (isWhite(image.getPixel(i, j))) {
					whiteCount++;
				}
			}
		}
		return whiteCount;
	}

}
