package flowbuilder.diagram.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class EndFigure extends Ellipse{

	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds();
		
		graphics.setBackgroundColor(new Color(null, new RGB(250, 250, 141)));
		graphics.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		
		graphics.setBackgroundColor(ColorConstants.black);
		int sizeX = (int) (((bounds.x + bounds.width) - bounds.x) * 0.2);
		int sizeY = (int) (((bounds.y + bounds.height) - bounds.y) * 0.2);
		graphics.fillOval(bounds.x + sizeX, bounds.y + sizeY, bounds.width - (sizeX * 2), bounds.height - (sizeY * 2));
	};
	
	
}
