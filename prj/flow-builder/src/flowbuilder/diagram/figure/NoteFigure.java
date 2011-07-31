package flowbuilder.diagram.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class NoteFigure extends Shape{

	private String text = "";
	
	public NoteFigure() {
	}
	
	public NoteFigure(String text) {
		this.text = text;
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		Rectangle bounds = getBounds();
		graphics.setBackgroundColor(new Color(null, new RGB(250, 250, 141)));
		graphics.fillRectangle(bounds);
		graphics.setBackgroundColor(ColorConstants.black);
		graphics.drawLine(bounds.x + bounds.width - 10, bounds.y, bounds.x + bounds.width, bounds.y + 10);
		graphics.drawLine(bounds.x + bounds.width - 10, bounds.y, bounds.x + bounds.width - 10, bounds.y + 10);
		graphics.drawLine(bounds.x + bounds.width - 10, bounds.y + 10, bounds.x + bounds.width, bounds.y + 10);

		graphics.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y);
		graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height);
		graphics.drawLine(bounds.x + bounds.width -1, bounds.y + bounds.height, bounds.x + bounds.width -1, bounds.y);
		graphics.drawLine(bounds.x + bounds.width, bounds.y + bounds.height -1, bounds.x, bounds.y + bounds.height - 1);
		
		graphics.setBackgroundColor(ColorConstants.white);
		graphics.fillPolygon(new int[]{bounds.x + bounds.width - 9, bounds.y, bounds.x + bounds.width, bounds.y, bounds.x + bounds.width, bounds.y + 9});
		graphics.drawString(text, bounds.x + 5, bounds.y + 5);
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// TODO Auto-generated method stub
		
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
		
	}
	
	
}
