package parser;

import standard.Token;
import standard.TokenType;

import java.util.ArrayList;

public class ASTnode
{
    public static class Program implements AST
    {
        private ArrayList<AST> functions = new ArrayList<AST>();

        public ArrayList<AST> getFunctions() {
            return functions;
        }
        public void addFunction(AST function) {
            functions.add(function);
        }
    }

    public static class FunctionDef implements AST
    {
        Token name;
        AST paramList;
        AST functionBody;

        FunctionDef(Token name, AST paramList, AST functionBody)
        {
            this.name = name;
            this.paramList = paramList;
            this.functionBody = functionBody;
        }
    }

    public static class ParamList implements AST
    {
        ArrayList<AST> names;

        ParamList(ArrayList<AST> names)
        {
            this.names = names;
        }
    }

    public static class FunctionBody implements AST
    {
        ArrayList<AST> statements;

        FunctionBody(ArrayList<AST> statements)
        {
            this.statements = statements;
        }
    }

    public static class Parameter implements AST
    {
        AST variable;

        Parameter(AST variable)
        {
            this.variable = variable;
        }
    }

    public static class Variable implements AST
    {
        Token name;

        Variable(Token name)
        {
            this.name = name;
        }
    }

    public static class FunctionCall implements AST
    {
        Token name;
        ArrayList<AST> arguments;

        public FunctionCall(Token name, ArrayList<AST> arguments)
        {
            this.name = name;
            this.arguments = arguments;
        }
    }


}

