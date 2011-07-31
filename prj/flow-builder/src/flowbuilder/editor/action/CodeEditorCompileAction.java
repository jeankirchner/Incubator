package flowbuilder.editor.action;

import java.io.InputStream;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.FileEditorInput;

import flowbuilder.code.parser.FBC;

public class CodeEditorCompileAction implements IEditorActionDelegate {

	IAction action;
	IEditorPart editor;
	
	@Override
	public void setActiveEditor(IAction arg0, IEditorPart arg1) {
		action = arg0;
		editor = arg1;
	}

	@Override
	public void run(IAction arg0) {
		try {
			InputStream in = ((FileEditorInput)editor.getEditorInput().getPersistable()).getFile().getContents();
			int i = in.available();
			byte[] bt = new byte[i];
			in.read(bt);
			FBC.parseCode(new String(bt));
		} catch (Exception e) {
			MessageBox box = new MessageBox(editor.getSite().getShell());
			box.setText("Compiler Error");
			box.setMessage(e.getMessage());
			box.open();
			return;
		}
		MessageBox box = new MessageBox(editor.getSite().getShell());
		box.setText("Compiled");
		box.setMessage("No errors found!");
		box.open();
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
