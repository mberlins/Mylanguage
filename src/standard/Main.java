package standard;

import Interpreter.Interpreter;
import Interpreter.InterpreterException;
import parser.AST;
import parser.Parser;
import parser.ParserException;


import java.io.File;
import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        File file = new File("C:\\Users\\Admin\\IdeaProjects\\TKOM\\exampleTer.txt");

        AST p;
        Parser parser = new Parser(file);
        try
        {
            p = parser.program();

            Interpreter interpreter = new Interpreter(p);
            AST wynik = (AST)interpreter.run();
            System.out.println("--------------");
            if(p.equals(null))
                System.out.println("XD");
        }
        catch (ParserException | InterpreterException e)
        {
            System.out.println(e);
        }

    }
}
