class Rectangle {

	int	x;
	int	y;
	int	width;
	int	height;

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		Rectangle other = (Rectangle) obj;
		return x == other.x && y == other.y && width == other.width && height == other.height;
	}

}