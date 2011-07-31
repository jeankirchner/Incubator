package flowbuilder.code.structure;

/**
 * Representa a chamada de outro processo.
 * 
 * @author Anderson Dorow
 */
public class Call extends Code {

	private final String	processName;

	public Call(String process) {
		processName = process;
	}

	public String processName() {
		return processName;
	}

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}

}
