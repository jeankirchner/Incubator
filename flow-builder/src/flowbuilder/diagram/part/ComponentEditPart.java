package flowbuilder.diagram.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import flowbuilder.diagram.model.ActivityComponent;
import flowbuilder.diagram.model.BeginComponent;
import flowbuilder.diagram.model.Component;
import flowbuilder.diagram.model.Connection;
import flowbuilder.diagram.model.DecisionComponent;
import flowbuilder.diagram.model.EndComponent;
import flowbuilder.diagram.model.ForkComponent;
import flowbuilder.diagram.model.ModelElement;
import flowbuilder.diagram.model.NoteComponent;
import flowbuilder.diagram.model.TextComponent;
import flowbuilder.diagram.model.commands.ConnectionCreateCommand;
import flowbuilder.diagram.model.commands.ConnectionReconnectCommand;

public abstract class ComponentEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart {

	private ConnectionAnchor anchor;

	/**
	 * Upon activation, attach to the model element as a property change
	 * listener.
	 */
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
	// allow the creation of connections and
	// and the reconnection of connections between Shape instances
	installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy() {
		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
		 * getConnectionCompleteCommand
		 * (org.eclipse.gef.requests.CreateConnectionRequest)
		 */
		protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
			ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
			cmd.setTarget((Component) getHost().getModel());
			return cmd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
		 * getConnectionCreateCommand
		 * (org.eclipse.gef.requests.CreateConnectionRequest)
		 */
		protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
			Component source = (Component) getHost().getModel();
			int style = ((Integer) request.getNewObjectType()).intValue();
			ConnectionCreateCommand cmd = new ConnectionCreateCommand(source, style);
			request.setStartCommand(cmd);
			return cmd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
		 * getReconnectSourceCommand
		 * (org.eclipse.gef.requests.ReconnectRequest)
		 */
		protected Command getReconnectSourceCommand(ReconnectRequest request) {
			Connection conn = (Connection) request.getConnectionEditPart().getModel();
			Component newSource = (Component) getHost().getModel();
			ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
			cmd.setNewSource(newSource);
			return cmd;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
		 * getReconnectTargetCommand
		 * (org.eclipse.gef.requests.ReconnectRequest)
		 */
		protected Command getReconnectTargetCommand(ReconnectRequest request) {
			Connection conn = (Connection) request.getConnectionEditPart().getModel();
			Component newTarget = (Component) getHost().getModel();
			ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
			cmd.setNewTarget(newTarget);
			return cmd;
		}
	});
}

	/**
	 * Upon deactivation, detach from the model element as a property change
	 * listener.
	 */
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((ModelElement) getModel()).removePropertyChangeListener(this);
		}
	}

	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			if (getModel() instanceof BeginComponent
					|| getModel() instanceof DecisionComponent
					|| getModel() instanceof EndComponent
					|| getModel() instanceof TextComponent)
				anchor = new EllipseAnchor(getFigure());
			else if (getModel() instanceof ActivityComponent
					|| getModel() instanceof ForkComponent
					|| getModel() instanceof NoteComponent)
				anchor = new ChopboxAnchor(getFigure());
			else
				// if Shapes gets extended the conditions above must be updated
				throw new IllegalArgumentException("unexpected model");
		}
		return anchor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections
	 * ()
	 */
	protected List getModelSourceConnections() {
		return getCastedModel().getSourceConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections
	 * ()
	 */
	protected List getModelTargetConnections() {
		return getCastedModel().getTargetConnections();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (Component.SIZE_PROP.equals(prop) || Component.LOCATION_PROP.equals(prop)) {
			refreshVisuals();
		} else if (Component.SOURCE_CONNECTIONS_PROP.equals(prop)) {
			refreshSourceConnections();
		} else if (Component.TARGET_CONNECTIONS_PROP.equals(prop)) {
			refreshTargetConnections();
		}
	}
	
	private Component getCastedModel() {
		return (Component) getModel();
	}


	protected void refreshVisuals() {
		// notify parent container of changed position & location
		// if this line is removed, the XYLayoutManager used by the parent
		// container
		// (the Figure of the ShapesDiagramEditPart), will not know the bounds
		// of this figure
		// and will not draw it correctly.
		Rectangle bounds = new Rectangle(getCastedModel().getLocation(), getCastedModel().getSize());
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
	}
	
}
