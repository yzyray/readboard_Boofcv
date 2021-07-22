package boardsync;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class IntDocument extends PlainDocument {
  private static final long serialVersionUID = 2L;

  public IntDocument() {
    super();
  }

  public void insertString(int offset, String inStr, AttributeSet attrSet)
      throws BadLocationException {
    String numStr = getText(0, offset) + inStr + getText(offset, getLength() - offset);
    try {
      new Integer(numStr);
    } catch (NumberFormatException e1) {
      return;
    }
    super.insertString(offset, inStr, attrSet);
  }
}
