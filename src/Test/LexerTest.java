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
        Lexer lexer = new Lexer("if", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.IF);
        assertEquals("if", token.getValue());
    }

    @Test
    void ELSEtest() throws Exception
    {
        Lexer lexer = new Lexer("else", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.ELSE);
        assertEquals("else", token.getValue());
    }

    @Test
    void WHILEtest() throws Exception
    {
        Lexer lexer = new Lexer("while", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LOOP);
        assertEquals("while", token.getValue());
    }

    @Test
    void RETURNtest() throws Exception
    {
        Lexer lexer = new Lexer("return", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RETURN);
        assertEquals("return", token.getValue());
    }

    @Test
    void DEFtest() throws Exception
    {
        Lexer lexer = new Lexer("def", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.DEF);
        assertEquals("def", token.getValue());
    }

    @Test
    void VARtest() throws Exception
    {
        Lexer lexer = new Lexer("var", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.VAR);
        assertEquals("var", token.getValue());
    }

    @Test
    void FUNCTIONtest() throws Exception
    {
        Lexer lexer = new Lexer("function", 0);
        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.FUNCTION);
        assertEquals("function", token.getValue());
    }

    @Test
    void MULTIPLICATIVEtest() throws Exception
    {
        Lexer lexer = new Lexer("*", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.MULTIPLICATIVE_OP);
        assertEquals("*", token.getValue());
    }

    @Test
    void DIVIDEtest() throws Exception
    {
        Lexer lexer = new Lexer("/", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.DIVIDE_OP);
        assertEquals("/", token.getValue());
    }

    @Test
    void ADDITIVEtest() throws Exception
    {
        Lexer lexer = new Lexer("+", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.ADDITIVE_OP);
        assertEquals("+", token.getValue());
    }

    @Test
    void MINUStest() throws Exception
    {
        Lexer lexer = new Lexer("-", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.MINUS_OP);
        assertEquals("-", token.getValue());
    }

    @Test
    void COLONtest() throws Exception
    {
        Lexer lexer = new Lexer(":", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.COLON);
        assertEquals(":", token.getValue());
    }

    @Test
    void SEMICOLONtest() throws Exception
    {
        Lexer lexer = new Lexer(";", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.SEMICOLON);
        assertEquals(";", token.getValue());
    }

    @Test
    void COMMAtest() throws Exception
    {
        Lexer lexer = new Lexer(",", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.COMMA);
        assertEquals(",", token.getValue());
    }

    @Test
    void LEFTPARENTHESIStest() throws Exception
    {
        Lexer lexer = new Lexer("(", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LEFT_PARENTHESIS);
        assertEquals("(", token.getValue());
    }

    @Test
    void RIGHTPARENTHESIStest() throws Exception
    {
        Lexer lexer = new Lexer(")", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RIGHT_PARENTHESIS);
        assertEquals(")", token.getValue());
    }

    @Test
    void LEFTBRACKETtest() throws Exception
    {
        Lexer lexer = new Lexer("[", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LEFT_BRACKET);
        assertEquals("[", token.getValue());
    }

    @Test
    void RIGHTBRACKETtest() throws Exception
    {
        Lexer lexer = new Lexer("]", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RIGHT_BRACKET);
        assertEquals("]", token.getValue());
    }

    @Test
    void LEFTBRACETtest() throws Exception
    {
        Lexer lexer = new Lexer("{", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.LEFT_BRACE);
        assertEquals("{", token.getValue());
    }

    @Test
    void RIGHTBRACETtest() throws Exception
    {
        Lexer lexer = new Lexer("}", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.RIGHT_BRACE);
        assertEquals("}", token.getValue());
    }

    @Test
    void AND_OPtest() throws Exception
    {
        Lexer lexer = new Lexer("&", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.AND_OP);
        assertEquals("&", token.getValue());
    }

    @Test
    void OR_OPtest() throws Exception
    {
        Lexer lexer = new Lexer("|", 0);

        Token token = lexer.nextToken();
        assertEquals(token.getType(), TokenType.OR_OP);
        assertEquals("|", token.getValue());
    }

    @Test
    void STRINGtest() throws Exception
    {
        Lexer lexer = new Lexer("\"message\"", 0);
        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("message", token.getValue());
    }

    @Test
    void BADSTRINGtest() throws Exception
    {
        Lexer lexer = new Lexer("\"message", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("message", token.getValue());
    }

    /*@Test
    void STRINGINSIDEtest() throws Exception
    {
        Lexer lexer = new Lexer("\"message\"\" " , 0);      //dodac escapowanie symboli np jesli w stringu mamy \" to czyta jako cudzyslow

        Token token = lexer.nextToken();
        assertEquals(TokenType.STRING, token.getType());
        assertEquals("message", token.getValue());
        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("\"", token.getValue());
    }

    @Test
    void SPACEtest() throws Exception
    {
        Lexer lexer = new Lexer("\t" , 0);      // TODO wyrzucic biale znaki

        Token token = lexer.nextToken();
        assertEquals(TokenType.SPACE, token.getType());
        assertEquals(" ", token.getValue());
        token = lexer.nextToken();
        assertEquals(TokenType.SPACE, token.getType());
        assertEquals(" ", token.getValue());
    }*/

    @Test
    void EQUALtest() throws Exception
    {
        Lexer lexer = new Lexer("== =", 0);

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
        Lexer lexer = new Lexer(">=", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.BIGGER_EQUAL, token.getType());
        assertEquals(">=", token.getValue());
    }

    @Test
    void SMALLER_EQUALtest() throws Exception
    {
        Lexer lexer = new Lexer("<=", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.SMALLER_EQUAL, token.getType());
        assertEquals("<=", token.getValue());
    }

    @Test
    void BIGGERtest() throws Exception
    {
        Lexer lexer = new Lexer(">", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.BIGGER, token.getType());
        assertEquals(">", token.getValue());
    }

    @Test
    void SMALLERtest() throws Exception
    {
        Lexer lexer = new Lexer("<", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.SMALLER, token.getType());
        assertEquals("<", token.getValue());
    }

    @Test
    void COMMENTtest() throws Exception
    {
        Lexer lexer = new Lexer("//", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.COMMENT, token.getType());
        assertEquals("//", token.getValue());
    }

    @Test
    void NUMBERtest() throws Exception
    {
        Lexer lexer = new Lexer("0 11 1.1 0.1 11.1", 0);

        Token token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals("0", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals("11", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals("1.1", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals("0.1", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.NUMBER, token.getType());
        assertEquals("11.1", token.getValue());
    }

    @Test
    void WRONGNUMBERtest() throws Exception
    {
        Lexer lexer = new Lexer("7.5m 0m 0.1m 0m.m 00 1.1.1 1..1", 0);
        Token token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("7.5m", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("0m", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("0.1m", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("0m.m", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("00", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("1.1.1", token.getValue());

        token = lexer.nextToken();
        assertEquals(TokenType.UNKNOWN, token.getType());
        assertEquals("1..1", token.getValue());
    }

    @Test
    void POSITIONtest() throws Exception
    {
        Lexer lexer = new Lexer("7.5m 0m 0.1m\r\n2", 0);
        Token token = lexer.nextToken();

        token = lexer.nextToken();
        token = lexer.nextToken();
        assertEquals(12, token.getX_coor());
        assertEquals(0, token.getY_coor());
        token = lexer.nextToken();
        token = lexer.nextToken();
        assertEquals(1, token.getY_coor());
        token = lexer.nextToken();
        assertEquals(1, token.getX_coor());
    }
}
