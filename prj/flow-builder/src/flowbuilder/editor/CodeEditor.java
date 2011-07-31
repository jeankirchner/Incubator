package flowbuilder.editor;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Editor da pseudo linguagem do FlowBuilder.
 * 
 * @author felipe
 */
public class CodeEditor extends TextEditor implements IResourceChangeListener, ITextListener {

	private ColorManager colorManager;

	public CodeEditor() {
		super();
		this.colorManager = new ColorManager();
		this.setSourceViewerConfiguration(new CodeConfiguration(this.colorManager));
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		setDocumentProvider(new CodeDocumentProvider());
	}

	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
	    if (!(editorInput instanceof IFileEditorInput))
            throw new PartInitException("Invalid Input: Must be IFileEditorInput");
	    super.init(site, editorInput);
	}
	
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
	    super.doSave(progressMonitor);
	}
	
	@Override
	public void doSaveAs() {
	    super.doSaveAs();
	}
	
	@Override
	public boolean isSaveAsAllowed() {
	    // não há situação em que não seja permitido "salvar como..."
	    return true;
	}
	
	/**
     * Fecha todos os arquivos abertos ao fechar o projeto.
     */
    public void resourceChanged(final IResourceChangeEvent event){
        if (this.getSourceViewer() != null) {
            this.getSourceViewer().addTextListener(this);
        }
        
        // Fecha todos os arquivos abertos ao fechar o projeto.
        if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
            Display.getDefault().asyncExec(new Runnable(){
                public void run(){
                    IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
                    for (int i = 0; i<pages.length; i++){
                        if(((FileEditorInput) getEditorInput()).getFile().getProject().equals(event.getResource())){
                            IEditorPart editorPart = pages[i].findEditor(getEditorInput());
                            pages[i].closeEditor(editorPart,true);
                        }
                    }
                }            
            });
        }
    }
	
	@Override
	public void dispose() {
		this.colorManager.dispose();
		super.dispose();
	}

    @Override
    public void textChanged(TextEvent event) {
        //System.out.println("text changed: " + event);
    }

}
