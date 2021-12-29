package flowbuilder.diagram.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import flowbuilder.diagram.model.ActivityComponent;
import flowbuilder.diagram.model.BeginComponent;
import flowbuilder.diagram.model.Component;
import flowbuilder.diagram.model.DecisionComponent;
import flowbuilder.diagram.model.EndComponent;
import flowbuilder.diagram.model.FlowDiagram;
import flowbuilder.diagram.model.ForkComponent;
import flowbuilder.diagram.model.ModelElement;
import flowbuilder.diagram.model.NoteComponent;
import flowbuilder.diagram.model.TextComponent;
import flowbuilder.diagram.model.commands.ComponentCreateCommand;
import flowbuilder.diagram.model.commands.ComponentSetConstraintCommand;

public class DiagramEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener{

	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}
	
	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));
		f.setLayoutManager(new FreeformLayout());

		// Create the static router for the connection layer
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new ShortestPathConnectionRouter(f));
		
		return f;
	}

	@Override
	protected void createEditPolicies() {
		// handles constraint changes (e.g. moving and/or resizing) of model elements
		// and creation of new model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE,  new ComponentXYLayoutEditPolicy());
	}
	
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}
	
	private FlowDiagram getCastedModel() {
		return (FlowDiagram) getModel();
	}
	
	protected List getModelChildren() {
		return getCastedModel().getChildren(); // return a list of shapes
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		// these properties are fired when Shapes are added into or removed from 
		// the ShapeDiagram instance and must cause a call of refreshChildren()
		// to update the diagram's contents.
		if (FlowDiagram.CHILD_ADDED_PROP.equals(prop)) {
			refreshChildren();
		}		
	}

	private static class ComponentXYLayoutEditPolicy extends XYLayoutEditPolicy {
		
		/* (non-Javadoc)
		 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest, EditPart, Object)
		 */
		protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
				EditPart child, Object constraint) {
			if ((child instanceof ActivityEditPart //
					|| child instanceof BeginEditPart //
					|| child instanceof DecisionEditPart //
					|| child instanceof EndEditPart //
					|| child instanceof ForkEditPart //
					|| child instanceof NoteEditPart //
					|| child instanceof TextEditPart) //
					&& constraint instanceof Rectangle) {
				// return a command that can move and/or resize a Shape
				return new ComponentSetConstraintCommand(
						(Component) child.getModel(), request, (Rectangle) constraint);
			}
			return super.createChangeConstraintCommand(request, child, constraint);
		}
		
		/* (non-Javadoc)
		 * @see ConstrainedLayoutEditPolicy#createChangeConstraintCommand(EditPart, Object)
		 */
		protected Command createChangeConstraintCommand(EditPart child,
				Object constraint) {
			return null;
		}
		
		/* (non-Javadoc)
		 * @see LayoutEditPolicy#getCreateCommand(CreateRequest)
		 */
		protected Command getCreateCommand(CreateRequest request) {
			Object childClass = request.getNewObjectType();
			if (childClass == ActivityComponent.class //
					|| childClass == BeginComponent.class //
					|| childClass == DecisionComponent.class //
					|| childClass == EndComponent.class //
					|| childClass == ForkComponent.class //
					|| childClass == NoteComponent.class //
					|| childClass == TextComponent.class) {
				// return a command that can add a Shape to a ShapesDiagram 
				return new ComponentCreateCommand((Component)request.getNewObject(), 
						(FlowDiagram)getHost().getModel(), (Rectangle)getConstraintFor(request));
			}
			return null;
		}
		
	}

	
}
