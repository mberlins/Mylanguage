package parser;

public class ParserException extends Exception
{
    String message;
    int position;
    double line;

    public ParserException(){};
    public ParserException(String message, int position, int line)
    {
        this.message = message;
        this.line = line;
        this.position = position;
    }

    @Override
    public String toString()
    {
        return (message + " at line: " + line + ", position: " + position);
    }
}
