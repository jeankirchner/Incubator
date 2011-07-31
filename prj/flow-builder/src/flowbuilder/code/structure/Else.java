package flowbuilder.code.structure;

public class Else extends CodeComposite {

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}

}
