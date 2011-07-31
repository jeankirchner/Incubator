/**
 * Representa uma "VIEW" lógica de uma imagem real para otimizar cópia de arrays.
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
public class Snapshot implements IImage {

	private final int		originalImageWidth;
	private final int		originalImageHeight;

	private final IImage	image;
	private final int		height;
	private final int		width;
	private final int		y;
	private final int		x;

	public Snapshot(IImage image, int x, int y, int width, int height, int originalWidth, int originalHeight) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = image;

		originalImageHeight = originalHeight;
		originalImageWidth = originalWidth;
	}

	@Override
	public void setData(int[] data) {
		if (data.length != width * height) {
			throw new IllegalArgumentException("data does not have the right size");
		}
		ImageData img = new ImageData(this.width, this.height, data);
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				this.setPixel(i, j, img.getPixel(i, j));
			}
		}
	}

	@Override
	public void setData(int[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				setPixel(i, j, data[i][j]);
			}
		}
	}

	@Override
	public boolean isImageLimit(int x, int y) {
		return (x == 0 && y == 0) || (width == x && height == y);
	}

	protected void check(int x, int y) {
		if (!hasPixel(x, y)) {
			throw new IllegalArgumentException("X can't be greater than width nor y can be greater than height");
		}
	}

	@Override
	public boolean hasPixel(int x, int y) {
		return x >= 0 && y >= 0 && x <= width && y <= height;
	}

	@Override
	public void setPixel(int x, int y, int value) {
		check(x, y);
		image.setPixel(x + this.x, y + this.y, value);
	}

	@Override
	public int getPixel(int x, int y) {
		check(x, y);
		return image.getPixel(this.x + x, this.y + y);
	}

	@Override
	public int[] getData() {
		ImageData newImage = new ImageData(width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				newImage.setPixel(i, j, this.getPixel(i, j));
			}
		}
		return newImage.getData();
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public IImage snapshot(int x, int y, int width, int height) {
		return new Snapshot(this, x, y, width, height, originalImageWidth, originalImageHeight);
	}

	@Override
	public double proportionOfOriginalImage() {
		return ((getWidth() * getHeight()) / (double) (image.getWidth() * image.getHeight()));
	}

	@Override
	public int getOriginalImageWidth() {
		return this.originalImageWidth;
	}

	@Override
	public int getOriginalImageHeight() {
		return this.originalImageHeight;
	}

}
