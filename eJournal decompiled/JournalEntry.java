import java.io.File;
import java.io.IOException;
import java.util.Date;

// 
// Decompiled by Procyon v0.5.36
// 

public class JournalEntry implements Comparable
{
    String title;
    String entry;
    String folder;
    Date date;
    Date lastModifiedDate;
    
    public JournalEntry(final String t, final String e, final String f) {
        this.title = t;
        this.entry = e;
        this.date = new Date();
        this.folder = f;
        this.lastModifiedDate = this.date;
    }
    
    public JournalEntry(final String fileName, final String folderName) {
        this.entry = "";
        String dateString = "";
        this.folder = folderName;
        this.title = fileName;
        final int numLines = this.getNumLines();
        try {
            IO.openInputFile(String.valueOf(this.folder) + "/" + fileName);
            dateString = IO.readLine();
            for (int i = 1; i < numLines - 1; ++i) {
                this.entry = String.valueOf(this.entry) + IO.readLine() + "\n";
            }
            this.entry = String.valueOf(this.entry) + IO.readLine();
            IO.closeInputFile();
        }
        catch (IOException e) {
            this.title = "Error";
            this.entry = "File Not Found";
        }
        this.lastModifiedDate = new Date(new File(String.valueOf(this.folder) + "/" + fileName).lastModified());
        try {
            this.date = new Date(Long.parseLong(dateString));
        }
        catch (NumberFormatException e2) {
            this.date = new Date();
        }
    }
    
    public String getTitle() {
        return this.title.toLowerCase().substring(0, this.title.length() - 4);
    }
    
    public String getEntry() {
        return this.entry;
    }
    
    public Date getDate() {
        return this.date;
    }
    
    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }
    
    public String getFolder() {
        return this.folder;
    }
    
    public void setTitle(final String t) {
        final File oldTitle = new File(String.valueOf(this.folder) + "/" + this.title);
        final File newTitle = new File(String.valueOf(this.folder) + "/" + t);
        if (!oldTitle.renameTo(newTitle)) {}
        newTitle.delete();
        oldTitle.renameTo(newTitle);
        this.title = t;
    }
    
    public void setEntry(final String e) {
        this.entry = e;
    }
    
    public void delete() {
        final File file = new File(String.valueOf(this.folder) + "/" + this.title);
        final boolean sucess = file.delete();
        if (!sucess) {
            System.out.println("Could not delete file : " + this.folder + "/" + this.title);
        }
    }
    
    public static void saveEntry(final String t, final String e, final String f) {
        final Date date = new Date();
        IO.createOutputFile(String.valueOf(f) + "/" + t);
        IO.println(new StringBuilder(String.valueOf(date.getTime())).toString());
        IO.print(e);
        IO.closeOutputFile();
    }
    
    public void saveEntry() {
        IO.createOutputFile(String.valueOf(this.folder) + "/" + this.title);
        IO.println(new StringBuilder(String.valueOf(this.date.getTime())).toString());
        IO.print(this.entry);
        IO.closeOutputFile();
        this.lastModifiedDate = new Date(new File(String.valueOf(this.folder) + "/" + this.title).lastModified());
    }
    
    @Override
    public String toString() {
        return "Folder : " + this.folder + "Date : " + this.date + ", Last mofied date : " + this.lastModifiedDate + ", Title : " + this.title + ", Entry : " + this.entry;
    }
    
    @Override
    public int compareTo(final Object journal) {
        return this.date.compareTo(((JournalEntry)journal).date) * -1;
    }
    
    private int getNumLines() {
        int numLines = 0;
        try {
            IO.openInputFile(String.valueOf(this.folder) + "/" + this.title);
            for (String line = IO.readLine(); line != null; line = IO.readLine()) {
                ++numLines;
            }
            IO.closeInputFile();
        }
        catch (IOException e) {
            System.out.println("Could not read the number of Lines");
        }
        return numLines;
    }
}
