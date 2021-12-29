package flowbuilder.diagram.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class DecisionFigure extends Shape {

	public DecisionFigure() {
		//setBorder(new LineBorder(ColorConstants.white, 0));
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds();
		graphics.setBackgroundColor(new Color(null, new RGB(250, 250, 141)));
		graphics.setForegroundColor(ColorConstants.white);
		graphics.fillPolygon(new int[]{bounds.x + (bounds.width / 2), //
										bounds.y+1, //
										bounds.x+1, //
										bounds.y + (bounds.height / 2), //
										bounds.x + (bounds.width / 2), //
										bounds.y-2 + bounds.height, //
										bounds.x-2 + bounds.width, //
										bounds.y + (bounds.height / 2)});
		graphics.setBackgroundColor(ColorConstants.black);
		graphics.setForegroundColor(ColorConstants.black);
		graphics.drawPolygon(new int[]{bounds.x + (bounds.width / 2), //
				bounds.y+1, //
				bounds.x+1, //
				bounds.y + (bounds.height / 2), //
				bounds.x + (bounds.width / 2), //
				bounds.y-2 + bounds.height, //
				bounds.x-2 + bounds.width, //
				bounds.y + (bounds.height / 2)});
		
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}
	
}
