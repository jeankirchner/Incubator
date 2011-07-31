package flowbuilder.code.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import flowbuilder.code.structure.Begin;
import flowbuilder.code.structure.Call;
import flowbuilder.code.structure.Else;
import flowbuilder.code.structure.End;
import flowbuilder.code.structure.FBCNode;
import flowbuilder.code.structure.FBCVisitor;
import flowbuilder.code.structure.Fork;
import flowbuilder.code.structure.If;
import flowbuilder.code.structure.Loop;
import flowbuilder.code.structure.Note;
import flowbuilder.code.structure.Process;
import flowbuilder.code.structure.Step;
import flowbuilder.code.structure.Task;

public class Main {

	private static String readFile(String file) throws IOException {
		StringBuilder content = new StringBuilder();
		InputStream in = new FileInputStream(file);

		int count;
		byte[] bts = new byte[4096];
		while ((count = in.read(bts)) > 0) {
			content.append(new String(bts, 0, count));
		}
		return content.toString();
	}

	public static void main(String[] args) throws IOException, ParseException {
		String content = readFile("./doc/Especificação Linguagem.txt");
		FBCNode node = FBC.parseCode(content);
		System.out.println(node);
	}

	private static class TestVisitor implements FBCVisitor {

		@Override
		public void visit(Process process) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Loop loop) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Else elseObj) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Task task) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(If ifObj) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Fork fork) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(End end) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Begin begin) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Call call) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Step step) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visit(Note note) {
			// TODO Auto-generated method stub

		}

	}

}
