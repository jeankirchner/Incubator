package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * End component
 * @author Felipe
 */
public class EndComponent extends Component{

	/** A 16x16 pictogram of a end component. */
	private static final Image END_ICON = createImage("icons/End16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return END_ICON;
	}

	public String toString() {
		return "End " + hashCode();
	}
	
}
