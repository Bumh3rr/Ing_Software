package bumh3r.utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimitTextDocument extends PlainDocument {
    private final int limite;

    public LimitTextDocument(int limite) {
        this.limite = limite;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= limite) {
            super.insertString(offset, str, attr);
        }
    }
}
