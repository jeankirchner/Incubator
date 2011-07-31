package flowbuilder.editor;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;

import flowbuilder.code.KeyWord;

/**
 * Scanner de tokens da linguagem
 * @author felipe
 */
public class CodePartitionScanner extends RuleBasedPartitionScanner {

	public final static String COMMENT = "__flowbuilder_comment";
	public final static String KEYWORD = "__flowbuilder_keyword";
	public final static String NOTE = "__flowbuilder_note";
	
	public CodePartitionScanner() {
		IToken comment = new Token(COMMENT);
		IToken keyword = new Token(KEYWORD); 
		IToken note = new Token(NOTE);
		
		IPredicateRule[] rules = new IPredicateRule[11]; 

		// Regras dos tokens
		rules[0] = new SingleLineRule("//", null, comment);
		
		rules[1] = new KeyWordRule(KeyWord.PROCESS.description(), keyword);
		rules[2] = new KeyWordRule(KeyWord.BEGIN.description(), keyword);
		rules[3] = new KeyWordRule(KeyWord.END.description(), keyword);
		rules[4] = new KeyWordRule(KeyWord.IF.description(), keyword);
		rules[5] = new KeyWordRule(KeyWord.ELSE.description(), keyword);
		rules[6] = new KeyWordRule(KeyWord.LOOP.description(), keyword);
		rules[7] = new KeyWordRule(KeyWord.FORK.description(), keyword);
		rules[8] = new KeyWordRule(KeyWord.TASK.description(), keyword);
		rules[9] = new KeyWordRule(KeyWord.CALL.description(), keyword);
		rules[10] = new KeyWordRule(KeyWord.NOTE.description(), note);
		
		setPredicateRules(rules);
	}
	
	@Override
	public char[][] getLegalLineDelimiters() {
	    // TODO Auto-generated method stub
	    return super.getLegalLineDelimiters();
	}
	
}
