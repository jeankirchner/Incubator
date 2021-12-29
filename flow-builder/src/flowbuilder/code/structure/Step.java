package flowbuilder.code.structure;

/**
 * Representa uma a��o, ou um passo, que � executado.
 * 
 * @author Anderson Dorow
 */
public class Step extends Code {

	private final String	statement;

	public Step(String statement) {
		this.statement = statement;
	}

	public String statement() {
		return statement;
	}

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}

}
