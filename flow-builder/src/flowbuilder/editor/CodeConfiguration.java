package flowbuilder.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;

public class CodeConfiguration extends SourceViewerConfiguration {

	private CodeDoubleClickStrategy doubleClickStrategy;
	private CodeScanner scanner;
	private final ColorManager colorManager;

	public CodeConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
		// TODO
		};
	}

	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (this.doubleClickStrategy == null) {
			this.doubleClickStrategy = new CodeDoubleClickStrategy();
		}
		return this.doubleClickStrategy;
	}

	protected CodeScanner getFlowBuilderCodeScanner() {
		if (this.scanner == null) {
			this.scanner = new CodeScanner(this.colorManager);
			this.scanner.setDefaultReturnToken(new Token(new TextAttribute(this.colorManager.getColor(ICodeColorConstants.DEFAULT))));
		}
		return this.scanner;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(this.getFlowBuilderCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndrComment = new NonRuleBasedDamagerRepairer(new TextAttribute(this.colorManager.getColor(ICodeColorConstants.COMMENT)));
		reconciler.setDamager(ndrComment, CodePartitionScanner.COMMENT);
		reconciler.setRepairer(ndrComment, CodePartitionScanner.COMMENT);

		NonRuleBasedDamagerRepairer ndrKW = new NonRuleBasedDamagerRepairer(new TextAttribute(this.colorManager.getColor(ICodeColorConstants.KEYWORD), null, SWT.BOLD));
		reconciler.setDamager(ndrKW, CodePartitionScanner.KEYWORD);
		reconciler.setRepairer(ndrKW, CodePartitionScanner.KEYWORD);

		NonRuleBasedDamagerRepairer ndrNote = new NonRuleBasedDamagerRepairer(new TextAttribute(this.colorManager.getColor(ICodeColorConstants.NOTE), null, SWT.BOLD));
		reconciler.setDamager(ndrNote, CodePartitionScanner.NOTE);
		reconciler.setRepairer(ndrNote, CodePartitionScanner.NOTE);
		
		return reconciler;
	}

}
