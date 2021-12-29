package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * Text Component
 * @author Felipe
 */
public class TextComponent extends Component{

	/** A 16x16 pictogram of a note component. */
	private static final Image TEXT_ICON = createImage("icons/Text16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return TEXT_ICON;
	}

	public String toString() {
		return "Text " + hashCode();
	}

}
