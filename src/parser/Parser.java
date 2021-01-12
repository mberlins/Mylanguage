package parser;

import standard.*;
import parser.ASTnode.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Parser
{
    Lexer lexer;
    Token currentToken;

    public Parser(Lexer lexer)
    {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    public Parser() throws FileNotFoundException {
        lexer = new Lexer("C:\\Users\\Admin\\IdeaProjects\\TKOM\\example.txt");
        currentToken = lexer.nextToken();
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
        else if (currentToken.getType() == TokenType.UNKNOWN)
            throw new ParserException("Unsupported type");
        else
            throw new ParserException("Expected other Token type");
    }

    private AST function() throws ParserException
    {
        Token type = currentToken;
        if (currentToken.getType() == TokenType.FUNCTION)
            proceed(type.getType());
        else
            throw new ParserException("Expected other Token type");

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

        if ((statement = functionCallStatement()) != null)
            return statement;
        if ((statement = assignmentStatement()) != null)
            return statement;
        if ((statement = varDeclarationStatement()) != null)
            return statement;
        if ((statement = unitDeclarationStatement()) != null)
            return statement;
        if ((statement = ifStatement()) != null)
            return statement;
        if ((statement = whileStatement()) != null)
            return statement;
        if ((statement = returnStatement()) != null)
            return statement;
        if ((statement = printCallStatement()) != null)
            return statement;

        return null; // todo chyba mozna error
    }

    private AST functionCallStatement(/*Token name*/) throws ParserException
    {
        ArrayList<Character> waitingList = (ArrayList<Character>) lexer.peekCharacters();
        if (currentToken.getType() != TokenType.NAME || waitingList.get(0) != '(')
            return null;

        Token functionName = currentToken;
        ArrayList<AST> arguments = new ArrayList<AST>();

        if(currentToken.getType() == TokenType.RIGHT_PARENTHESIS)
        {
            proceed(TokenType.RIGHT_PARENTHESIS);
            return new FunctionCall(functionName, arguments);
        }

        arguments.add(additiveExpression());
        while (currentToken.getType() == TokenType.COMMA)
        {
            proceed(TokenType.COMMA);
            arguments.add(additiveExpression()); // po wyjsciu current token powinno byc comma
        }
        proceed(TokenType.RIGHT_PARENTHESIS);
        return new FunctionCall(functionName, arguments);
    }

    private AST assignmentStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.NAME)
            return null;

        Token name = currentToken;
        proceed(TokenType.NAME);
        proceed(TokenType.ASSIGNMENT_OP);
        AST additiveExp = additiveExpression();

        return new Assignment(name, additiveExp);
    }

    private AST varDeclarationStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.NAME)
            return null;

        Token name = currentToken;
        AST additiveExp = null;

        proceed(TokenType.NAME);

        if (currentToken.getType() == TokenType.ASSIGNMENT_OP)
            additiveExp = additiveExpression();

        return new VarDeclaration(name, additiveExp);
    }

    private AST unitDeclarationStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.DEF)
            return null;

        Token name, unitField, multiplicity, parentName;

        proceed(TokenType.DEF);
        name = currentToken;
        proceed(TokenType.NAME);
        proceed(TokenType.COLON);

        if(currentToken.getType() == TokenType.NUMBER)
        {
            multiplicity = currentToken;
            proceed(TokenType.NUMBER);
            parentName = currentToken;
            proceed(TokenType.NAME);
            return new Unit(name, multiplicity, parentName);
        }
        else if (currentToken.getType() == TokenType.LEFT_BRACKET )
        {
            proceed(TokenType.LEFT_BRACKET);
            unitField = currentToken;
            proceed(TokenType.NAME);
            proceed(TokenType.RIGHT_BRACKET);
            return new BaseUnit(name, unitField);
        }
        return null;
    }

    private AST ifStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.IF)
            return null;

        AST condition, ifBody, elseBody = null;

        proceed(TokenType.IF);
        proceed(TokenType.LEFT_PARENTHESIS);
        condition = conditionalExpression();
        proceed(TokenType.RIGHT_PARENTHESIS);
        ifBody = functionBody();
        if(currentToken.getType() == TokenType.ELSE)
        {
            proceed(TokenType.ELSE);
            elseBody = functionBody();
        }
        return new IfStatement(condition, ifBody, elseBody);
    }

    private AST whileStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.LOOP)
            return null;

        AST condition, whileBody;
        proceed(TokenType.LOOP);
        proceed(TokenType.LEFT_PARENTHESIS);
        condition = conditionalExpression();
        proceed(TokenType.RIGHT_PARENTHESIS);
        whileBody = functionBody();
        return new WhileStatement(condition, whileBody);
    }

    private AST returnStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.RETURN)
            return null;

        proceed(TokenType.RETURN);
        return new ReturnStatement(assignmentStatement());
    }

    private AST printCallStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.PRINT)
            return null;

        proceed(TokenType.PRINT);
        AST printArgument = basicExpression();
        proceed(TokenType.SEMICOLON);

        return new PrintCall(printArgument);
    }

   private AST conditionalExpression() throws ParserException
   {
       AST node = andCondition();
       while (currentToken.getType() == TokenType.OR_OP)
       {
           Token token = currentToken;
           proceed(TokenType.OR_OP);
           node = new BinLogicOperator(node, token, andCondition());
       }
       return node;
   }

   private AST andCondition() throws ParserException
   {
       AST node = equalityCondition();
       while (currentToken.getType() == TokenType.AND_OP)
       {
           Token token = currentToken;
           proceed(TokenType.AND_OP);
           node = new BinLogicOperator(node, token, equalityCondition());
       }
       return node;
   }

   private AST equalityCondition() throws ParserException
   {
       AST node = relationalCondition();

       while (currentToken.getType() == TokenType.EQUAL_OP || currentToken.getType() == TokenType.UNEQUAL_OP )
       {
           Token token = currentToken;
           proceed(token.getType());
           node = new BinLogicOperator(node, token, relationalCondition());
       }
       return node;
   }

    private AST relationalCondition() throws ParserException
    {
        AST node = primaryCondition();

        while (currentToken.getType() == TokenType.BIGGER || currentToken.getType() == TokenType.BIGGER_EQUAL || currentToken.getType() == TokenType.SMALLER
                || currentToken.getType() == TokenType.SMALLER_EQUAL)
        {
            Token token = currentToken;
            proceed(token.getType());
            node = new BinLogicOperator(node, token, relationalCondition());
        }
        return node;
    }

    private AST primaryCondition() throws ParserException
    {
        Token token = currentToken;

        if (token.getType() == TokenType.LEFT_PARENTHESIS)
        {
            proceed(TokenType.LEFT_PARENTHESIS);
            AST node = conditionalExpression();
            proceed(TokenType.RIGHT_PARENTHESIS);
            return node;
        }
        else
        {
            AST node = additiveExpression();
            return node;
        }
    }

    private AST additiveExpression() throws ParserException
    {
        AST node = multiplicativeExpression();

        while (currentToken.getType() == TokenType.ADDITIVE_OP || currentToken.getType() == TokenType.MINUS_OP)
        {
            Token operator = currentToken;
            proceed(operator.getType());
            node = new BinOperator(node, operator, multiplicativeExpression());
        }
        return node;
    }

    private AST multiplicativeExpression() throws ParserException
    {
        AST node = basicExpression();

        while (currentToken.getType() == TokenType.DIVIDE_OP || currentToken.getType() == TokenType.MULTIPLICATIVE_OP)
        {
            Token operator = currentToken;
            proceed(operator.getType());
            node = new BinOperator(node, operator, basicExpression());
        }
        return node;
    }

    private AST basicExpression() throws ParserException    //todo co z double?
    {
        Token token = currentToken;
        AST leaf;
        if ((leaf = isUnit(token)) != null)
            return leaf;
        if (token.getType() == TokenType.NUMBER)
        {
            proceed(TokenType.NUMBER);
            return leaf = intOrDouble(token);
        }
        else if (token.getType() == TokenType.STRING)
        {
            proceed(TokenType.STRING);
            return new StringVar(token);
        }
        else if (token.getType() == TokenType.LEFT_PARENTHESIS)
        {
            proceed(TokenType.LEFT_PARENTHESIS);
            AST node = additiveExpression();
            proceed(TokenType.RIGHT_PARENTHESIS);
            return node;
        }
        else if (token.getType() == TokenType.NAME)
        {
            token = currentToken;
            proceed(TokenType.NAME);
            try
            {
                proceed(TokenType.LEFT_PARENTHESIS);
                return functionCallStatement();
            }
            catch (ParserException e)
            {
                return variable(token);
            }
        }
        return null; //niech zwraca variable dodatkowa
    }

    private AST intOrDouble(Token token) throws ParserException
    {
        AST node;

        if(token.getIntValue() != Integer.MIN_VALUE)
            return node = new IntNum(token);
        else if (token.getDoubleValue() != Double.MIN_VALUE)
            return node = new DoubleNum(token);

        return null;
    }

    private AST isUnit(Token token) throws ParserException
    {
        if (currentToken.getType() != TokenType.LEFT_BRACKET)
            return null;
        Token number, name;
        proceed(TokenType.LEFT_BRACKET);
        number = currentToken;
        proceed(TokenType.NUMBER);
        name = currentToken;
        proceed(TokenType.NAME);
        proceed(TokenType.RIGHT_BRACKET);

        return new Unit(number, name);
    }

    private AST variable(Token token) throws ParserException
    {
        return new Variable(token);
    }
}
