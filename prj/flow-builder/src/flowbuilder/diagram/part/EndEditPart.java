package flowbuilder.diagram.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
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

import flowbuilder.diagram.figure.EndFigure;
import flowbuilder.diagram.model.Component;
import flowbuilder.diagram.model.Connection;
import flowbuilder.diagram.model.EndComponent;
import flowbuilder.diagram.model.ModelElement;
import flowbuilder.diagram.model.commands.ConnectionCreateCommand;
import flowbuilder.diagram.model.commands.ConnectionReconnectCommand;

public class EndEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, NodeEditPart{

	private ConnectionAnchor anchor;
	
	public void activate() {
		if (!isActive()) {
			super.activate();
			((ModelElement) getModel()).addPropertyChangeListener(this);
		}
	}
	
	@Override
	protected IFigure createFigure() {
		IFigure f = new EndFigure();
		f.setOpaque(true); // non-transparent figure
		f.setBackgroundColor(ColorConstants.black);
		return f;
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
	
	private EndComponent getCastedModel() {
		return (EndComponent) getModel();
	}
	
	protected ConnectionAnchor getConnectionAnchor() {
		if (anchor == null) {
			anchor = new EllipseAnchor(getFigure());
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
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return getConnectionAnchor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef
	 * .Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
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

	
	@Override
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
}
