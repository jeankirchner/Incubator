package flowbuilder.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import flowbuilder.code.structure.Begin;
import flowbuilder.code.structure.Code;
import flowbuilder.code.structure.Conditional;
import flowbuilder.code.structure.Else;
import flowbuilder.code.structure.End;
import flowbuilder.code.structure.FBCVisitorAdapter;
import flowbuilder.code.structure.Fork;
import flowbuilder.code.structure.If;
import flowbuilder.code.structure.Loop;
import flowbuilder.code.structure.Note;
import flowbuilder.code.structure.Process;
import flowbuilder.code.structure.Step;
import flowbuilder.code.structure.Task;
import flowbuilder.diagram.model.ActivityComponent;
import flowbuilder.diagram.model.BeginComponent;
import flowbuilder.diagram.model.Component;
import flowbuilder.diagram.model.Connection;
import flowbuilder.diagram.model.DecisionComponent;
import flowbuilder.diagram.model.EndComponent;
import flowbuilder.diagram.model.FlowDiagram;
import flowbuilder.diagram.model.ForkComponent;
import flowbuilder.diagram.model.NoteComponent;
import flowbuilder.diagram.model.TextComponent;


public class FlowGenerator extends FBCVisitorAdapter{

	private int x = 350, y;
	private FlowDiagram diagram;
	
	public FlowDiagram convertProcess(Process process){
		diagram = new FlowDiagram();
	
		// cria o titulo
		createText(process.getName(), 5, 5);
		
		// Converte o begin
		List<Component> source = new ArrayList<Component>(); 
		source = convert(process.getBeginNode(), source);
		createNotes(process.getNotes(), source.get(0));
		
		// itera sobre os outro itens
		for (int i = 0; i < process.getInnerCode().size(); i++) {
			// converte os itens 
			source = convert(process.getInnerCode().get(i), source);			
		}
		
		// Converte o end
		convert(process.getEndNode(), source);
		
		return diagram;
	}
	
	private List<Component> convert(Begin beginNode, List<Component> source) {
		BeginComponent beginComponent = new BeginComponent();
		beginComponent.setLocation(new Point(x - 15, y = y+50));
		beginComponent.setSize(new Dimension(30, 30));
		diagram.addChild(beginComponent);
		
		List<Component> target = new ArrayList<Component>();
		target.add(beginComponent);
		
		createNotes(beginNode.getNotes(), beginComponent);
		createConnections(source, target);
		
		return target;
	}	

	private List<Component> convert(End endNode, List<Component> source) {
		EndComponent endComponent = new EndComponent();
		endComponent.setLocation(new Point(x - 15, y = y+50));
		endComponent.setSize(new Dimension(30, 30));
		diagram.addChild(endComponent);
		
		List<Component> target = new ArrayList<Component>();
		target.add(endComponent);
		
		createNotes(endNode.getNotes(), endComponent);
		createConnections(source, target);
		
		return target;
	}
	
	private List<Component> convert(Fork forkNode, List<Component> source){
		ForkComponent forkComponent = new ForkComponent();
		forkComponent.setLocation(new Point(x - (50 * forkNode.getInnerCode().size()), y = y+50));
		forkComponent.setSize(new Dimension(100 * forkNode.getInnerCode().size(), 10));
		diagram.addChild(forkComponent);
		
		List<Component> target = new ArrayList<Component>();
		target.add(forkComponent);
		
		createNotes(forkNode.getNotes(), forkComponent);
		createConnections(source, target);
		source = target;
		
		int oldX = x;
		int oldY = y;
		x = x+50 - (50 * forkNode.getInnerCode().size());
		
		List<Component> taskSources = new ArrayList<Component>();
		// Itera sobre os itens
		for (int i = 0; i < forkNode.getInnerCode().size(); i++) {
			y = oldY;
			// converte os itens 
			taskSources.addAll(convert(forkNode.getInnerCode().get(i), source));	
			x += 100;
		}
		x = oldX;
		
		forkComponent = new ForkComponent();
		forkComponent.setLocation(new Point(x - (50 * forkNode.getInnerCode().size()), y = y+50));
		forkComponent.setSize(new Dimension(100 * forkNode.getInnerCode().size(), 10));
		diagram.addChild(forkComponent);
		
		target = new ArrayList<Component>();
		target.add(forkComponent);
		
		createConnections(taskSources, target);
		return target;
	}
	
	private List<Component> convert(Step stepNode, List<Component> source){
		ActivityComponent activityComponent = new ActivityComponent();
		activityComponent.setLocation(new Point(x - 40, y = y+50));
		activityComponent.setSize(new Dimension(80, 30));
		activityComponent.setText(stepNode.statement());
		diagram.addChild(activityComponent);
		
		List<Component> target = new ArrayList<Component>();
		target.add(activityComponent);
		
		createNotes(stepNode.getNotes(), activityComponent);
		createConnections(source, target);
		return target;
	}
	
