package flowbuilder.code.structure;

/**
 * Representa a definição de uma sequência de passos a serem seguidos em uma tarefa paralela.
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
