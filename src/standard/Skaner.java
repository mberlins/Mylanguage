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

    public Skaner (String file_loc) throws FileNotFoundException
    {
        code = new File(file_loc);
        scanner = new Scanner(code);
        scanner.useDelimiter("");
    }

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
