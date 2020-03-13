import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.text.Document;
import java.awt.Font;
import java.awt.event.WindowListener;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.36
// 

public class EditEntryFrame extends JFrame implements ActionListener
{
    private JTextField titleTextField;
    private JTextArea displayArea;
    private JButton saveButton;
    private JButton backButton;
    private JButton closeButton;
    private JournalEntry journal;
    private MainFrame mainFrame;
    private String originalName;
    
    public EditEntryFrame(final JournalEntry journalEntry, final MainFrame mf) {
        super("eJournal - Editing Entry : " + journalEntry.getTitle());
        this.setSize(500, 700);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(0);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                final int n = JOptionPane.showConfirmDialog(EditEntryFrame.this.getParent(), "Do you want to close this entry without saving?", "Close Entry Confirmation", 0);
                if (n == 0) {
                    EditEntryFrame.this.closedEntry();
                    EditEntryFrame.this.dispose();
                }
            }
        });
        this.mainFrame = mf;
        this.journal = journalEntry;
        this.originalName = this.journal.getTitle();
        this.addOpenedEntry();
        (this.titleTextField = new JTextField()).setBounds(10, 20, 470, 40);
        this.titleTextField.setHorizontalAlignment(0);
        this.titleTextField.setFont(new Font("Arial", 1, 31));
        this.titleTextField.setToolTipText("Set the text here to change the title of the entry");
        this.titleTextField.setDocument(new FixedSizePlainDocument(26));
        this.titleTextField.setText(journalEntry.getTitle());
        this.add(this.titleTextField);
        final JLabel dateLabel = new JLabel("This entry was written on : " + journalEntry.getDate());
        dateLabel.setHorizontalAlignment(0);
        dateLabel.setBounds(0, 47, 500, 40);
        this.add(dateLabel);
        final JLabel lastModifiedDateLabel = new JLabel("This entry was last modified : " + journalEntry.getLastModifiedDate());
        lastModifiedDateLabel.setHorizontalAlignment(0);
        lastModifiedDateLabel.setBounds(0, 62, 500, 40);
        this.add(lastModifiedDateLabel);
        (this.displayArea = new JTextArea(journalEntry.getEntry())).setBounds(10, 100, 470, 500);
        this.displayArea.setLineWrap(true);
        this.displayArea.setWrapStyleWord(true);
        this.displayArea.setToolTipText("Edit your entry here");
        final JScrollPane displayScroll = new JScrollPane(this.displayArea);
        displayScroll.setBounds(10, 100, 470, 500);
        this.add(displayScroll);
        (this.saveButton = new JButton("Save this entry")).setBounds(10, 610, 150, 30);
        this.saveButton.addActionListener(this);
        this.saveButton.setToolTipText("Click this button to save your entry");
        this.add(this.saveButton);
        (this.closeButton = new JButton("Close")).setBounds(170, 610, 150, 30);
        this.closeButton.addActionListener(this);
        this.closeButton.setToolTipText("Click this button to close your entry without saving");
        this.add(this.closeButton);
        (this.backButton = new JButton("Back to View")).setBounds(330, 610, 150, 30);
        this.backButton.addActionListener(this);
        this.backButton.setToolTipText("Click this button to go back to viewing your entry without saving");
        this.add(this.backButton);
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.saveButton) {
            final File toSaveFile = new File(String.valueOf(this.journal.getFolder()) + "/" + this.deleteInvalidChars(this.titleTextField.getText()) + ".txt");
            boolean toSave = true;
            if (toSaveFile.exists() && !this.titleTextField.getText().equalsIgnoreCase(this.journal.getTitle())) {
                final int n = JOptionPane.showConfirmDialog(this, "An entry with this name already exists. Do you want to override it??", "Override entry", 0);
                if (n == 1) {
                    toSave = false;
                }
                else if (this.isEntryOpened(this.titleTextField.getText())) {
                    toSave = false;
                    JOptionPane.showMessageDialog(this, "A file of this name is already open. You can not override it without first closing it.", "Entry still open", 0);
                }
            }
            if (toSave) {
                final int n = JOptionPane.showConfirmDialog(this, "Do you want to save this entry?", "Save Entry Confirmation", 0);
                if (n == 0) {
                    this.journal.setTitle(String.valueOf(this.deleteInvalidChars(this.titleTextField.getText())) + ".jrn");
                    this.journal.setEntry(this.displayArea.getText());
                    this.journal.saveEntry();
                    this.mainFrame.refresh();
                    this.closedEntry();
                    this.dispose();
                }
            }
        }
        if (e.getSource() == this.backButton) {
            final int n2 = JOptionPane.showConfirmDialog(this, "Do you want to go back to viewing this entry without saving?", "Leaving Frame Confirmation", 0);
            if (n2 == 0) {
                this.closedEntry();
                new ViewEntryFrame(this.journal, this.mainFrame);
                this.dispose();
            }
        }
        if (e.getSource() == this.closeButton) {
            final int n2 = JOptionPane.showConfirmDialog(this, "Do you want to close this entry without saving?", "Close Entry Confirmation", 0);
            if (n2 == 0) {
                this.closedEntry();
                this.dispose();
            }
        }
    }
    
    private void closedEntry() {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.mainFrame.getJournalName().substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "File Creation failure", 0);
        }
        final int n = openedJournalsList.indexOf(this.originalName);
        openedJournalsList.remove(n);
        IO.createOutputFile(String.valueOf(this.mainFrame.getJournalName().substring(9)) + "OpenedEntries.jrn");
        for (int i = 0; i < openedJournalsList.size(); ++i) {
            IO.println(openedJournalsList.get(i));
        }
        IO.closeOutputFile();
    }
    
    private void addOpenedEntry() {
        final ArrayList<String> openedEntriesList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.journal.getFolder().substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedEntriesList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "File Creation failure", 0);
        }
        openedEntriesList.add(this.journal.getTitle());
        IO.createOutputFile(String.valueOf(this.journal.getFolder().substring(9)) + "OpenedEntries.jrn");
        for (int i = 0; i < openedEntriesList.size(); ++i) {
            IO.println(openedEntriesList.get(i));
        }
        IO.closeOutputFile();
    }
    
    private boolean isEntryOpened(final String s) {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.journal.getFolder().substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException i) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "Checking File failure", 0);
        }
        final int n = openedJournalsList.indexOf(s.toLowerCase());
        return n != -1;
    }
    
    private String deleteInvalidChars(final String s) {
        String temp = "";
        final char[] invalidChars = { '*', '<', '>', '[', ']', '=', '+', '\\', '/', ',', ':', ';', '\"', '?', '%', '|', '(', ')' };
        for (int i = 0; i < s.length(); ++i) {
            if (Organizer.sequentialSearch(invalidChars, s.charAt(i)) == -1) {
                temp = String.valueOf(temp) + s.charAt(i);
            }
        }
        return temp;
    }
}
