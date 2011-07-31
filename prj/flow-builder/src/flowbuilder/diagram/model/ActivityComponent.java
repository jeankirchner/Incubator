package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * Activity component
 * @author Felipe
 */
public class ActivityComponent extends Component {

	/** A 16x16 pictogram of a activity component. */
	private static final Image ACTIVITY_ICON = createImage("icons/Activity16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return ACTIVITY_ICON;
	}

	public String toString() {
		return "Activity " + hashCode();
	}
	
}
