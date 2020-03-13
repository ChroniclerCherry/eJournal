import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.DefaultCellEditor;

// 
// Decompiled by Procyon v0.5.36
// 

public class ButtonEditor extends DefaultCellEditor
{
    protected JButton button;
    private String label;
    private boolean isPushed;
    private String journal;
    private int rowNum;
    private JTable myTable;
    private MainFrame mainFrame;
    
    public ButtonEditor(final JCheckBox checkBox, final MainFrame mf) {
        super(checkBox);
        this.mainFrame = mf;
        this.journal = this.mainFrame.getJournalName();
        (this.button = new JButton()).setOpaque(true);
        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                ButtonEditor.this.fireEditingStopped();
            }
        });
    }
    
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
        if (isSelected) {
            this.button.setForeground(table.getSelectionForeground());
            this.button.setBackground(table.getSelectionBackground());
        }
        else {
            this.button.setForeground(table.getForeground());
            this.button.setBackground(table.getBackground());
        }
        this.rowNum = row;
        this.myTable = table;
        this.label = ((value == null) ? "" : value.toString());
        this.button.setText(this.label);
        this.isPushed = true;
        return this.button;
    }
    
    @Override
    public Object getCellEditorValue() {
        if (this.isPushed) {
            final String fileName = (String)this.myTable.getValueAt(this.rowNum, 0);
            if (this.isEntryOpened(fileName)) {
                JOptionPane.showMessageDialog(this.mainFrame, "This entry is already open", "Selected journal already open", 0);
            }
            else {
                final ViewEntryFrame viewEntryFrame = new ViewEntryFrame(new JournalEntry(String.valueOf(fileName) + ".jrn", this.journal), this.mainFrame);
            }
        }
        this.isPushed = false;
        return new String(this.label);
    }
    
    @Override
    public boolean stopCellEditing() {
        this.isPushed = false;
        return super.stopCellEditing();
    }
    
    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
    
    private boolean isEntryOpened(final String s) {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.journal.substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException i) {
            JOptionPane.showMessageDialog(this.mainFrame, "Error in retrieving Info", "Checking File failure", 0);
        }
        final int n = openedJournalsList.indexOf(s);
        return n != -1;
    }
}
