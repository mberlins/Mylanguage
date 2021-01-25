package parser;

import standard.*;
import parser.ASTnode.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Parser
{
    Lexer lexer;
    ArrayList<Token> currentTokens =  new ArrayList<>();;
    Token currentToken;

    public Parser(File file) throws FileNotFoundException {
        lexer = new Lexer(file);

        currentToken = lexer.nextToken();

    }

    public Parser(String code) {
        lexer = new Lexer(code);
        currentToken = lexer.nextToken();
    }

    public AST program() throws ParserException
    {
        //int x = 2 2;          // java chce srednika po dwojce
        AST program = new Program();
        while (currentToken.getType() != TokenType.EOF)
        {
            AST f = function();
            ((Program) program).addFunction( ((ASTnode.FunctionDef) f).getName().getValue() , f);
        }
        return program;
    }

    private void proceed(TokenType type) throws ParserException
    {
        if (currentToken.getType() == type)
        {
            if (currentTokens.size() > 0)
            {
                currentToken = currentTokens.get(0);
                currentTokens.remove(0);
            }
            else
                currentToken = lexer.nextToken();
            return;
        }
        else if (currentToken.getType() == TokenType.UNKNOWN)
            throw new ParserException("Unsupported type", currentToken.getX_coor(), currentToken.getY_coor());
        else
        {
            String message = "Expected " + type.name();
            throw new ParserException(message , currentToken.getX_coor(), currentToken.getY_coor());
        }
    }

    private AST function() throws ParserException
    {
        Token type = currentToken;
        if (currentToken.getType() == TokenType.FUNCTION)
            proceed(type.getType());
        else
            throw new ParserException("Expected wrong Token type", currentToken.getX_coor(), currentToken.getY_coor());

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
        functionParameters.add(new Variable(token));

        while(currentToken.getType() == TokenType.COMMA)
        {
            proceed(TokenType.COMMA);
            token = currentToken;
            if(token.getType() == TokenType.NAME)
            {
                functionParameters.add(new Variable(token));
                proceed(TokenType.NAME);
            }
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

    private ArrayList<AST> statementList() throws ParserException
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
        else if ((statement = varDeclarationStatement()) != null)
            return statement;
        else if ((statement = unitDeclarationStatement()) != null)
            return statement;
        else if ((statement = ifStatement()) != null)
            return statement;
        else if ((statement = whileStatement()) != null)
            return statement;
        else if ((statement = returnStatement()) != null)
            return statement;
        else if ((statement = printCallStatement()) != null)
            return statement;

        return null;
    }

    private AST functionCallStatement() throws ParserException
    {
        //ArrayList<Character> waitingList = (ArrayList<Character>) lexer.peekCharacters();
        currentTokens.add(lexer.nextToken());
        if (currentToken.getType() != TokenType.NAME || currentTokens.get(0).getType() != TokenType.LEFT_PARENTHESIS)
            return null;

        Token functionName = currentToken;
        proceed(TokenType.NAME);
        proceed(TokenType.LEFT_PARENTHESIS);
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
        //proceed(TokenType.NAME);
        AST var = variable();
        proceed(TokenType.ASSIGNMENT_OP);
        AST additiveExp = additiveExpression();

        return new Assignment(var, additiveExp);
    }

    private AST varDeclarationStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.VAR)
            return null;

        proceed(TokenType.VAR);
        AST name = variable();
        AST additiveExp = null;

        //proceed(TokenType.NAME);

        if (currentToken.getType() == TokenType.ASSIGNMENT_OP)
        {
            proceed(TokenType.ASSIGNMENT_OP);
            additiveExp = additiveExpression();
        }

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
        return new ReturnStatement(additiveExpression());
    }

    private AST printCallStatement() throws ParserException
    {
        if (currentToken.getType() != TokenType.PRINT)
            return null;

        proceed(TokenType.PRINT);
        AST printArgument = additiveExpression();
        //proceed(TokenType.SEMICOLON);

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

    private AST basicExpression() throws ParserException
    {
        Token token = currentToken;
        AST leaf = null;
        if ((leaf = isUnit()) != null)
            return leaf;
        if (token.getType() == TokenType.MINUS_OP)
        {
            proceed(TokenType.MINUS_OP);
            return new UnOperator(token, basicExpression());
        }
        if (token.getType() == TokenType.ADDITIVE_OP)
        {
            proceed(TokenType.ADDITIVE_OP);
            return new UnOperator(token, basicExpression());
        }
        if (currentToken.getType() == TokenType.NUMBER)
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
        else if ((leaf = functionCallStatement()) != null)
            return leaf;
        else if ((leaf = variable()) != null)
            return leaf;

        throw new ParserException("Unknown Token Type" , currentToken.getX_coor(), currentToken.getY_coor());
        //return variable();
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

    private AST isUnit() throws ParserException
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

        return new UnitResult(number, name);
    }

    private AST variable() throws ParserException
    {
        if( currentToken.getType() != TokenType.NAME)
            return null;

        AST var = new Variable(currentToken);
        proceed(TokenType.NAME);
        return var;
    }
}
