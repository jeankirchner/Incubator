package flowbuilder.code.structure;

import java.util.Collection;

/**
 * Representa a definição de execuções paralelas.
 * 
 * @author Anderson Dorow
 */
public class Fork extends CodeComposite {

	@Override
	public void addCodeNode(Code node) {
		if (!(node instanceof Task)) {
			throw new IllegalArgumentException("Code for fork structure must be an task");
		}
		super.addCodeNode(node);
	}

	@Override
	public void addCodeNodes(Collection<Code> nodes) {
		for (Code c : nodes) {
			addCodeNode(c);
		}
	}

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
		for (Code c : innerCode) {
			((Task) c).accept(visitor);
		}
	}

}
