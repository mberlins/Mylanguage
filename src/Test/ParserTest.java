package Test;

import org.junit.jupiter.api.Test;
import parser.AST;
import parser.ASTnode;
import parser.Parser;
import parser.ParserException;
import standard.TokenType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest
{
    Parser parser = new Parser("function fun (abc, efg)\n" +
            "{\n" +
            "    var x = [2 kg];\n" +
            "    def m: [LENGTH];\n" +
            "    def kg: 1000 g;\n" +
            "    fun();\n" +
            "    x = 7.0;\n" +
            "    y = \"string\";\n" +
            "    z = 2 + 2 * 2 + (7 + 1);\n" +
            "\n" +
            "    return 5;\n" +
            "    fun( x, 5);\n" +
            "}");

    AST program = parser.program();

    ParserTest() throws ParserException, FileNotFoundException {
    }

    ArrayList<AST> statements = ((ASTnode.FunctionBody)((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getFunctionBody()).getStatements();

    @Test
    void functionParamList() throws ParserException
    {
        assertEquals("abc", ((ASTnode.Variable)((ASTnode.ParamList)((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getParamList()).getNames().get(0)).getName().getValue());
        assertEquals(TokenType.NAME, ((ASTnode.Variable)((ASTnode.ParamList)((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getParamList()).getNames().get(0)).getName().getType());
        assertEquals("efg", ((ASTnode.Variable)((ASTnode.ParamList)((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getParamList()).getNames().get(1)).getName().getValue());
        assertEquals(TokenType.NAME, ((ASTnode.Variable)((ASTnode.ParamList)((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getParamList()).getNames().get(1)).getName().getType());
    }

    @Test
    void functionHeader() throws ParserException
    {
        assertEquals("fun", (((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getName().getValue()));
        assertEquals(TokenType.NAME, (((ASTnode.FunctionDef)(((ASTnode.Program) program).getFunctions().get("fun"))).getName().getType()));
    }

    @Test
    void varStatement() throws ParserException
    {
        assertEquals(ASTnode.VarDeclaration.class, statements.get(0).getClass());
    }

    @Test
    void unitStatement() throws ParserException
    {
        assertEquals(ASTnode.BaseUnit.class, statements.get(1).getClass());
    }

    @Test
    void baseUnitStatement() throws ParserException
    {
        assertEquals(ASTnode.Unit.class, statements.get(2).getClass());
    }

    @Test
    void functionCallStatement() throws ParserException
    {
        assertEquals(ASTnode.FunctionCall.class, statements.get(3).getClass());
    }

    @Test
    void assigmentStatement() throws ParserException
    {
        assertEquals(ASTnode.Assignment.class, statements.get(4).getClass());
    }

    @Test
    void assigmentStatementBis() throws ParserException
    {
        assertEquals(ASTnode.Assignment.class, statements.get(5).getClass());
    }

    @Test
    void assignmentStatementTer() throws ParserException
    {
        assertEquals(ASTnode.Assignment.class, statements.get(6).getClass());
    }

    @Test
    void returnStatement() throws ParserException
    {
        assertEquals(ASTnode.ReturnStatement.class, statements.get(7).getClass());
    }

    @Test
    void functionCallStatementBis() throws ParserException
    {
        assertEquals(ASTnode.FunctionCall.class, statements.get(8).getClass());
    }

    @Test
    void varDeclarationTest() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.Variable)((ASTnode.VarDeclaration)statements.get(0)).getName()).getName().getType());
        assertEquals("x", ((ASTnode.Variable)((ASTnode.VarDeclaration)statements.get(0)).getName()).getName().getValue());
        assertEquals("kg", ((ASTnode.UnitResult)((ASTnode.VarDeclaration)statements.get(0)).getAssignmentValue()).getName().getValue());
        assertEquals(2.0, ((ASTnode.UnitResult)((ASTnode.VarDeclaration)statements.get(0)).getAssignmentValue()).getNumber());
    }

    @Test
    void unitDeclarationTest() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.BaseUnit)statements.get(1)).getName().getType());
        assertEquals("m", ((ASTnode.BaseUnit)statements.get(1)).getName().getValue());
        assertEquals(TokenType.NAME, ((ASTnode.BaseUnit)statements.get(1)).getUnitField().getType());
        assertEquals("LENGTH", ((ASTnode.BaseUnit)statements.get(1)).getUnitField().getValue());
    }

    @Test
    void unitDeclarationTestbis() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.Unit)statements.get(2)).getName().getType());
        assertEquals("kg", ((ASTnode.Unit)statements.get(2)).getName().getValue());
        assertEquals(TokenType.NUMBER, ((ASTnode.Unit)statements.get(2)).getNumber().getType());
        assertEquals(1000, ((ASTnode.Unit)statements.get(2)).getNumber().getIntValue());
        assertEquals(TokenType.NAME, ((ASTnode.Unit)statements.get(2)).getParentName().getType());
        assertEquals("g", ((ASTnode.Unit)statements.get(2)).getParentName().getValue());
    }

    @Test
    void functionCallTest() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.FunctionCall)statements.get(3)).getName().getType());
        assertEquals("fun", ((ASTnode.FunctionCall)statements.get(3)).getName().getValue());
    }

    @Test
    void assigmentTest() throws ParserException
    {
        assertEquals(7.0, ((ASTnode.DoubleNum)((ASTnode.Assignment)statements.get(4)).getAssignmentValue()).getValue());
        assertEquals(TokenType.NAME, ((ASTnode.Variable)((ASTnode.Assignment)statements.get(4)).getVariable()).getName().getType());
        assertEquals("x", ((ASTnode.Variable)((ASTnode.Assignment)statements.get(4)).getVariable()).getName().getValue());
    }

    @Test
    void assignmentTestBis() throws ParserException
    {
        assertEquals("string", ((ASTnode.StringVar)((ASTnode.Assignment)statements.get(5)).getAssignmentValue()).getValue().getValue());
        assertEquals(TokenType.NAME, ((ASTnode.Variable)((ASTnode.Assignment)statements.get(5)).getVariable()).getName().getType());
        assertEquals("y", ((ASTnode.Variable)((ASTnode.Assignment)statements.get(5)).getVariable()).getName().getValue());
    }

    @Test
    void assignmentTestTer() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.Variable)((ASTnode.Assignment)statements.get(6)).getVariable()).getName().getType());
        assertEquals("z", ((ASTnode.Variable)((ASTnode.Assignment)statements.get(6)).getVariable()).getName().getValue());
        assertEquals(1, ((ASTnode.IntNum)((ASTnode.BinOperator)((ASTnode.BinOperator)((ASTnode.Assignment)statements.get(6)).getAssignmentValue()).getRight()).getRight()).getValue());
        assertEquals(7, ((ASTnode.IntNum)((ASTnode.BinOperator)((ASTnode.BinOperator)((ASTnode.Assignment)statements.get(6)).getAssignmentValue()).getRight()).getLeft()).getValue());
        assertEquals(2, ((ASTnode.IntNum)((ASTnode.BinOperator)((ASTnode.BinOperator)((ASTnode.Assignment)statements.get(6)).getAssignmentValue()).getLeft()).getLeft()).getValue());
    }

    @Test
    void returnTest() throws ParserException
    {
        assertEquals(5, ((ASTnode.IntNum)((ASTnode.ReturnStatement)statements.get(7)).getRetValue()).getValue());
    }

    Parser parserBis = new Parser("function foo (param1, param2)\n" +
            "{\n" +
            "    if(x > 10)\n" +
            "    {\n" +
            "        x = x + 1;\n" +
            "    };\n" +
            "}\n");
    AST programBis = parserBis.program();
    AST statementBis = (((ASTnode.FunctionBody)((ASTnode.FunctionDef)(((ASTnode.Program) programBis).getFunctions().get("foo"))).getFunctionBody()).getStatements()).get(0);

    @Test
    void ifConditionTest() throws ParserException
    {
        assertEquals(ASTnode.BinLogicOperator.class, ((ASTnode.BinLogicOperator)((ASTnode.IfStatement) statementBis).getCondition()).getClass());
    }

    @Test
    void ifConditionOperatorTest() throws ParserException
    {
        assertEquals(TokenType.BIGGER, (((ASTnode.BinLogicOperator)((ASTnode.IfStatement) statementBis).getCondition()).getOperation()).getType());
    }

    @Test
    void ifConditionLeftExpTest() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.Variable)(((ASTnode.BinLogicOperator)((ASTnode.IfStatement) statementBis).getCondition()).getLeft())).getName().getType());
    }

    @Test
    void ifConditionRightExpTest() throws ParserException
    {
        assertEquals(10, ((ASTnode.IntNum)(((ASTnode.BinLogicOperator)((ASTnode.IfStatement) statementBis).getCondition()).getRight())).getValue());
    }

    @Test
    void ifBlockTest() throws ParserException
    {
        assertEquals(ASTnode.FunctionBody.class, ((ASTnode.IfStatement) statementBis).getIfBody().getClass());
    }


    @Test
    void elseBlockTest() throws ParserException
    {
        assertNull(((ASTnode.IfStatement) statementBis).getElseBody());
    }

    Parser parserTer = new Parser("function foo (param1, param2)\n" +
            "{\n" +
            "    while(x <= 9.0)\n" +
            "    {\n" +
            "        x = x + 1;\n" +
            "    };\n" +
            "}\n");
    AST programTer = parserTer.program();
    AST statementTer = (((ASTnode.FunctionBody)((ASTnode.FunctionDef)(((ASTnode.Program) programTer).getFunctions().get("foo"))).getFunctionBody()).getStatements()).get(0);

    @Test
    void whileConditionTest() throws ParserException
    {
        assertEquals(ASTnode.BinLogicOperator.class, ((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementTer).getCondition()).getClass());
    }

    @Test
    void whileConditionOperatorTest() throws ParserException
    {
        assertEquals(TokenType.SMALLER_EQUAL, (((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementTer).getCondition()).getOperation()).getType());
    }

    @Test
    void whileConditionLeftExpTest() throws ParserException
    {
        assertEquals(TokenType.NAME, ((ASTnode.Variable)(((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementTer).getCondition()).getLeft())).getName().getType());
    }

    @Test
    void whileConditionRightExpTest() throws ParserException
    {
        assertEquals(9.0, ((ASTnode.DoubleNum)(((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementTer).getCondition()).getRight())).getValue());
    }

    @Test
    void whileBlockTest() throws ParserException
    {
        assertEquals(ASTnode.FunctionBody.class, ((ASTnode.WhileStatement) statementTer).getWhileBody().getClass());
    }

    Parser parserQuater = new Parser("function foo (param1, param2)\n" +
            "{\n" +
            "    while(i <= 100 & ( x == 3))\n" +
            "    {\n" +
            "        x = (x - 10) + 5 * 2;\n" +
            "    };\n" +
            "}\n");
    AST programQuater = parserQuater.program();
    AST statementQuater = (((ASTnode.FunctionBody)((ASTnode.FunctionDef)(((ASTnode.Program) programQuater).getFunctions().get("foo"))).getFunctionBody()).getStatements()).get(0);

    @Test
    void binLogicOperatorTest() throws ParserException
    {
        assertEquals(ASTnode.BinLogicOperator.class, ((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getClass());
    }

    @Test
    void binLogicOperatorTestBis() throws ParserException
    {
        assertEquals(ASTnode.BinLogicOperator.class, ((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getLeft().getClass());
    }

    @Test
    void binLogicOperatorTestTer() throws ParserException
    {
        assertEquals(ASTnode.BinLogicOperator.class, ((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getRight().getClass());
    }

    @Test
    void comparisonTest() throws ParserException
    {
        assertEquals(3, ((ASTnode.IntNum)((ASTnode.BinLogicOperator)(((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getRight())).getRight()).getValue());
    }

    @Test
    void comparisonTestBis() throws ParserException
    {
        assertEquals("x", ((ASTnode.Variable)((ASTnode.BinLogicOperator)(((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getRight())).getLeft()).getName().getValue());
    }

    @Test
    void comparisonTestTer() throws ParserException
    {
        assertEquals("==", ((ASTnode.BinLogicOperator)(((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getRight())).getOperation().getValue());
    }

    @Test
    void binLogicOperatorTestQuater() throws ParserException
    {
        assertEquals("<=", ((ASTnode.BinLogicOperator)(((ASTnode.BinLogicOperator)((ASTnode.WhileStatement) statementQuater).getCondition()).getLeft())).getOperation().getValue());
    }

    @Test
    void negativeTestBis() throws ParserException, FileNotFoundException {
        Parser parser5 = new Parser("function main () \r" +
                "{\r" +
                "var xd=1.5; // test\r" +
                "var str=\"message\"\r" +
                "};");

        String message = " ";
        try {
            parser5.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected SEMICOLON at line: 4.0, position: 1", message);
    }

    @Test
    void negativeTest() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main () \r" +
                "{\r" +
                "var xd=1.a; // test\r" +
                "var str=\"message\";\r" +
                "};");

        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Wrong Token Type at line: 2.0, position: 10", message);
    }

    @Test
    void negativeTest1() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main () \r" +
                "{\r" +
                "var xd=1.5; // test\r" +
                "var str=\"message;\r" +
                "};");

        String message = " ";
        try
        {
            parser.program();
        }
        catch (ParserException e)
        {
            message = e.toString();
        }
        assertEquals("Wrong Token Type at line: 3.0, position: 17", message);
    }

    @Test
    void negativeTest2() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main () \r" +
                "{\r" +
                "var xd=1.5; // test\r" +
                "var str=\"message\";\r" +
                "x = 2 + 2 * 2  (7 + 1);\r" +
                "};");

        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected SEMICOLON at line: 4.0, position: 16", message);
    }

    @Test
    void negativeTest3() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main () \r" +
                "{\r" +
                "    var d=1.5; // test\r" +
                "    var str=\"message\";\r" +
                "    def kg 1000 g;\r" +
                "};");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected COLON at line: 4.0, position: 15", message);
    }

    @Test
    void negativeTest4() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main(){\r" +
                "if(){\r" +
                "x = x + 1;\r" +
                "};\r" +
                "return y;\r" +
                "}");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Statement is missing parameters at line: 1.0, position: 4", message);
    }

    @Test
    void negativeTest5() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main(){\r" +
                "var y = [13 kg;\r" +
                "return y;\r" +
                "}");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected RIGHT_BRACKET at line: 1.0, position: 15", message);
    }

    @Test
    void negativeTest6() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main()\r" +
                "var y = [13 kg];\r" +
                "return y;\r" +
                "}");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected LEFT_BRACE at line: 1.0, position: 3", message);
    }

    @Test
    void negativeTest7() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("funct main(){\r" +
                "var y = [13 kg];\r" +
                "return y;\r" +
                "}");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected function declaration at line: 0.0, position: 5", message);
    }

    @Test
    void negativeTest8() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function; main(){\r" +
                "var y = [13 kg];\r" +
                "return y;\r" +
                "}");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Expected NAME at line: 0.0, position: 9", message);
    }

    @Test
    void negativeTest9() throws ParserException, FileNotFoundException {
        Parser parser = new Parser("function main(){\r" +
                "if( 5 < 6 < 7 < 9 == 10 < ()){\r" +
                "x = x + 1;\r" +
                "};\r" +
                "return y;\r" +
                "}");
        String message = " ";
        try {
            parser.program();
        }
        catch (ParserException e) {
            message = e.toString();
        }
        assertEquals("Statement is missing parameters at line: 1.0, position: 28", message);
    }
}
