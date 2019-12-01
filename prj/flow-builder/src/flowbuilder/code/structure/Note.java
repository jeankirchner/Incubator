package flowbuilder.code.structure;

/**
 * Representa uma anota��o.
 * 
 * @author Anderson Dorow
 */
public class Note extends FBCNode {

	private final String	text;

	public Note(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}

	@Override
	public void accept(FBCVisitor visitor) {
		visitor.visit(this);
	}

}
