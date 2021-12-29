package flowbuilder.code.structure;

/**
 * Representa uma expressão condicional.
 * 
 * @author Anderson Dorow
 */
public class If extends CodeComposite {

	private final String	condition;

	public If(String condition) {
		this.condition = condition;
	}

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}

	public String getCondition() {
		return condition;
	}
	
}
