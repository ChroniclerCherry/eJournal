import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.LayoutManager;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.36
// 

public class SelectJournalFrame extends JFrame implements ActionListener
{
    private JComboBox journalDropdown;
    private String[] journalFolders;
    private JButton newJournalButton;
    private JButton openJournalButton;
    private JButton deleteJournalButton;
    
    public static void main(final String[] args) {
        IO.createOutputFile("OpenedJournals.jrn");
        IO.closeOutputFile();
        final File file = new File("OpenedJournals.jrn");
        file.deleteOnExit();
        new SelectJournalFrame();
    }
    
    private SelectJournalFrame() {
        super("eJournal");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(3);
        this.setLayout(null);
        this.setResizable(false);
        (this.deleteJournalButton = new JButton("Delete Selected Journal")).setBounds(20, 180, 200, 30);
        this.deleteJournalButton.addActionListener(this);
        this.deleteJournalButton.setToolTipText("Click this button to delete whichever journal you currently have selected");
        this.add(this.deleteJournalButton);
        (this.openJournalButton = new JButton("Open selected Journal")).setBounds(250, 180, 200, 30);
        this.openJournalButton.addActionListener(this);
        this.openJournalButton.setToolTipText("Click this button to open whichever journal you currently have selected");
        this.add(this.openJournalButton);
        this.getJournals();
        final JLabel titleLabel = new JLabel("eJournal");
        titleLabel.setBounds(20, 20, this.getWidth(), 40);
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setFont(new Font("Arial", 1, 41));
        this.add(titleLabel);
        final JLabel journalLabel = new JLabel("Choose Journal :");
        journalLabel.setBounds(0, 80, 500, 30);
        journalLabel.setFont(new Font("Arial", 1, 21));
        journalLabel.setHorizontalAlignment(0);
        this.add(journalLabel);
        (this.journalDropdown = new JComboBox((E[])this.journalFolders)).setBounds(100, 130, 300, 30);
        this.journalDropdown.setToolTipText("You can select which journal you want to open or delete here");
        this.add(this.journalDropdown);
        (this.newJournalButton = new JButton("New Journal")).setBounds(150, 220, 200, 30);
        this.newJournalButton.addActionListener(this);
        this.newJournalButton.setToolTipText("Click this button to create a new Journal");
        this.add(this.newJournalButton);
        this.setVisible(true);
    }
    
    private void getJournals() {
        final File file = new File("Journals");
        final boolean directoryExists = file.exists();
        if (!directoryExists) {
            file.mkdir();
        }
        this.journalFolders = file.list();
        if (this.journalFolders.length == 0) {
            (this.journalFolders = new String[1])[0] = "No Journals were found";
            this.deleteJournalButton.setEnabled(false);
            this.openJournalButton.setEnabled(false);
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.openJournalButton) {
            final ArrayList<String> openedJournalsList = new ArrayList<String>();
            try {
                IO.openInputFile("OpenedJournals.jrn");
                for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                    openedJournalsList.add(temp);
                }
                IO.closeInputFile();
            }
            catch (IOException i) {
                JOptionPane.showMessageDialog(this, "Error in retriving Info", "Checking File failure", 0);
            }
            final int n = openedJournalsList.indexOf(this.journalDropdown.getSelectedItem());
            if (n == -1) {
                this.addOpenedJournal((String)this.journalDropdown.getSelectedItem());
                new MainFrame("Journals/" + (String)this.journalDropdown.getSelectedItem());
                IO.createOutputFile(this.journalDropdown.getSelectedItem() + "OpenedEntries.jrn");
                IO.closeOutputFile();
            }
            else {
                JOptionPane.showMessageDialog(this, "This Journal is already open", "Selected journal already open", 0);
            }
        }
        if (e.getSource() == this.newJournalButton) {
            this.createNewJournal();
        }
        if (e.getSource() == this.deleteJournalButton) {
            final ArrayList<String> openedJournalsList = new ArrayList<String>();
            try {
                IO.openInputFile("OpenedJournals.jrn");
                for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                    openedJournalsList.add(temp);
                }
                IO.closeInputFile();
            }
            catch (IOException i) {
                JOptionPane.showMessageDialog(this, "Error in retrieving Info", "Checking File failure", 0);
            }
            final int n = openedJournalsList.indexOf(this.journalDropdown.getSelectedItem());
            if (n == -1) {
                this.deleteJournal();
            }
            else {
                JOptionPane.showMessageDialog(this, "Close this journal before deleting", "Journal Open", 0);
            }
        }
    }
    
    private void deleteJournal() {
        final File deletedJournal = new File("Journals/" + (String)this.journalDropdown.getSelectedItem());
        int n = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the journal '" + (String)this.journalDropdown.getSelectedItem() + "'?\nYou will lose all entries in the journal.", "Journal deletion confirmation", 0);
        if (n == 0) {
            n = JOptionPane.showConfirmDialog(this, "Are you absolutely sure you want to delete the journal '" + (String)this.journalDropdown.getSelectedItem() + "'?\nYou will lose all entries in the journal", "Journal deletion confirmation", 0);
            if (n == 0) {
                final String[] storedjournals = deletedJournal.list();
                boolean deleteContinue = true;
                for (int i = 0; i < storedjournals.length; ++i) {
                    final boolean success = new File(deletedJournal, storedjournals[i]).delete();
                    if (!success) {
                        deleteContinue = false;
                        JOptionPane.showMessageDialog(this, "Journal was not sucessfully deleted, though some files may be missing", "Journal deletion failure", 0);
                    }
                }
                if (deleteContinue) {
                    final boolean success2 = deletedJournal.delete();
                    if (success2) {
                        JOptionPane.showMessageDialog(this, "Journal was sucessfully deleted", "Journal deletion Success", 1);
                        this.refresh();
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Journal was not sucessfully deleted", "Journal deletion failure", 0);
                    }
                }
            }
        }
    }
    
    private void createNewJournal() {
        final int n = JOptionPane.showConfirmDialog(this, "You want to create a new journal?", "New Journal Confirmation", 0);
        if (n == 0) {
            final String journalName = JOptionPane.showInputDialog(this, "Please enter what you want your journal to be called:", "Create new Journal", 3);
            final File journalFolder = new File("Journals/" + this.deleteInvalidChars(journalName));
            if (journalName == null) {
                JOptionPane.showMessageDialog(this, "The journal was not created", "Journal not created", 1);
            }
            else if (this.deleteInvalidChars(journalName).equals("")) {
                JOptionPane.showMessageDialog(this, "Journal not created: Journal needs a valid name.", "Journal not created", 1);
            }
            else if (journalFolder.exists()) {
                JOptionPane.showMessageDialog(this, "Error: Journal " + journalName + " already exists", "Journal already exists", 0);
            }
            else {
                final boolean sucess = journalFolder.mkdirs();
                if (sucess) {
                    JOptionPane.showMessageDialog(this, "Journal was created sucessfully", "Journal Creation sucess", 1);
                    this.refresh();
                }
                else {
                    JOptionPane.showMessageDialog(this, "Journal was not created sucessfully", "Journal Creation failure", 0);
                }
            }
        }
    }
    
    private void refresh() {
        new SelectJournalFrame();
        this.dispose();
    }
    
    private void addOpenedJournal(final String s) {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile("OpenedJournals.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "File Creation failure", 0);
        }
        openedJournalsList.add(s);
        IO.createOutputFile("OpenedJournals.jrn");
        for (int i = 0; i < openedJournalsList.size(); ++i) {
            IO.println(openedJournalsList.get(i));
        }
        IO.closeOutputFile();
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
