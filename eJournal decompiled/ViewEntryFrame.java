import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.LayoutManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.36
// 

public class ViewEntryFrame extends JFrame implements ActionListener
{
    private JButton deleteEntryButton;
    private JButton editEntryButton;
    private JButton closeButton;
    private JournalEntry journal;
    private MainFrame mainFrame;
    
    public ViewEntryFrame(final JournalEntry journalEntry, final MainFrame mf) {
        super("eJournal - Viewing entry : " + journalEntry.getTitle());
        this.setSize(500, 700);
        this.setLayout(null);
        this.setResizable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                ViewEntryFrame.this.closedEntry();
            }
        });
        this.mainFrame = mf;
        this.journal = journalEntry;
        this.addOpenedEntry();
        final JLabel titleLabel = new JLabel(journalEntry.getTitle());
        titleLabel.setBounds(0, 20, 500, 40);
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setFont(new Font("Arial", 1, 31));
        this.add(titleLabel);
        final JLabel dateLabel = new JLabel("This entry was written on : " + journalEntry.getDate());
        dateLabel.setHorizontalAlignment(0);
        dateLabel.setBounds(0, 45, 500, 40);
        this.add(dateLabel);
        final JLabel lastModifiedDateLabel = new JLabel("This entry was last modified : " + journalEntry.getLastModifiedDate());
        lastModifiedDateLabel.setHorizontalAlignment(0);
        lastModifiedDateLabel.setBounds(0, 60, 500, 40);
        this.add(lastModifiedDateLabel);
        final JTextArea displayArea = new JTextArea(journalEntry.getEntry());
        displayArea.setBounds(10, 100, 470, 500);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        displayArea.setEditable(false);
        final JScrollPane displayScroll = new JScrollPane(displayArea);
        displayScroll.setBounds(10, 100, 470, 500);
        this.add(displayScroll);
        (this.deleteEntryButton = new JButton("Delete this Entry")).setBounds(10, 610, 150, 30);
        this.deleteEntryButton.addActionListener(this);
        this.deleteEntryButton.setToolTipText("Click this button to delete the current entry");
        this.add(this.deleteEntryButton);
        (this.closeButton = new JButton("Close")).setBounds(170, 610, 150, 30);
        this.closeButton.addActionListener(this);
        this.closeButton.setToolTipText("Click this button to close the current entry");
        this.add(this.closeButton);
        (this.editEntryButton = new JButton("Edit this Entry")).setBounds(330, 610, 150, 30);
        this.editEntryButton.addActionListener(this);
        this.editEntryButton.setToolTipText("Click this button to edit the current entry");
        this.add(this.editEntryButton);
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.deleteEntryButton) {
            final int n = JOptionPane.showConfirmDialog(this, "Do you want to delete this entry?", "delete Entry Confirmation", 0);
            if (n == 0) {
                this.closedEntry();
                this.journal.delete();
                this.mainFrame.refresh();
                this.dispose();
            }
        }
        if (e.getSource() == this.editEntryButton) {
            this.closedEntry();
            new EditEntryFrame(this.journal, this.mainFrame);
            this.dispose();
        }
        if (e.getSource() == this.closeButton) {
            this.closedEntry();
            this.dispose();
        }
    }
    
    private void closedEntry() {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.journal.getFolder().substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "File Creation failure", 0);
        }
        final int n = openedJournalsList.indexOf(this.journal.getTitle());
        openedJournalsList.remove(n);
        IO.createOutputFile(String.valueOf(this.journal.getFolder().substring(9)) + "OpenedEntries.jrn");
        for (int i = 0; i < openedJournalsList.size(); ++i) {
            IO.println(openedJournalsList.get(i));
        }
        IO.closeOutputFile();
    }
    
    private void addOpenedEntry() {
        final ArrayList<String> openedEntriesList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.mainFrame.getJournalName().substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedEntriesList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this.mainFrame, "Error in retrieving Info", "File Creation failure", 0);
        }
        openedEntriesList.add(this.journal.getTitle());
        IO.createOutputFile(String.valueOf(this.mainFrame.getJournalName().substring(9)) + "OpenedEntries.jrn");
        for (int i = 0; i < openedEntriesList.size(); ++i) {
            IO.println(openedEntriesList.get(i));
        }
        IO.closeOutputFile();
    }
}
