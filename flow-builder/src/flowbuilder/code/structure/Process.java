package flowbuilder.code.structure;

/**
 * Representa um processo.
 * 
 * @author Anderson Dorow
 */
public class Process extends CodeComposite {

	private Begin			beginNode;
	private End				endNode;
	private final String	name;

	public Process(String name, Begin begin, End end) {
		this.name = name;
		beginNode = begin;
		endNode = end;
	}

	@Override
	public void addCodeNode(Code node) {
		if (node.getClass() == Begin.class) {
			if (beginNode != null) {
				throw new IllegalArgumentException("An process can have just one begin");
			}
			beginNode = (Begin) node;

		} else if (node.getClass() == End.class) {
			if (beginNode != null) {
				throw new IllegalArgumentException("An process can have just one end");
			}
			endNode = (End) node;
		} else {
			if (beginNode == null || endNode == null) {
				throw new IllegalArgumentException();
			}
			super.addCodeNode(node);
		}
	}

	@Override
	public void accept(FBCVisitor visitor) {
		visitor.visit(this);
		beginNode.accept(visitor);
		super.accept(visitor);
		endNode.accept(visitor);
	}
	
	public Begin getBeginNode() {
		return beginNode;
	}
	
	public End getEndNode() {
		return endNode;
	}
	
	public String getName() {
		return name;
	}
}
