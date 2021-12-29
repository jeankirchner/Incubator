import java.util.ArrayList;
import java.util.List;

/**
 * Utilitários para corte de imagens e gerar {@link Snapshot}
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
public class ImageCropUtils {

	static IImage cropByRect(IImage image, int xMin, int xMax, int yMin, int yMax) {
		return image.snapshot(xMin, yMin, xMax - xMin, yMax - yMin);
	}

	static IImage[] cropInXAxis(IImage image, int xCut) {
		IImage newImageUp = new ImageData(xCut, image.getHeight());
		IImage newImageDown = new ImageData(image.getWidth() - xCut, image.getHeight());
		for (int x = 0; x < xCut; x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				newImageUp.setPixel(x, y, image.getPixel(x, y));
			}
		}
		for (int x = xCut; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				newImageUp.setPixel(x - xCut, y, image.getPixel(x, y));
			}
		}
		return new IImage[] { newImageUp, newImageDown };
	}

	static IImage[] cropInYAxis(IImage image, int yCut) {
		IImage newImageUp = new ImageData(image.getWidth(), yCut);
		IImage newImageDown = new ImageData(image.getWidth(), image.getHeight() - yCut);

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < yCut; y++) {
				newImageUp.setPixel(x, y, image.getPixel(x, y));
			}
		}

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = yCut; y < image.getHeight(); y++) {
				newImageUp.setPixel(x, y - yCut, image.getPixel(x, y));
			}
		}

		return new IImage[] { newImageUp, newImageDown };
	}

	public static List<IImage> splitByYAxis(IImage image, int yBorder) {
		if (yBorder == -1) {
			return null;
		}
		List<IImage> images = new ArrayList<IImage>();
		images.add(image.snapshot(0, 0, image.getWidth(), yBorder));
		images.add(image.snapshot(0, yBorder, image.getWidth(), image.getHeight() - yBorder));
		return images;
	}

	public static List<IImage> splitByXAxis(IImage image, int xBorder) {
		if (xBorder == -1) {
			return null;
		}
		List<IImage> images = new ArrayList<IImage>(2);
		images.add(image.snapshot(0, 0, xBorder, image.getHeight()));
		images.add(image.snapshot(xBorder, 0, image.getWidth() - xBorder, image.getHeight()));
		return images;
	}

	public static IImage cropBoundingBox(IImage image, float comicPrecision) {
		int xMin = BorderMatch.findFirstBorderInX(image, Direction.ASCENDING, comicPrecision, 0);
		int xMax = BorderMatch.findFirstBorderInX(image, Direction.DESCENDING, comicPrecision, 0);

		int yMin = BorderMatch.findFirstBorderInY(image, Direction.ASCENDING, comicPrecision, 0);
		int yMax = BorderMatch.findFirstBorderInY(image, Direction.DESCENDING, comicPrecision, 0);

		if (xMin == -1 || ((xMin / (float) image.getWidth()) > .1)) {
			xMin = 0;
		}
		if (xMax == -1 || ((xMax / (float) image.getWidth()) < .9)) {
			xMax = image.getWidth();
		}

		if (yMin == -1 || ((yMin / (float) image.getHeight()) > .1)) {
			yMin = 0;
		}

		if (yMax == -1 || ((yMax / (float) image.getHeight()) < .9)) {
			yMax = image.getHeight();
		}

		return cropByRect(image, xMin, xMax, yMin, yMax);
	}
}
