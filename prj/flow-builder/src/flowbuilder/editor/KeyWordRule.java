package flowbuilder.editor;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;

public class KeyWordRule extends PatternRule {
    
	public KeyWordRule(String keyword, IToken token) {
		super(keyword, " ", token, (char) 0, true);
	}

	@Override
	protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
	    if (scanner.getColumn() > 0) {
	        scanner.unread();
	        char prevChar = (char) scanner.read();
	        if (Character.isLetterOrDigit(prevChar)) {
	            // tem algo antes? então não é um token esperado
	            return Token.UNDEFINED;
	        }
	    }
	    int c = scanner.read();
        if (equalChars((char) c, fStartSequence[0])) {
            if (sequenceDetected(scanner, fStartSequence, false))
                return fToken;
        }

        scanner.unread();
        return Token.UNDEFINED;
	}
	
	private boolean equalChars(char a, char b) {
	    if (Character.isLetter(a) && Character.isLetter(b)) {
	        return Character.toUpperCase(a) == Character.toUpperCase(b);
	    }
	    return (a == b);
	}
	
    protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
        // start of sequence
        for (int i = 1; i < sequence.length; i++) {
            if (!equalChars((char) scanner.read(), sequence[i])) {
                unread(scanner, i);
                return false;
            }
        }
        char charAfter = (char) scanner.read();
        scanner.unread();
        // tem caracteres a mais? entao nao é a palavra desejada
        if (Character.isLetterOrDigit(charAfter)) {
            unread(scanner, sequence.length - 1);
            return false;
        }

        return true;
    }

    private void unread(ICharacterScanner scanner, int iReaded) {
        for (int j= iReaded-1; j >= 0; j--)
            scanner.unread();
    }
}