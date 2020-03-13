import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

// 
// Decompiled by Procyon v0.5.36
// 

class FixedSizePlainDocument extends PlainDocument
{
    int maxSize;
    
    public FixedSizePlainDocument(final int limit) {
        this.maxSize = limit;
    }
    
    @Override
    public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
        if (this.getLength() + str.length() <= this.maxSize) {
            super.insertString(offs, str, a);
            return;
        }
        throw new BadLocationException("Insertion exceeds max size of document", offs);
    }
}
