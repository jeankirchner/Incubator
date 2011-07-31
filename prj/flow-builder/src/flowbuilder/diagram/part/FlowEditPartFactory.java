package flowbuilder.diagram.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import flowbuilder.diagram.model.ActivityComponent;
import flowbuilder.diagram.model.BeginComponent;
import flowbuilder.diagram.model.Connection;
import flowbuilder.diagram.model.DecisionComponent;
import flowbuilder.diagram.model.EndComponent;
import flowbuilder.diagram.model.FlowDiagram;
import flowbuilder.diagram.model.ForkComponent;
import flowbuilder.diagram.model.NoteComponent;
import flowbuilder.diagram.model.TextComponent;

public class FlowEditPartFactory implements EditPartFactory{

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object modelElement) {
		// get EditPart for model element
		EditPart part = getPartForElement(modelElement);
		// store model element in EditPart
		part.setModel(modelElement);
		return part;
	}
	
	/**
	 * Maps an object to an EditPart. 
	 * @throws RuntimeException if no match was found (programming error)
	 */
	private EditPart getPartForElement(Object modelElement) {
		if (modelElement instanceof FlowDiagram) {
			return new DiagramEditPart();
		}
		if (modelElement instanceof ActivityComponent) {
			return new ActivityEditPart();
		}
		if (modelElement instanceof BeginComponent) {
			return new BeginEditPart();
		}
		if (modelElement instanceof DecisionComponent) {
			return new DecisionEditPart();
		}
		if (modelElement instanceof EndComponent) {
			return new EndEditPart();
		}
		if (modelElement instanceof ForkComponent) {
			return new ForkEditPart();
		}
		if (modelElement instanceof NoteComponent) {
			return new NoteEditPart();
		}
		if (modelElement instanceof TextComponent) {
			return new TextEditPart();
		}
		if (modelElement instanceof Connection) {
			return new ConnectionEditPart();
		}

		throw new RuntimeException(
				"Can't create part for model element: "
				+ ((modelElement != null) ? modelElement.getClass().getName() : "null"));
	}
	
}
