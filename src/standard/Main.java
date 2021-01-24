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
	    //Lexer lexer = new Lexer("C:\\Users\\Admin\\IdeaProjects\\TKOM\\exampleTer.txt");
        //Lexer lexer = new Lexer("To jest var message = 1.5;", 0);
	    Token token;

	    /*while(true)
        {
            token = lexer.nextToken();

            if (token.getType() == TokenType.EOF)
                break;

                /*System.out.print(token.getValue());
                System.out.print("   ");
                System.out.print(token.getType());
                System.out.print("   ");
                System.out.print(token.getX_coor());
                System.out.print("   ");
                System.out.println(token.getY_coor());
                System.out.print("   ");


        }*/

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
