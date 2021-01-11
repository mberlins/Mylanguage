package parser;

import standard.*;
import parser.ASTnode.*;

import java.util.ArrayList;

public class Parser
{
    Lexer lexer;
    Token currentToken;                 // todo przyjrzyj sie astParse

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public AST program() throws ParserException
    {
        AST program = new Program();
        while (currentToken.getType() != TokenType.EOF)
        {
            ((Program) program).addFunction(function());
        }
        return program;
    }

    private void proceed(TokenType type) throws ParserException
    {
        if (currentToken.getType() == type)
        {
            currentToken = lexer.nextToken();
            return;
        }
    }

    private AST function() throws ParserException
    {
        Token type = currentToken;
        if (currentToken.getType() == TokenType.FUNCTION)
            proceed(type.getType());
        else
        {
            //todo errors
        }
        Token name = currentToken;
        proceed(TokenType.NAME);
        AST paramList = paramList();
        AST functionBody = functionBody();

        return new FunctionDef(name, paramList, functionBody);
    }

    private AST paramList() throws ParserException
    {
        proceed(TokenType.LEFT_PARENTHESIS);
        if(currentToken.getType() == TokenType.RIGHT_PARENTHESIS)
        {
            proceed(TokenType.RIGHT_PARENTHESIS);
            return null;
        }

        Token token = currentToken;
        proceed(TokenType.NAME);
        ArrayList<AST> functionParameters = new ArrayList<AST>();
        functionParameters.add(new Parameter(new Variable(token)));

        while(currentToken.getType() == TokenType.COMMA)
        {
            proceed(TokenType.COMMA);
            token = currentToken;
            if(token.getType() == TokenType.NAME)
                proceed(TokenType.NAME);
            else
            {
                // todo error
            }
            functionParameters.add(new Parameter(new Variable(token)));
        }
        proceed(TokenType.RIGHT_PARENTHESIS);
        return new ParamList(functionParameters);
    }

    private AST functionBody() throws ParserException
    {
        proceed(TokenType.LEFT_BRACE);
        ArrayList<AST> statements = statementList();
        proceed(TokenType.RIGHT_BRACE);
        // AST root = new functionBody(statements) - czy zwrocic
        return new FunctionBody(statements);
    }

    private ArrayList<AST> statementList() throws ParserException  // todo wrzucic to do function block
    {
        ArrayList<AST> statements = new ArrayList<AST>();

        while (currentToken.getType() != TokenType.RIGHT_BRACE)
        {
            statements.add(statement());
            proceed(TokenType.SEMICOLON);
        }
        return statements;
    }

    private AST statement() throws ParserException
    {
        AST statement;

       if (currentToken.getType() == TokenType.NAME)        // todo zmienic na switcha
       {
           Token functionName = currentToken;
           proceed(TokenType.NAME);
           try{
               proceed(TokenType.LEFT_PARENTHESIS);
               statement = functionCallStatement(functionName);  //todo zmienic na return functionCall() etc w innych miejscach
               return statement;
           }
           catch(ParserException e)
           {
                statement = assignmentStatement();
                return statement;
           }
       }
       else if (currentToken.getType() == TokenType.VAR)
       {
            statement = varDeclarationStatement();
            return statement;
       }
       else if (currentToken.getType() == TokenType.DEF)
       {
           statement = unitDeclarationStatement();
           return statement;
       }
       else if (currentToken.getType() == TokenType.IF)
       {
           statement = ifStatement();
           return statement;
       }
       else if (currentToken.getType() == TokenType.LOOP)
       {
           statement = whileStatement();
           return statement;
       }
       else if (currentToken.getType() == TokenType.RETURN)
       {
           statement = returnStatement();
           return statement;
       }
       else if (currentToken.getType() == TokenType.PRINT)
       {
           statement = printCallStatement();
           return statement;
       }

       return null; // todo tutaj chyba mozna wyrzucac blad
    }

    private AST functionCallStatement(Token name) throws ParserException
    {
        Token functionName = name;
        ArrayList<AST> arguments = new ArrayList<AST>();

        if(currentToken.getType() == TokenType.RIGHT_PARENTHESIS)
        {
            proceed(TokenType.RIGHT_PARENTHESIS);
            return new FunctionCall(functionName, arguments);
        }

        arguments.add(basicExpression());
        while (currentToken.getType() == TokenType.COMMA)
        {
            proceed(TokenType.COMMA);
            arguments.add(basicExpression()); // po wyjsciu current token powinno byc comma
        }
        proceed(TokenType.RIGHT_PARENTHESIS);
        return new FunctionCall(functionName, arguments);
    }
}
