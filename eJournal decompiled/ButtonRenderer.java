import javax.swing.UIManager;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JButton;

// 
// Decompiled by Procyon v0.5.36
// 

public class ButtonRenderer extends JButton implements TableCellRenderer
{
    public ButtonRenderer() {
        this.setOpaque(true);
    }
    
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        if (isSelected) {
            this.setForeground(table.getSelectionForeground());
            this.setBackground(table.getSelectionBackground());
        }
        else {
            this.setForeground(table.getForeground());
            this.setBackground(UIManager.getColor("Button.background"));
        }
        this.setText((value == null) ? "" : value.toString());
        return this;
    }
}
