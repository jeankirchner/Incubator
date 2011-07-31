package flowbuilder.code.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Representa um pedaço de código que é formado por um conjunto de outros trechos de código.
 * 
 * @author Anderson Dorow
 */
public abstract class CodeComposite extends Code {

	protected List<Code>	innerCode	= new ArrayList<Code>();

	public void addCodeNode(Code node) {
		innerCode.add(node);
	}

	public void addCodeNodes(Collection<Code> nodes) {
		innerCode.addAll(nodes);
	}

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		for (Code c : innerCode) {
			c.accept(visitor);
		}
	}
	
	public List<Code> getInnerCode() {
		return innerCode;
	}

}
