package Test;

import org.junit.jupiter.api.Test;
import standard.Lexer;
import standard.Token;
import standard.TokenType;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void test1() throws Exception {
        int testINT = 1;
        assertEquals(1, testINT);
    }

    @Test
    void IFtest()throws Exception
    {
        Lexer lexer = new Lexer("if");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.IF);
        assertEquals("if", token.getValue());
    }

    @Test
    void ELSEtest() throws Exception
    {
        Lexer lexer = new Lexer("else");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.ELSE);
        assertEquals("else", token.getValue());
    }

    @Test
    void WHILEtest() throws Exception
    {
        Lexer lexer = new Lexer("while");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LOOP);
        assertEquals("while", token.getValue());
    }

    @Test
    void RETURNtest() throws Exception
    {
        Lexer lexer = new Lexer("return");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RETURN);
        assertEquals("return", token.getValue());
    }

    @Test
    void DEFtest() throws Exception
    {
        Lexer lexer = new Lexer("def");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.DEF);
        assertEquals("def", token.getValue());
    }

    @Test
    void VARtest() throws Exception
    {
        Lexer lexer = new Lexer("var");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.VAR);
        assertEquals("var", token.getValue());
    }

    @Test
    void FUNCTIONtest() throws Exception
    {
        Lexer lexer = new Lexer("function");
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.FUNCTION);
        assertEquals("function", token.getValue());
    }

    @Test
    void MULTIPLICATIVEtest() throws Exception
    {
        Lexer lexer = new Lexer("*");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.MULTIPLICATIVE_OP);
        assertEquals("*", token.getValue());
    }

    @Test
    void DIVIDEtest() throws Exception
    {
        Lexer lexer = new Lexer("/");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.DIVIDE_OP);
        assertEquals("/", token.getValue());
    }

    @Test
    void ADDITIVEtest() throws Exception
    {
        Lexer lexer = new Lexer("+");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.ADDITIVE_OP);
        assertEquals("+", token.getValue());
    }

    @Test
    void MINUStest() throws Exception
    {
        Lexer lexer = new Lexer("-");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.MINUS_OP);
        assertEquals("-", token.getValue());
    }

    @Test
    void COLONtest() throws Exception
    {
        Lexer lexer = new Lexer(":");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.COLON);
        assertEquals(":", token.getValue());
    }

    @Test
    void SEMICOLONtest() throws Exception
    {
        Lexer lexer = new Lexer(";");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.SEMICOLON);
        assertEquals(";", token.getValue());
    }

    @Test
    void COMMAtest() throws Exception
    {
        Lexer lexer = new Lexer(",");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.COMMA);
        assertEquals(",", token.getValue());
    }

    @Test
    void LEFTPARENTHESIStest() throws Exception
    {
        Lexer lexer = new Lexer("(");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LEFT_PARENTHESIS);
        assertEquals("(", token.getValue());
    }

    @Test
    void RIGHTPARENTHESIStest() throws Exception
    {
        Lexer lexer = new Lexer(")");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RIGHT_PARENTHESIS);
        assertEquals(")", token.getValue());
    }

    @Test
    void LEFTBRACKETtest() throws Exception
    {
        Lexer lexer = new Lexer("[");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LEFT_BRACKET);
        assertEquals("[", token.getValue());
    }

    @Test
    void RIGHTBRACKETtest() throws Exception
    {
        Lexer lexer = new Lexer("]");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RIGHT_BRACKET);
        assertEquals("]", token.getValue());
    }

    @Test
    void LEFTBRACETtest() throws Exception
    {
        Lexer lexer = new Lexer("{");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LEFT_BRACE);
        assertEquals("{", token.getValue());
    }

    @Test
    void RIGHTBRACETtest() throws Exception
    {
        Lexer lexer = new Lexer("}");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RIGHT_BRACE);
        assertEquals("}", token.getValue());
    }

    @Test
    void AND_OPtest() throws Exception
    {
        Lexer lexer = new Lexer("&");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.AND_OP);
        assertEquals("&", token.getValue());
    }

    @Test
    void OR_OPtest() throws Exception
    {
        Lexer lexer = new Lexer("|");

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.OR_OP);
        assertEquals("|", token.getValue());
    }

    @Test
    void STRINGtest() throws Exception
    {
        Lexer lexer = new Lexer("\"message\"");
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("message", token.getValue());
    }

    @Test
    void BADSTRINGtest() throws Exception
    {
        Lexer lexer = new Lexer("\"message");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("message", token.getValue());
    }

    @Test
    void STRINGINSIDEtest() throws Exception                                // cudzyslow w stringu obsluzony
    {
        Lexer lexer = new Lexer("\"abc\\\"de\"" );

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("abc\"de", token.getValue());
    }

    @Test
    void EQUALtest() throws Exception
    {
        Lexer lexer = new Lexer("== =");

        Token token = lexer.nextToken();
        assertEquals(TokenType.EQUAL_OP, token.getType());
        assertEquals("==", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.ASSIGNMENT_OP, token.getType());
        assertEquals("=", token.getValue());
    }

    @Test
    void BIGGER_EQUALtest() throws Exception
    {
        Lexer lexer = new Lexer(">=");

        Token token = lexer.nextToken();
        assertEquals(TokenType.BIGGER_EQUAL, token.getType());
        assertEquals(">=", token.getValue());
    }

    @Test
    void SMALLER_EQUALtest() throws Exception
    {
        Lexer lexer = new Lexer("<=");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SMALLER_EQUAL, token.getType());
        assertEquals("<=", token.getValue());
    }

    @Test
    void BIGGERtest() throws Exception
    {
        Lexer lexer = new Lexer(">");

        Token token = lexer.nextToken();
        assertEquals(TokenType.BIGGER, token.getType());
        assertEquals(">", token.getValue());
    }

    @Test
    void SMALLERtest() throws Exception
    {
        Lexer lexer = new Lexer("<");

        Token token = lexer.nextToken();
        assertEquals(TokenType.SMALLER, token.getType());
        assertEquals("<", token.getValue());
    }

    @Test
    void Xtest() throws Exception
    {
        Lexer lexer = new Lexer(" < x");

        Token /*token = lexer.nextToken();
        assertEquals(TokenType.NAME, token.getType());
        assertEquals("x", token.getValue());*/
        token = lexer.nextToken();
        assertEquals(TokenType.SMALLER, token.getType());
        assertEquals("<", token.getValue());
        token = lexer.nextToken();
        assertEquals(TokenType.NAME, token.getType());
        assertEquals("x", token.getValue());
    }

    /*@Test
    void COMMENTtest() throws Exception
    {
        Lexer lexer = new Lexer("//");

        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMENT, token.getType());
        assertEquals("//", token.getValue());
    }*/



    @Test
    void ZEROtest() throws Exception
    {
        Lexer lexer = new Lexer("0");

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals(0, token.getNumValue());
    }

    @Test
    void INTtest() throws Exception
    {
        Lexer lexer = new Lexer("11");

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals(11, token.getIntValue());
    }

    @Test
    void DOUBLEtest() throws Exception
    {
        Lexer lexer = new Lexer("1.1");

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals(1.1, token.getNumValue());
    }

    @Test
    void ZERODOUBLEtest() throws Exception
    {
        Lexer lexer = new Lexer("0.5");

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals(0.5, token.getNumValue());
    }

    @Test
    void DOUBLEBIStest() throws Exception
    {
        Lexer lexer = new Lexer("13.1");

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals(13.1, token.getNumValue());
    }

    @Test
    void WRONGDOUBLEtest() throws Exception
    {
        Lexer lexer = new Lexer("7.5m");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("7.5m", token.getValue());
    }

    @Test
    void WRONGZEROtest() throws Exception
    {
        Lexer lexer = new Lexer("0m");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("0m", token.getValue());
    }

    @Test
    void WRONGDOUBLEZEROtest() throws Exception
    {
        Lexer lexer = new Lexer("0.1m");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("0.1m", token.getValue());
    }

    @Test
    void WRONGDOUBLEZEOBIStest() throws Exception
    {
        Lexer lexer = new Lexer("00");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("00", token.getValue());
    }

    @Test
    void WRONGDOUBLEZEROTERtest() throws Exception
    {
        Lexer lexer = new Lexer("0.0m");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("0.0m", token.getValue());
    }

    @Test
    void WRONGDOUBLEBIStest() throws Exception
    {
        Lexer lexer = new Lexer("1.1.1");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("1.1.1", token.getValue());
    }

    @Test
    void WRONGDOUBLETERtest() throws Exception                                      // po zmianie na tworzenie liczb od razu jako double zawsze wrzuca 0 po kropce
    {                                                                               // chyba bez znaczenia jesli i tak wychodzi blad
        Lexer lexer = new Lexer("1..1");

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("1.0.1", token.getValue());
    }

    @Test
    void POSITIONtest() throws Exception
    {
        Lexer lexer = new Lexer("12.1m2 0.0mm 1\r\n2");
        Token token = lexer.nextToken();

        token = lexer.nextToken();
        token = lexer.nextToken();
        assertEquals(14, token.getX_coor());
        assertEquals(0, token.getY_coor());
        token = lexer.nextToken();
        token = lexer.nextToken();
        assertEquals(1, token.getY_coor());
        token = lexer.nextToken();
        assertEquals(1, token.getX_coor());
    }
}
