package parser;

import Interpreter.Interpreter;
import Interpreter.InterpreterException;

public interface AST
{
    public void accept(Interpreter visitor) throws InterpreterException;
}
