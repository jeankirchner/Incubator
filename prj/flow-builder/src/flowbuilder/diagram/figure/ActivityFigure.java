package flowbuilder.diagram.figure;


import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

public class ActivityFigure extends RoundedRectangle{
	
	private String text = "";
	
	public ActivityFigure() {
		// Stub
	}
	
	public ActivityFigure(String text) {
		this.text = text;
	}
	
	@Override
	protected void fillShape(Graphics graphics) {
		super.fillShape(graphics);
		Rectangle bounds = getBounds();
		graphics.drawString(text, bounds.x + 5, bounds.y + 5);
	}
	
	public String getText(){
		return text;
	}
	
	public void setText(String text){
		this.text = text;
		
	}
	
}
