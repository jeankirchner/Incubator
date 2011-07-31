package flowbuilder.diagram.model.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import flowbuilder.diagram.model.Component;
import flowbuilder.diagram.model.FlowDiagram;

public class ComponentCreateCommand extends Command{
	
	/** The new component. */ 
	private Component newComponent;
	/** FlowDiagram to add to. */
	private final FlowDiagram parent;
	/** The bounds of the new component. */
	private Rectangle bounds;

	/**
	 * Create a command that will add a new component to a FlowDiagram.
	 * @param newComponent the new Component that is to be added
	 * @param parent the FlowDiagram that will hold the new element
	 * @param bounds the bounds of the new component; the size can be (-1, -1) if not known
	 * @throws IllegalArgumentException if any parameter is null, or the request
	 * 						  does not provide a new component instance
	 */
	public ComponentCreateCommand(Component newComponent, FlowDiagram parent, Rectangle bounds) {
		this.newComponent = newComponent;
		this.parent = parent;
		this.bounds = bounds;
		setLabel("component creation");
	}

	/**
	 * Can execute if all the necessary information has been provided. 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return newComponent != null && parent != null && bounds != null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		newComponent.setLocation(bounds.getLocation());
		Dimension size = bounds.getSize();
		if (size.width > 0 && size.height > 0)
			newComponent.setSize(size);
		redo();
	}

}
