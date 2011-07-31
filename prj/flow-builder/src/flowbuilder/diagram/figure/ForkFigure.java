package flowbuilder.diagram.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

public class ForkFigure extends RectangleFigure{

	@Override
	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds();
		graphics.setBackgroundColor(ColorConstants.black);
		graphics.fillRectangle(bounds);
	}
	
}
