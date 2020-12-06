package standard;

import java.io.FileNotFoundException;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
	    Lexer lexer = new Lexer("C:\\Users\\Admin\\IdeaProjects\\TKOM\\exampleBis.txt");
	    Token token;

	    while(true)
        {
            token = lexer.nextToken();

            if (token.getType() == TokenType.EOF)
                break;

            if (token.getType()!=TokenType.SPACE && token.getType()!=TokenType.EOL )
            {
                System.out.print(token.getValue());
                System.out.print(" ");
                System.out.println(token.getType());
            }

        }
    }
}
