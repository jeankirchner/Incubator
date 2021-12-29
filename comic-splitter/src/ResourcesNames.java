import java.util.ArrayList;
import java.util.List;

public class ResourcesNames {

	private static int			index			= -1;
	private static List<String>	resourceNames	= new ArrayList<String>();

	static {
		resourceNames.add("1.png");
		resourceNames.add("2.png");
		resourceNames.add("3.png");
		resourceNames.add("4.png");
		resourceNames.add("5.png");

		resourceNames.add("1.jpg");
		resourceNames.add("2.jpg");
		resourceNames.add("3.jpg");
		resourceNames.add("4.jpg");
		resourceNames.add("5.jpg");
		resourceNames.add("6.jpg");
		resourceNames.add("7.jpg");
		resourceNames.add("8.jpg");
		resourceNames.add("9.jpg");
		resourceNames.add("10.jpg");
		resourceNames.add("11.jpg");
		resourceNames.add("12.jpg");
		resourceNames.add("13.jpg");
		resourceNames.add("14.jpg");
		resourceNames.add("15.jpg");
		resourceNames.add("16.jpg");
		resourceNames.add("17.jpg");
		resourceNames.add("18.jpg");
		resourceNames.add("19.jpg");
		resourceNames.add("20.jpg");
	}

	public static List<String> getResourcenames() {
		return resourceNames;
	}

	public static boolean hasNext() {
		return index < resourceNames.size() - 1;
	}

	public static boolean hasPrev() {
		return resourceNames.size() > 0 && index > 0;
	}

	public static String next() {
		return resourceNames.get(++index);
	}

	public static String prev() {
		return resourceNames.get(--index);
	}

}
