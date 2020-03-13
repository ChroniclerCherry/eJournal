import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.util.regex.PatternSyntaxException;
import javax.swing.RowFilter;
import java.text.DateFormat;
import javax.swing.table.JTableHeader;
import javax.swing.RowSorter;
import javax.swing.table.TableCellEditor;
import javax.swing.JCheckBox;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Font;
import javax.swing.JLabel;
import java.io.File;
import java.awt.event.WindowListener;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.LayoutManager;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

// 
// Decompiled by Procyon v0.5.36
// 

public class MainFrame extends JFrame implements ActionListener
{
    private String journalName;
    private JournalEntry[] entriesList;
    private JButton newEntryButton;
    private JTextField searchField;
    private TableRowSorter<TableModel> sorter;
    private JTable viewEntriesTable;
    private JScrollPane entryListScrollPane;
    
    public MainFrame(final String folderName) {
        super("eJournal - Main Page");
        this.setSize(800, 650);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(0);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (MainFrame.this.checkIfEntriesOpen()) {
                    MainFrame.this.closedJournal();
                    MainFrame.this.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(MainFrame.this.getParent(), "Please close all Entries first, including the New Entry frame", "Journal close failure", 0);
                }
            }
        });
        this.journalName = folderName;
        this.setUpGUI();
        final File file = new File(String.valueOf(this.journalName.substring(9)) + "OpenedEntries.jrn");
        file.deleteOnExit();
        this.setVisible(true);
    }
    
    private void setUpGUI() {
        final JLabel journalLabel = new JLabel(this.journalName.substring(9));
        journalLabel.setBounds(20, 20, this.getWidth(), 50);
        journalLabel.setHorizontalAlignment(0);
        journalLabel.setFont(new Font("Arial", 1, 41));
        this.add(journalLabel);
        (this.newEntryButton = new JButton("New Entry")).setBounds(20, 100, 760, 30);
        this.newEntryButton.addActionListener(this);
        this.newEntryButton.setToolTipText("Click this button to create a new journal entry");
        this.add(this.newEntryButton);
        this.getJournalFiles();
        this.fillTable();
        (this.entryListScrollPane = new JScrollPane(this.viewEntriesTable)).setBounds(20, 150, 760, 400);
        this.add(this.entryListScrollPane);
        final JLabel searchLabel = new JLabel("Search : ");
        searchLabel.setBounds(20, 570, 50, 30);
        searchLabel.setHorizontalAlignment(4);
        this.add(searchLabel);
        (this.searchField = new JTextField()).setBounds(70, 570, 710, 30);
        this.searchField.setToolTipText("You can search through entry titles here");
        this.add(this.searchField);
        this.searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(final DocumentEvent e) {
                MainFrame.this.newFilter();
            }
            
            @Override
            public void insertUpdate(final DocumentEvent e) {
                MainFrame.this.newFilter();
            }
            
            @Override
            public void removeUpdate(final DocumentEvent e) {
                MainFrame.this.newFilter();
            }
        });
    }
    
    private void getJournalFiles() {
        final File userFolder = new File(this.journalName);
        final String[] filesString = userFolder.list();
        this.entriesList = new JournalEntry[filesString.length];
        for (int i = 0; i < filesString.length; ++i) {
            this.entriesList[i] = new JournalEntry(filesString[i], this.journalName);
        }
        Organizer.insertionSort(this.entriesList);
    }
    
    private void fillTable() {
        final Object[][] tableEntries = new Object[this.entriesList.length][3];
        final DateFormat dateFormat = new SimpleDateFormat("yyyy / MM / dd E 'at' HH:mm a");
        for (int i = 0; i < tableEntries.length; ++i) {
            tableEntries[i][0] = this.entriesList[i].getTitle();
            tableEntries[i][1] = dateFormat.format(this.entriesList[i].getDate());
            tableEntries[i][2] = "View";
        }
        final DefaultTableModel dtm = new DefaultTableModel(tableEntries, new String[] { "Title", "Date", "View" }) {
            @Override
            public boolean isCellEditable(final int row, final int col) {
                return col != 0 && col != 1;
            }
        };
        this.viewEntriesTable = new JTable(dtm);
        this.viewEntriesTable.getColumn("View").setCellRenderer(new ButtonRenderer());
        this.viewEntriesTable.getColumn("View").setCellEditor(new ButtonEditor(new JCheckBox(), this));
        this.viewEntriesTable.setFillsViewportHeight(true);
        this.viewEntriesTable.getTableHeader().setReorderingAllowed(false);
        this.sorter = new TableRowSorter<TableModel>(this.viewEntriesTable.getModel());
        this.viewEntriesTable.setRowSorter((RowSorter<? extends TableModel>)this.sorter);
        final String[] toolTipStr = { "Title of the entry: Click on this header to sort by alphabetical order", "Date the entry was created: Click on this header to sort by chronological order", "Click this button to view the selected entry" };
        final ToolTipHeader header = new ToolTipHeader(this.viewEntriesTable.getColumnModel());
        header.setToolTipStrings(toolTipStr);
        this.viewEntriesTable.setTableHeader(header);
    }
    
    private void newFilter() {
        RowFilter<TableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(this.searchField.getText().toLowerCase(), 0);
        }
        catch (PatternSyntaxException e) {
            return;
        }
        this.sorter.setRowFilter(rf);
    }
    
    public String getJournalName() {
        return this.journalName;
    }
    
    public void refresh() {
        new MainFrame(this.journalName);
        this.dispose();
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.newEntryButton) {
            if (this.isEntryOpened("!!!New Entry!!!")) {
                JOptionPane.showMessageDialog(this, "A new entry frame is already open", "New Entry frame Open", 0);
            }
            else {
                new NewEntryFrame(this);
            }
        }
    }
    
    private void closedJournal() {
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
        final int n = openedJournalsList.indexOf(this.journalName.substring(9));
        try {
            openedJournalsList.remove(n);
        }
        catch (ArrayIndexOutOfBoundsException e2) {
            JOptionPane.showMessageDialog(this, "ERROR: we could not find this journal in the list of opened Journals", "Journal search failure", 0);
        }
        IO.createOutputFile("OpenedJournals.jrn");
        for (int i = 0; i < openedJournalsList.size(); ++i) {
            IO.println(openedJournalsList.get(i));
        }
        IO.closeOutputFile();
    }
    
    private boolean checkIfEntriesOpen() {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.journalName.substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "File Creation failure", 0);
        }
        return openedJournalsList.size() == 0;
    }
    
    private boolean isEntryOpened(final String s) {
        final ArrayList<String> openedJournalsList = new ArrayList<String>();
        try {
            IO.openInputFile(String.valueOf(this.journalName.substring(9)) + "OpenedEntries.jrn");
            for (String temp = IO.readLine(); temp != null; temp = IO.readLine()) {
                openedJournalsList.add(temp);
            }
            IO.closeInputFile();
        }
        catch (IOException i) {
            JOptionPane.showMessageDialog(this, "Error in retrieving Info", "Checking File failure", 0);
        }
        final int n = openedJournalsList.indexOf(s);
        return n != -1;
    }
}
