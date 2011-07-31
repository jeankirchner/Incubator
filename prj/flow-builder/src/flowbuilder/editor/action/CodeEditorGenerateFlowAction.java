package flowbuilder.editor.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import flowbuilder.code.parser.FBC;
import flowbuilder.code.structure.FBCVisitorAdapter;
import flowbuilder.code.structure.Process;
import flowbuilder.diagram.model.FlowDiagram;
import flowbuilder.generator.FlowGenerator;

public class CodeEditorGenerateFlowAction extends FBCVisitorAdapter implements IEditorActionDelegate{

	private IAction action;
	private IEditorPart editor;
	private FlowDiagram diagram;
	
	
	@Override
	public void setActiveEditor(IAction arg0, IEditorPart arg1) {
		action = arg0;
		editor = arg1;
	}

	@Override
	public void run(IAction arg0) {
		IFile file = ((FileEditorInput)editor.getEditorInput().getPersistable()).getFile();
		try {
			InputStream in = file.getContents();
			int i = in.available();
			byte[] bt = new byte[i];
			in.read(bt);
			Process process = (Process) FBC.parseCode(new String(bt));
			diagram = new FlowGenerator().convertProcess(process);
		} catch (Exception e) {
			MessageBox box = new MessageBox(editor.getSite().getShell());
			box.setText("Compiler Error");
			box.setMessage(e.getMessage());
			box.open();
			return;
		}
		writeFile(file.getName().toString());
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// Stub
	}
	
	private void writeFile(String filePath) {
		// Show a SaveAs dialog
		final SaveAsDialog dialog = new SaveAsDialog(editor.getEditorSite().getShell());
		dialog.setOriginalName(filePath.replace(".fbc", ".fbd"));
		dialog.open();
		
		IPath path = dialog.getResult();
		if (path != null) {
			// try to save the editor's contents under a different file name
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			try {
				new ProgressMonitorDialog(null).run(
						false, // don't fork
						false, // not cancelable
						new WorkspaceModifyOperation() { // run this operation
							public void execute(final IProgressMonitor monitor) throws CoreException, InvocationTargetException {
							    monitor.beginTask("Writing file '" + file.getName() + "'", 2);
								try {
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									ObjectOutputStream oos = new ObjectOutputStream(out);
									oos.writeObject(diagram);
									oos.close();
									
									if (file.exists()) {
									    file.setContents(new ByteArrayInputStream(out.toByteArray()), // contents
                                                true, // keep saving, even if IFile is out of sync with the Workspace
                                                true, // keep history
                                                monitor); // progress monitor);
									} else {
									    file.create(
									            new ByteArrayInputStream(out.toByteArray()), // contents
									            true, // keep saving, even if IFile is out of sync with the Workspace
									            monitor); // progress monitor
									}
									
									monitor.worked(1);
								} catch (IOException ioe) {
								    throw new InvocationTargetException(ioe);
								}
								
								monitor.setTaskName("Opening editor...");
								// abre o diagrama
								editor.getEditorSite().getShell().getDisplay().asyncExec(new Runnable() {
					                public void run() {
					                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					                    try {
					                        IDE.openEditor(page, file, true);
					                    } catch (PartInitException e) {
					                    }
					                }
					            });
								monitor.worked(1);
							}
						});
			} catch (InterruptedException ie) {
	  			// should not happen, since the monitor dialog is not cancelable
				ie.printStackTrace(); 
			} catch (InvocationTargetException ite) { 
				ite.printStackTrace(); 
			}
		}

	}
	
}
