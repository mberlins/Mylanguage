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

    public static class Assignment implements AST
    {
        Token name;
        AST assignmentValue;

        Assignment(Token name, AST addExp)
        {
            this.name = name;
            this.assignmentValue = addExp;
        }
    }

    public static class VarDeclaration extends Assignment
    {
        VarDeclaration(Token name, AST addExp) {
            super(name, addExp);
        }
    }

    public static class IfStatement implements AST
    {
        AST condition, ifBody, elseBody;

        IfStatement(AST condition, AST ifBody, AST elseBody)
        {
            this.condition = condition;
            this.ifBody = ifBody;
            this.elseBody = elseBody;
        }
    }

    public static class WhileStatement implements AST
    {
        AST condition, whileBody;

        WhileStatement(AST condition, AST whileBody)
        {
            this.condition = condition;
            this.whileBody = whileBody;
        }
    }

    public static class ReturnStatement implements AST
    {
        AST retValue;

        ReturnStatement(AST retValue)
        {
            this.retValue = retValue;
        }
    }

    public static class PrintCall implements AST
    {
        AST printCall;

        PrintCall(AST printCall)
        {
            this.printCall = printCall;
        }
    }

    public static class BinOperator implements AST{
        public AST right, left;
        public Token operation;

        public BinOperator(AST left, Token operation, AST right)
        {
            this.right = right;
            this.left = left;
            this.operation = operation;
        }
    }

    public static class BinLogicOperator implements AST
    {
        public AST right, left;
        public Token operation;

        public BinLogicOperator(AST left, Token operation, AST right) {
            this.right = right;
            this.left = left;
            this.operation = operation;
        }
    }

    public static class NumberTest implements AST
    {
        Token expression;

        public NumberTest(Token expression)
        {
            this.expression = expression;
        }
    }

    public static class IntNum implements AST
    {
        Integer value;
        IntNum(Token token)
        {
            this.value = token.getIntValue();
        }
    }

    public static class DoubleNum implements AST
    {
        Double value;
        DoubleNum(Token token)
        {
            this.value = token.getDoubleValue();
        }
    }

    public static class StringVar implements AST
    {
        String value;
        StringVar(Token token)
        {
            this.value = token.getValue();
        }
    }

    public static class Unit implements AST
    {
        Token name;
        Token number;
        Token parentName;

        Unit(Token name, Token number, Token parent)
        {
            this.name = name;
            this.number = number;
            this.parentName = parent;
        }

        Unit(Token name, Token number)
        {
            this.name = name;
            this.number = number;
        }
    }

    public static class BaseUnit implements AST
    {
        Token name;
        Token unitField;

        BaseUnit(Token name, Token unitField)
        {
            this.name = name;
            this.unitField = unitField;
        }
    }

}

