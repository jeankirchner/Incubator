import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementação genérica do algoritmo para encontrar quadrinhos.
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
public class ComicDetection {

	public static final float	COMIC_PRECISION	= 0.98f;

	static <T> List<T> createList() {
		return new ArrayList<T>();
	}

	static <T> List<T> createLinkedList() {
		return new LinkedList<T>();
	}

	static IImage detectComics(IImage originalImage) {

		Sobel.detectEdges(originalImage);
		
		//descomentar as partes abaixo para ver os resultados de cada algoritmo específico
//		if (true) {
//			return originalImage;
//		}
		
		ImageUtils.threshold(originalImage);
		
//		if (true) {
//			return originalImage;
//		}
		
		originalImage = adjustBorderFailures(originalImage);
		
//		if (true) {
//			return originalImage;
//		}

		List<IImage> finishedComics = createList();
		List<IImage> horizontalPending = createLinkedList();
		List<IImage> verticalPending = createLinkedList();

		horizontalPending.add(ImageCropUtils.cropBoundingBox(originalImage, .8f));

		while (horizontalPending.size() > 0 || verticalPending.size() > 0) {
			while (horizontalPending.size() > 0) {
				IImage img = horizontalPending.remove(0);

				img = ImageCropUtils.cropBoundingBox(img, .8f);

				int xBorder = BorderMatch.findFirstBorderInX(img, Direction.ASCENDING, COMIC_PRECISION, .1f);

				if (xBorder != -1) {
					List<IImage> croppedImages = ImageCropUtils.splitByXAxis(img, xBorder);
					if (croppedImages.size() == 0) {
						verticalPending.add(img);
					} else {
						verticalPending.addAll(croppedImages);
					}
				} else {
					verticalPending.add(img);
				}

			}

			while (verticalPending.size() > 0) {

				IImage img = verticalPending.remove(0);
				img = ImageCropUtils.cropBoundingBox(img, .8f);

				int yBorder = BorderMatch.findFirstBorderInY(img, Direction.ASCENDING, COMIC_PRECISION, .1f);

				if (yBorder != -1) {
					List<IImage> croppedImages = ImageCropUtils.splitByYAxis(img, yBorder);
					if (croppedImages.size() == 0) {
						finishedComics.add(img);
					}
					horizontalPending.addAll(croppedImages);
				} else {
					finishedComics.add(img);
				}
			}
		}
		paintImagesBorder(finishedComics, ImageUtils.BLUE);

		return originalImage;

	}

	public static IImage adjustBorderFailures(IImage image) {
		image = Dilatation.dilate(image);
		return image;
	}

	private static void fillColor(List<IImage> images, int color) {
		for (IImage img : images) {
			for (int i = 0; i < img.getWidth(); i++) {
				for (int j = 0; j < img.getHeight(); j++) {
					img.setPixel(i, j, color);
				}
			}
		}
	}

	private static void paintImagesBorder(List<IImage> images, int color) {
		for (IImage img : images) {
			for (int i = 0; i < img.getWidth(); i++) {
				img.setPixel(i, 0, color);
				img.setPixel(i, img.getHeight() - 1, color);
			}
			for (int i = 0; i < img.getHeight(); i++) {
				img.setPixel(0, i, color);
				img.setPixel(img.getWidth() - 1, i, color);
			}
		}

	}

	private static void paintEdges(List<Integer> xEdges, List<Integer> yEdges, IImage image) {
		if (xEdges != null) {
			for (int i = 0; i < xEdges.size(); i++) {
				int x = xEdges.get(i);
				for (int y = 0; y < image.getHeight(); y++) {
					image.setPixel(x, y, ImageUtils.GREEN);
				}
			}
		}
		if (yEdges != null) {
			for (int i = 0; i < yEdges.size(); i++) {
				int y = yEdges.get(i);
				for (int x = 0; x < image.getWidth(); x++) {
					image.setPixel(x, y, ImageUtils.GREEN);
				}
			}
		}
	}

}
