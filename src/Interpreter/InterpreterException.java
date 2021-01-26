package Interpreter;

public class InterpreterException extends Exception
{
    String message;
    int line;

    public InterpreterException(){};
    public InterpreterException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return (message);
    }
}
