package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * Begin component
 * @author Felipe
 */
public class BeginComponent extends Component{

	/** A 16x16 pictogram of a begin component. */
	private static final Image BEGIN_ICON = createImage("icons/Begin16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return BEGIN_ICON;
	}

	public String toString() {
		return "Begin " + hashCode();
	}
	
}
