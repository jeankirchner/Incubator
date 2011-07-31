package flowbuilder.editor;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class CodeScanner extends RuleBasedScanner {

	public CodeScanner(ColorManager colorManager) {
//		IToken comment = new Token(new TextAttribute(colorManager.getColor(ICodeColorConstants.COMMENT)));
//		IToken note = new Token(new TextAttribute(colorManager.getColor(ICodeColorConstants.NOTE), null, SWT.BOLD));
//		IToken keyword = new Token(new TextAttribute(colorManager.getColor(ICodeColorConstants.KEYWORD), null, SWT.BOLD));
//		IToken defaultToken = new Token(new TextAttribute(colorManager.getColor(ICodeColorConstants.DEFAULT)));


		IRule[] rules = new IRule[1];
		rules[0] = new WhitespaceRule(new CodeWhiteSpaceDetector());

		this.setRules(rules);
	}

}
