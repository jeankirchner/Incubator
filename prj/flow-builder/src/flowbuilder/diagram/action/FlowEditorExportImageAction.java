package flowbuilder.diagram.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import flowbuilder.diagram.FlowEditor;

public class FlowEditorExportImageAction implements IEditorActionDelegate{

	private IAction action;
	private IEditorPart editor;

	@Override
	public void setActiveEditor(IAction arg0, IEditorPart arg1) {
		this.action = arg0;
		this.editor = arg1;
	}

	@Override
	public void run(IAction arg0) {
		// Show a SaveAs dialog
		SaveAsDialog dialog = new SaveAsDialog(editor.getEditorSite().getShell());
		dialog.setOriginalName(((FileEditorInput)editor.getEditorInput().getPersistable()).getFile().getName().toString().replace(".fbd", ".jpg"));
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
							public void execute(final IProgressMonitor monitor) {
								try {
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									
									GraphicalViewer viewer = ((FlowEditor)editor).getViewer();
									ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)viewer.getEditPartRegistry().get(LayerManager.ID);
									IFigure rootFigure = ((LayerManager)rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS);//rootEditPart.getFigure();
									Rectangle rootFigureBounds = rootFigure.getBounds();		
									
									Control figureCanvas = viewer.getControl();				
									GC figureCanvasGC = new GC(figureCanvas);		
									
									Image img = new Image(null, rootFigureBounds.width, rootFigureBounds.height);
									GC imageGC = new GC(img);
									imageGC.setBackground(figureCanvasGC.getBackground());
									imageGC.setForeground(figureCanvasGC.getForeground());
									imageGC.setFont(figureCanvasGC.getFont());
									imageGC.setLineStyle(figureCanvasGC.getLineStyle());
									imageGC.setLineWidth(figureCanvasGC.getLineWidth());
									imageGC.setXORMode(figureCanvasGC.getXORMode());
									Graphics imgGraphics = new SWTGraphics(imageGC);
									
									rootFigure.paint(imgGraphics);
									
									ImageData[] imgData = new ImageData[1];
									imgData[0] = img.getImageData();
									
									ImageLoader imgLoader = new ImageLoader();
									imgLoader.data = imgData;
									imgLoader.save(out, SWT.IMAGE_JPEG);
									
									figureCanvasGC.dispose();
									imageGC.dispose();
									img.dispose();
									
									
									file.create(
										new ByteArrayInputStream(out.toByteArray()), // contents
										true, // keep saving, even if IFile is out of sync with the Workspace
										monitor); // progress monitor
									
									out.close();
								} catch (CoreException ce) {
									ce.printStackTrace();
								} catch (IOException ioe) {
									ioe.printStackTrace();
								} 
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

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

}
