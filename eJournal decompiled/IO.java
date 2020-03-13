import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.PrintWriter;

// 
// Decompiled by Procyon v0.5.36
// 

public class IO
{
    private static PrintWriter fileOut;
    private static BufferedReader fileIn;
    
    public static void createOutputFile(final String fileName) {
        try {
            IO.fileOut = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
        }
        catch (IOException e) {
            System.out.println("*** Cannot create file: " + fileName + " ***");
        }
    }
    
    public static void print(final String text) {
        IO.fileOut.print(text);
    }
    
    public static void println(final String text) {
        IO.fileOut.println(text);
    }
    
    public static void closeOutputFile() {
        IO.fileOut.close();
    }
    
    public static void openInputFile(final String fileName) throws FileNotFoundException {
        IO.fileIn = new BufferedReader(new FileReader(fileName));
    }
    
    public static String readLine() throws IOException {
        return IO.fileIn.readLine();
    }
    
    public static void closeInputFile() throws IOException {
        IO.fileIn.close();
    }
}