	private List<Component> convert(Loop loopNode, List<Component> source){
		source = createDecision(loopNode.getCondition(), source);
		createNotes(loopNode.getNotes(), source.get(0));
		List<Component> lastTarget = source;
		
		createText("true", x-40, y+30);
		
		// Itera sobre os itens
		for (int i = 0; i < loopNode.getInnerCode().size(); i++) {
			// converte os itens 
			source = convert(loopNode.getInnerCode().get(i), source);						
		}

		createText("false", x-40, y+30);
		createConnections(source, lastTarget);
		return lastTarget;
	}
	
	private List<Component> convert(Task taskNode, List<Component> source){
		// Itera sobre os itens
		for (int i = 0; i < taskNode.getInnerCode().size(); i++) {
			// converte os itens 
			source = convert(taskNode.getInnerCode().get(i), source);
			
			if (i == 0){
				createNotes(taskNode.getNotes(), source.get(0));
			}
		}
		return source;
	}
	
	private List<Component> convert(Conditional conditionNode, List<Component> source){
		source = createDecision(conditionNode.get_if().getCondition(), source);
		createNotes(conditionNode.getNotes(), source.get(0));
				
		List<Component> ret = new ArrayList<Component>();
		ret.addAll(convert(conditionNode.get_if(), source));
		if (conditionNode.get_else() != null){
			ret.addAll(convert(conditionNode.get_else(), source));
		} else {
			ret.addAll(source);
			createText("false", x-40, y+30);
		}
		return ret;
	}
	
	private List<Component> convert(If ifNode, List<Component> source){
		// Itera sobre os itens
		for (int i = 0; i < ifNode.getInnerCode().size(); i++) {
			if (i == 0){
				createText("true", x-40, y+30);
			}
			
			// converte os itens 
			source = convert(ifNode.getInnerCode().get(i), source);
			createNotes(ifNode.getNotes(), source.get(0));
		}
		return source;
	}
	
	private List<Component> convert(Else elseNode, List<Component> source){
		// Itera sobre os itens
		for (int i = 0; i < elseNode.getInnerCode().size(); i++) {
			if (i == 0){
				createText("false", x-40, y+30);
			}
			
			// converte os itens 
			source = convert(elseNode.getInnerCode().get(i), source);
			createNotes(elseNode.getNotes(), source.get(0));
		}		
		return source;
	}
	
	private void createNotes(List<Note> noteNodes, Component source){
		int noteX = x + 100;
		
		for (int i = 0; i < noteNodes.size(); i++) {
			Note noteNode = noteNodes.get(i);
			
			NoteComponent noteComponent = new NoteComponent();
			noteComponent.setLocation(new Point(noteX = noteX + 100, y-50));
			noteComponent.setSize(new Dimension(80, 30));
			noteComponent.setText(noteNode.text());
			diagram.addChild(noteComponent);
			
			new Connection(source, noteComponent).setLineStyle(Graphics.LINE_DASH);
		}
	}
	
	private void createConnections(List<Component> source, List<Component> target) {
		for (int i = 0; i < source.size(); i++) {
			Component sourceComp = source.get(i);
			for (int j = 0; j < target.size(); j++) {
				Component targetComp = target.get(j);
				new Connection(sourceComp, targetComp).setLineStyle(Graphics.LINE_SOLID);
			}
		}
	}
	
	private List<Component> createDecision(String conditionString, List<Component> source){
		createText(conditionString, x + 5, y+30);
		
		DecisionComponent decisionComponent = new DecisionComponent();
		decisionComponent.setLocation(new Point(x - 15, y = y+50));
		decisionComponent.setSize(new Dimension(25, 25));
		diagram.addChild(decisionComponent);
		
		List<Component> target = new ArrayList<Component>();
		target.add(decisionComponent);
		
		createConnections(source, target);
		return target;
	}
	
	private void createText(String title, int x, int y){
		TextComponent processText = new TextComponent();
		processText.setLocation(new Point(x, y));
		processText.setText(title);
		processText.setSize(new Dimension(title.length()*8, 20));
		diagram.addChild(processText);
	}
	
	private List<Component> convert(Code code, List<Component> source){
		if (code instanceof Begin){
			return convert((Begin)code, source);
		} else if (code instanceof End){
			return convert((End)code, source);
		} else if (code instanceof Fork){
			return convert((Fork)code, source);
		} else if (code instanceof Step){
			return convert((Step)code, source);
		} else if (code instanceof Loop){
			return convert((Loop)code, source);
		} else if (code instanceof Task){
			return convert((Task)code, source);
		} else if (code instanceof If){
			return convert((If)code, source);
		} else if (code instanceof Else){
			return convert((Else)code, source);
		} else if (code instanceof Conditional){
			return convert((Conditional)code, source);
		}
		return new ArrayList<Component>();
	}
	
}
