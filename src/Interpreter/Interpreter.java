package Interpreter;

import parser.AST;
import parser.ASTnode;
import standard.Token;
import standard.TokenType;

public class Interpreter
{
    /*Environment environment;

    public AST getFunc(String name) throws InterpreterException {
        AST temporaryFunctionDef = new ASTnode().Variable(Token );
        //todo implement searching
        if(not found) throw new InterpreterException("Function not declared!");
        //return temporaryFunctionDef; //todo TUTAJ
    }

    public void visit(AST ast)
    {

    }

    public void visit(ASTnode.Program program)
    {
        environment = new Environment(program.getFunctions());
    }

    public void visit(ASTnode.FunctionDef functionDef) throws InterpreterException {
        functionDef.getFunctionBody().accept(this);
    }

    public void visit(ASTnode.FunctionCall functionCall) throws InterpreterException {
        environment.addBlockContext();
        environment.addVarContext(); // to nie moze byc tu
        for(AST i : ((ASTnode.ParamList)((ASTnode.FunctionDef)(getFunc(functionCall.getName().getValue()))).getParamList()).getNames()){
            // mapa argumentów z parametrami
        }

        getFunc(functionCall.getName().getValue()).accept(this);
        environment.deleteBlockContext();
    }

    public void visit(ASTnode.FunctionBody functionBody) throws InterpreterException {
        environment.addVarContext();

        for(AST i : functionBody.getStatements()){
            if(i instanceof ASTnode.ReturnStatement)
            {
                //cos tu jeszcze
                i.accept(this);
            }
            i.accept(this);
        }

        environment.deleteVarContext();
    }

    public void visit(ASTnode.ReturnStatement returnStatement) throws InterpreterException
    {
        returnStatement.getRetValue().accept(this);
    }

    public void visit(ASTnode.IfStatement ifStatement) throws InterpreterException
    {
        ifStatement.getCondition().accept(this);
        if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("błąd"); //todo zmien

        if((Boolean) (environment.getLastResult()))
            ifStatement.getIfBody().accept(this);
        else
            ifStatement.getElseBody().accept(this);
    }

    public void visit(ASTnode.WhileStatement whileStatement) throws InterpreterException
    {
        whileStatement.getCondition().accept(this);
        if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("błąd"); //todo zmien

        while((Boolean) (environment.getLastResult()))
        {
            whileStatement.getWhileBody().accept(this);

            whileStatement.getCondition().accept(this);
            if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("błąd"); //todo zmien
        }
    }

    public void visit(ASTnode.Assignment assignment) throws InterpreterException
    {
        assignment.getVariable().accept(this);
        if (!(environment.getLastResult() instanceof ASTnode.Variable)) throw new InterpreterException("Not a variable");

        ASTnode.Variable var = (ASTnode.Variable) environment.getLastResult();
        assignment.getAssignmentValue().accept(this);
        if(!((environment.getLastResult() instanceof ASTnode.IntNum) && (environment.getLastResult() instanceof ASTnode.DoubleNum)
                && (environment.getLastResult() instanceof ASTnode.Unit) && (environment.getLastResult() instanceof ASTnode.BaseUnit)
                && (environment.getLastResult() instanceof ASTnode.StringVar))) throw new InterpreterException("Error");

        environment.updateVarInCurrentBlockContext(var, (AST) environment.getLastResult());
    }

    public void visit(ASTnode.VarDeclaration varDeclaration) throws InterpreterException
    {
        varDeclaration.getName().accept(this);
        if (!(environment.getLastResult() instanceof ASTnode.Variable)) throw new InterpreterException("Not a variable");
        ASTnode.Variable var = (ASTnode.Variable) environment.getLastResult();

        varDeclaration.getAssignmentValue().accept(this);
        if(!((environment.getLastResult() instanceof ASTnode.IntNum) && (environment.getLastResult() instanceof ASTnode.DoubleNum)
                && (environment.getLastResult() instanceof ASTnode.Unit) && (environment.getLastResult() instanceof ASTnode.BaseUnit)
                && (environment.getLastResult() instanceof ASTnode.StringVar) && (environment.getLastResult() == null))) throw new InterpreterException("Error");

        environment.declareVarInCurrentScope(var, (AST) environment.getLastResult());
    }

    public void visit(ASTnode.PrintCall printCall) throws InterpreterException
    {
        printCall.getPrintCall().accept(this);
        System.out.println(environment.getLastResult());
    }

    public void visit(ASTnode.BaseUnit ubt) throws InterpreterException
    {
        //todo zrobic
    }

    public void visit(ASTnode.Unit uct) throws InterpreterException
    {
        //todo zrobić
    }

    public void visit(ASTnode.BinOperator binOperator) throws InterpreterException
    {
        binOperator.getLeft().accept(this);
        AST tmpLeft = (AST)environment.getLastResult();
        binOperator.getRight().accept(this);
        AST tmpRight = (AST)environment.getLastResult();
        //todo też sprawdzić
        Token operator = binOperator.getOperation();

        if(tmpLeft instanceof ASTnode.DoubleNum && tmpRight instanceof ASTnode.DoubleNum)
        {
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
            {
                double result = ((ASTnode.DoubleNum)tmpLeft).getValue() * ((ASTnode.DoubleNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
        }
    }

    public void visit(ASTnode.BinLogicOperator binLogicOperator) throws  InterpreterException {
        binLogicOperator.getLeft().accept(this);
        if(!((environment.getLastResult() instanceof ASTnode.IntNum) && (environment.getLastResult() instanceof ASTnode.DoubleNum)
                && (environment.getLastResult() instanceof ASTnode.Unit) && (environment.getLastResult() instanceof ASTnode.BaseUnit)
                && (environment.getLastResult() instanceof ASTnode.StringVar) && (environment.getLastResult() == null))) throw new InterpreterException("Error");

    }
*/
}
