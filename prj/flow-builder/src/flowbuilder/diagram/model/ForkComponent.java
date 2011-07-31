package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * Fork component
 * @author Felipe
 *
 */
public class ForkComponent extends Component{

	/** A 16x16 pictogram of a fork component. */
	private static final Image FORK_ICON = createImage("icons/Fork16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return FORK_ICON;
	}

	public String toString() {
		return "Fork " + hashCode();
	}
	
}
