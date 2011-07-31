/**
 * Representa um simples ponto 2d
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */
class Point {
	int	x;
	int	y;

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}

}