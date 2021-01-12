package standard;

import parser.Parser;
import parser.ParserException;

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

        Parser parser = new Parser();

        try
        {
            parser.program();
        }
        catch (ParserException e)
        {
            System.out.println(e);
        }

    }
}
