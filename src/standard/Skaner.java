package standard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Skaner
{
    private File code;
    private Character buffer;
    private Scanner scanner;

    public Character getBuffer() {
        return buffer;
    }

    public Skaner (String file_loc)
    {
        try
        {
            code = new File(file_loc);
            scanner = new Scanner(code);
            scanner.useDelimiter("");
        }
        catch (FileNotFoundException e)
        {
            scanner = new Scanner(file_loc);
            scanner.useDelimiter("");
            //e.printStackTrace();
        }

    }

    /*public Skaner (String message, int i)
    {
        scanner = new Scanner(message);
        scanner.useDelimiter("");
    }*/

    public char readNextChar()
    {
        if (!scanner.hasNext())
            return '\u001a';
        else
        {
            buffer = scanner.next().charAt(0);
            return buffer;
        }
    }
}
