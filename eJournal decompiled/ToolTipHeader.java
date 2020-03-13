import java.awt.event.MouseEvent;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;

// 
// Decompiled by Procyon v0.5.36
// 

class ToolTipHeader extends JTableHeader
{
    String[] toolTips;
    
    public ToolTipHeader(final TableColumnModel model) {
        super(model);
    }
    
    @Override
    public String getToolTipText(final MouseEvent e) {
        final int col = this.columnAtPoint(e.getPoint());
        final int modelCol = this.getTable().convertColumnIndexToModel(col);
        String retStr;
        try {
            retStr = this.toolTips[modelCol];
        }
        catch (NullPointerException ex) {
            retStr = "";
        }
        catch (ArrayIndexOutOfBoundsException ex2) {
            retStr = "";
        }
        if (retStr.length() < 1) {
            retStr = super.getToolTipText(e);
        }
        return retStr;
    }
    
    public void setToolTipStrings(final String[] toolTips) {
        this.toolTips = toolTips;
    }
}
