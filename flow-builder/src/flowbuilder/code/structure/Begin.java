package flowbuilder.code.structure;

public class Begin extends Code {

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}

}
