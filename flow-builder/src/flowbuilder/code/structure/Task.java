package flowbuilder.code.structure;

/**
 * Representa a defini��o de uma sequ�ncia de passos a serem seguidos em uma tarefa paralela.
 * 
 * @author Anderson Dorow
 */
public class Task extends CodeComposite {

	@Override
	public void accept(FBCVisitor visitor) {
		super.accept(visitor);
		visitor.visit(this);
	}

}
