/**
 * Interfaceamento simples para representar uma imagem
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
public interface IImage {

	void setData(int[] data);

	boolean isImageLimit(int x, int y);

	boolean hasPixel(int x, int y);

	void setPixel(int x, int y, int value);

	int getPixel(int x, int y);

	int[] getData();

	int getWidth();

	int getHeight();

	IImage snapshot(int x, int y, int width, int height);

	double proportionOfOriginalImage();

	void setData(int[][] pixel2);

	int getOriginalImageWidth();

	int getOriginalImageHeight();

}