/**
 * Representa uma imagem tendo cada píxel como um inteiro
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
class ImageData implements IImage {

	private int[]		data;
	private final int	height;
	private final int	width;

	ImageData(int width, int height) {
		this(width, height, new int[width * height]);
	}

	ImageData(int width, int height, int[] data) {
		this.width = width;
		this.height = height;
		setData(data);
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int[] getData() {
		return data;
	}

	@Override
	public int getPixel(int x, int y) {
		check(x, y);
		return this.data[y * width + x];
	}

	protected void check(int x, int y) {
		if (!hasPixel(x, y)) {
			throw new IllegalArgumentException("X can't be greater than width nor y can be greater than height");
		}
	}

	@Override
	public void setPixel(int x, int y, int value) {
		check(x, y);
		this.data[y * width + x] = value;
	}

	@Override
	public boolean hasPixel(int x, int y) {
		return x >= 0 && y >= 0 && x < width && y < height;
	}

	@Override
	public boolean isImageLimit(int x, int y) {
		return x == 0 || y == 0 || x == width || y == height;
	}

	@Override
	public void setData(int[] data) {
		if (data.length != width * height) {
			throw new IllegalArgumentException("data does not have the right size");
		}
		this.data = data;
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
	public IImage snapshot(int x, int y, int width, int height) {
		return new Snapshot(this, x, y, width, height, this.width, this.height);
	}

	@Override
	public double proportionOfOriginalImage() {
		return 1;
	}

	@Override
	public int getOriginalImageWidth() {
		return getWidth();
	}

	@Override
	public int getOriginalImageHeight() {
		return getHeight();
	}
}
