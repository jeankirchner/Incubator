import java.util.ArrayList;
import java.util.List;
/**
 * Implementação falha e "inocente" para seguir bordas na imagem.
 * Muitos bugs e situações não previstas pois parou-se de desenvoler.
 * Mantido por curiosidade.
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
public class BorderSeeker {

	static void paintPaths(IImage image, List<List<Point>> paths) {
		for (List<Point> path : paths) {
			for (Point p : path) {
				image.setPixel(p.x, p.y, ImageUtils.GREEN);
			}
		}
	}

	static List<List<Point>> followPath(IImage image) {
		boolean[] visited = new boolean[image.getWidth() * image.getHeight()];
		List<List<Point>> paths = new ArrayList<List<Point>>();

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {

				if (!visited[y * image.getWidth() + x] && ImageUtils.isWhite(image.getPixel(x, y))) {
					List<Point> path = getPath(x, y, image, visited);
					paths.add(path);
				}

			}
		}

		return paths;

	}

	private static List<Point> getPath(int x, int y, IImage image, boolean[] visited) {
		List<Point> path = new ArrayList<Point>();
		Point current = pt(x, y);
		path.add(current);
		while ((current = next(current, visited, image)) != null) {
			path.add(current);
		}
		return path;
	}

	private static int	precision	= 1;

	private static Point next(Point current, boolean[] visited, IImage image) {
		int[] bytes = image.getData();
		for (int i = -precision; i <= precision; i++) {
			for (int j = -precision; j <= precision; j++) {
				int x = current.x + i;
				int y = current.y + j;
				if (image.hasPixel(x, y) && !visited[y * image.getWidth() + x] && ImageUtils.isWhite(bytes[y * image.getWidth() + x])) {
					visited[y * image.getWidth() + x] = true;
					return pt(x, y);
				}
			}
		}
		return null;
	}

	private static Point pt(int x, int y) {
		Point p = new Point();
		p.x = x;
		p.y = y;
		return p;
	}

}
