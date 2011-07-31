package flowbuilder.code.structure;

public interface FBCVisitor {

	void visit(Process process);

	void visit(Loop loop);

	void visit(Else elseObj);

	void visit(Task task);

	void visit(If ifObj);

	void visit(Fork fork);

	void visit(End end);

	void visit(Begin begin);

	void visit(Call call);

	void visit(Step step);

	void visit(Note note);

}
