package flowbuilder.code.structure;

/**
 * Representa a execução de diversos processos repetidamente, enquanto uma determinada condição é verdadeira.
 * 
 * @author Anderson Dorow
 */
public class Loop extends CodeComposite {
	private final String	condition;

	public Loop(String condition) {
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
