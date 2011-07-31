package flowbuilder.diagram.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;

public class TextFigure extends Shape{

	private String text = "";
	
	public TextFigure() {
	}
	
	public TextFigure(String text) {
		this.text = text;
	}

	
	@Override
	protected void fillShape(Graphics graphics) {
		graphics.drawString(text, new Point(bounds.x + 2, bounds.y + 2));
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
