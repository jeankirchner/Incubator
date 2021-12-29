package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * Note component
 * @author Felipe
 */
public class NoteComponent extends Component{

	/** A 16x16 pictogram of a note component. */
	private static final Image NOTE_ICON = createImage("icons/Note16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return NOTE_ICON;
	}

	public String toString() {
		return "Note " + hashCode();
	}
	
}
