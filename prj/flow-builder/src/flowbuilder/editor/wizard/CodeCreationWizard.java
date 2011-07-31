package flowbuilder.editor.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import flowbuilder.Activator;
import flowbuilder.code.KeyWord;

public class CodeCreationWizard extends Wizard implements INewWizard {

    private CodeDataWizardPage page;
    private ISelection selection;

    @Override
    public void addPages() {
        page = new CodeDataWizardPage(selection);
        addPage(page);
    }

    /**
     * Este método é chamado quando o botão 'Finish' é pressionado no Wizard.
     */
    @Override
    public boolean performFinish() {
        final String containerName = page.getContainerName();
        final String processName = page.getProcessName();
        final String fileName = processName + ".fbc";
        
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(containerName));
        if (!resource.exists() || !(resource instanceof IContainer)) {
            MessageDialog.openError(getShell(), "Error", "Container \"" + containerName + "\" does not exist.");
            return false;
        }
        IContainer container = (IContainer) resource;
        final IFile file = container.getFile(new Path(fileName));
        if (file.exists() && !MessageDialog.openQuestion(getShell(), "Question", "The file '" + fileName + "' already exists. Do you want to replace it?")) {
            // o arquivo existe e não quer que seja sobreescrito, então deixa tudo como está
            return false;
        }
        
        IRunnableWithProgress op = new IRunnableWithProgress() {
            public void run(IProgressMonitor monitor) throws InvocationTargetException {
                try {
                    doWriteFile(containerName, processName, file, monitor);
                } catch (CoreException e) {
                    throw new InvocationTargetException(e);
                } finally {
                    monitor.done();
                }
            }
        };
        try {
            getContainer().run(true, false, op);
        } catch (InterruptedException e) {
            return false;
        } catch (InvocationTargetException e) {
            Throwable realException = e.getTargetException();
            MessageDialog.openError(getShell(), "Error", realException.getMessage());
            return false;
        }
        return true;
    }

    /**
     * The worker method. It will find the container, create the file if missing
     * or just replace its contents, and open the editor on the newly created
     * file.
     */

    private void doWriteFile(String containerName, String processName, final IFile file, IProgressMonitor monitor) throws CoreException {
        // create a sample file
        monitor.beginTask("Creating process " + processName, 1);
        
        try {
            InputStream stream = openContentStream(processName);
            if (file.exists()) {
                file.setContents(stream, true, true, monitor);
            } else {
                file.create(stream, true, monitor);
            }
            stream.close();
        } catch (IOException e) {
        }
        monitor.worked(1);
        monitor.beginTask("Opening file for editing...", 1);
        
        getShell().getDisplay().asyncExec(new Runnable() {
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

    /**
     * We will initialize file contents with a sample text.
     * @param processName the name of the process.
     */

    private InputStream openContentStream(String processName) {
        StringBuilder codeContent = new StringBuilder(128);
        
        final String newLine = String.format("%n");
        
        final String username = System.getProperty("user.name");
        
        codeContent.append("// Processo ").append('"').append(processName).append('"');
        codeContent.append(" criado por ").append(username).append(newLine);
        codeContent.append(KeyWord.PROCESS.description()).append(' ').append(processName).append(newLine);
        codeContent.append(newLine);
        
        codeContent.append(KeyWord.BEGIN.description());
        codeContent.append(newLine);

        codeContent.append('\t').append(newLine);
        codeContent.append('\t').append("An action;").append(newLine);
        codeContent.append('\t').append(newLine);

        codeContent.append(KeyWord.END.description());
        codeContent.append(newLine);

        return new ByteArrayInputStream(codeContent.toString().getBytes());
    }

    private void throwCoreException(String message) throws CoreException {
        IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, message, null);
        throw new CoreException(status);
    }

    /**
     * We will accept the selection in the workbench to see if we can initialize
     * from it.
     * 
     * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
    }

}
