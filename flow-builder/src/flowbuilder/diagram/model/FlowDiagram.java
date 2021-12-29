package flowbuilder.diagram.model;

import java.util.ArrayList;
import java.util.List;


public class FlowDiagram extends ModelElement{

	/** Property ID to use when a child is added to this diagram. */
	public static final String CHILD_ADDED_PROP = "FlowDiagram.ChildAdded";
	
	private static final long serialVersionUID = 1;
	
	@SuppressWarnings("unchecked")
	private List childrens = new ArrayList();

	/** 
	 * Add a shape to this diagram.
	 * @param c a non-null shape instance
	 * @return true, if the shape was added, false otherwise
	 */
	@SuppressWarnings("unchecked")
	public boolean addChild(Component c) {
		if (c != null && childrens.add(c)) {
			firePropertyChange(CHILD_ADDED_PROP, null, c);
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List getChildren() {
		return childrens;
	}
	
}
