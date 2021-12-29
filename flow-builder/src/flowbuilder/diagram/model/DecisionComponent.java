package flowbuilder.diagram.model;

import org.eclipse.swt.graphics.Image;

/**
 * Decision component
 * @author Felipe
 */
public class DecisionComponent extends Component{

	/** A 16x16 pictogram of a decision component. */
	private static final Image DECISION_ICON = createImage("icons/Decision16.gif");

	private static final long serialVersionUID = 1;

	public Image getIcon() {
		return DECISION_ICON;
	}

	public String toString() {
		return "Decision " + hashCode();
	}

	
}
