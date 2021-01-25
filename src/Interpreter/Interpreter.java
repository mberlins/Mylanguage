package Interpreter;

import parser.AST;
import parser.ASTnode;
import standard.Token;
import standard.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter
{
    Environment environment;
    AST program;

    public Interpreter(AST program)
    {
        this.program = program;
    }

    public Object run() throws InterpreterException {
        program.accept(this);
        return environment.getLastResult();
    }

    public AST getFunc(String name) throws InterpreterException
    {
        if(environment.getFuncDefs().get(name) == null)
            throw new InterpreterException("Function not declared!");
        else
            return environment.getFuncDefs().get(name);
    }

    public void visit(ASTnode.Program program) throws InterpreterException
    {
        environment = new Environment(program.getFunctions());
        AST main = getFunc("main");         // todo blad no main
        main.accept(this);
    }

    public void visit(ASTnode.FunctionDef functionDef) throws InterpreterException
    {
        environment.addBlockContext();

        if(((ASTnode.FunctionDef)(getFunc(functionDef.getName().getValue()))).getParamList() != null) {
            environment.setParameters(((ASTnode.ParamList) ((ASTnode.FunctionDef) (getFunc(functionDef.getName().getValue()))).getParamList()).getNames());

            if (environment.getParameters().size() != environment.getParametersValues().size())
                throw new InterpreterException("Expected: " + environment.getParameters().size() + "arguments but got: " + environment.getParametersValues().size());
            int counter = 0;
            for (AST i : environment.getParameters()) {
                ((ASTnode.Variable) i).setValue((environment.getParametersValues().get(counter))); //todo to jedyne dozwolone użycie value w Variable
                counter++;
            }
        }
        functionDef.getFunctionBody().accept(this);
        environment.deleteBlockContext();
    }

    public void visit(ASTnode.FunctionCall functionCall) throws InterpreterException
    {
        //tutaj zapisanie wartości tych parametrów po kolei do Array parameters
        ArrayList<AST> tmp = new ArrayList<>();
        for(AST i : functionCall.getArguments())
        {
            if(i instanceof ASTnode.Variable || i instanceof ASTnode.FunctionCall)
            {
                i.accept(this); // po wyjściu stąd w lastResult będzie wartość vara albo functioncalla
                tmp.add((AST) environment.getLastResult());
            }
            else // będzie num, unit, albo string bez ifa
                tmp.add(i);
        }
        environment.setParametersValues(tmp);
        getFunc(functionCall.getName().getValue()).accept(this); // wyszukuje funkcję w arrayu funkcji
    }

    public void visit(ASTnode.FunctionBody functionBody) throws InterpreterException
    {
        environment.addVarContext();
        if(environment.getParameters() != null)
            for(AST i : environment.getParameters())
            {
                environment.declareVarInCurrentScope(i, ((ASTnode.Variable) i).getValue()); //todo DOZWOLONE TYLKO W TYM PRZYPADKU, W INNYCH GETVALUE NIE REPREZENTUJE WARTOŚCI ZMIENNEJ
            }

        for(AST i : functionBody.getStatements()){
            if(i instanceof ASTnode.ReturnStatement)
            {
                i.accept(this);
                return;
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
        if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("106"); //todo zmien

        if((Boolean) (environment.getLastResult()))
            ifStatement.getIfBody().accept(this);
        else
            ifStatement.getElseBody().accept(this);
    }

    public void visit(ASTnode.WhileStatement whileStatement) throws InterpreterException
    {
        whileStatement.getCondition().accept(this);
        if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("117"); //todo zmien

        while((Boolean) (environment.getLastResult()))
        {
            whileStatement.getWhileBody().accept(this);
            whileStatement.getCondition().accept(this);
            if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("123"); //todo zmien
        }
    }

    public void visit(ASTnode.Assignment assignment) throws InterpreterException
    {
        AST var = assignment.getVariable();
        assignment.getAssignmentValue().accept(this);

        if(!((environment.getLastResult() instanceof ASTnode.IntNum) || (environment.getLastResult() instanceof ASTnode.DoubleNum)
                || (environment.getLastResult() instanceof ASTnode.Unit) || (environment.getLastResult() instanceof ASTnode.BaseUnit)
                || (environment.getLastResult() instanceof ASTnode.StringVar))) throw new InterpreterException("134");

        environment.updateVarInCurrentBlockContext(var, (AST) environment.getLastResult());
    }

    public void visit(ASTnode.VarDeclaration varDeclaration) throws InterpreterException
    {
        AST var = varDeclaration.getName();
        if (varDeclaration.getAssignmentValue() != null)
            varDeclaration.getAssignmentValue().accept(this);
        if(!((environment.getLastResult() instanceof ASTnode.IntNum) || (environment.getLastResult() instanceof ASTnode.DoubleNum)
                || (environment.getLastResult() instanceof ASTnode.Unit) || (environment.getLastResult() instanceof ASTnode.BaseUnit)
                || (environment.getLastResult() instanceof ASTnode.StringVar) || (environment.getLastResult() == null))) throw new InterpreterException("146");

        if (varDeclaration.getAssignmentValue() != null)
            environment.declareVarInCurrentScope(var, (AST) environment.getLastResult());
        else
            environment.declareVarInCurrentScope(var, null);
    }

    public void visit(ASTnode.PrintCall printCall) throws InterpreterException
    {
        printCall.getPrintCall().accept(this);
        System.out.println(environment.getLastResult());
    }

    public void visit(ASTnode.BaseUnit baseUnit) throws InterpreterException
    {
        //todo zrobic
    }

    public void visit(ASTnode.Unit unit) throws InterpreterException
    {
        //todo zrobić
    }

    public void visit(ASTnode.BinOperator binOperator) throws InterpreterException
    {
        binOperator.getLeft().accept(this);
        AST tmpLeft = (AST) environment.getLastResult();
        //todo spradzić czy tmpLeft jest Num coś tam String -- tu można sprawdzić mnożenie i dodawanie unitów
        binOperator.getRight().accept(this);
        AST tmpRight = (AST)environment.getLastResult();
        //todo też sprawdzić
        Token operator = binOperator.getOperation();

        if((tmpLeft instanceof ASTnode.DoubleNum) && (tmpRight instanceof ASTnode.DoubleNum || tmpRight instanceof ASTnode.IntNum))
        {
            double result;
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() * ((ASTnode.DoubleNum)tmpRight).getValue();
                else
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() * ((ASTnode.IntNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
            else  if(operator.getType() == TokenType.DIVIDE_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                {
                    if (((ASTnode.DoubleNum)tmpRight).getValue() == 0.0) throw new InterpreterException("Dividing by 0 forbidden");
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() / ((ASTnode.DoubleNum)tmpRight).getValue();
                }
                else
                {
                    if (((ASTnode.IntNum)tmpRight).getValue() == 0) throw new InterpreterException("Dividing by 0 forbidden");
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() / ((ASTnode.IntNum)tmpRight).getValue();
                }
                environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
            else  if(operator.getType() == TokenType.ADDITIVE_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() + ((ASTnode.DoubleNum)tmpRight).getValue();
                else
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() + ((ASTnode.IntNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
            else  if(operator.getType() == TokenType.MINUS_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() - ((ASTnode.DoubleNum)tmpRight).getValue();
                else
                    result = ((ASTnode.DoubleNum)tmpLeft).getValue() - ((ASTnode.IntNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
        }
        else if((tmpLeft instanceof ASTnode.IntNum) && (tmpRight instanceof ASTnode.DoubleNum || tmpRight instanceof ASTnode.IntNum))
        {
            int result;
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                    result = (int) (((ASTnode.IntNum)tmpLeft).getValue() * ((ASTnode.DoubleNum)tmpRight).getValue());
                else
                    result = ((ASTnode.IntNum)tmpLeft).getValue() * ((ASTnode.IntNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.IntNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
            else  if(operator.getType() == TokenType.DIVIDE_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                {
                    if (((ASTnode.DoubleNum)tmpRight).getValue() == 0.0) throw new InterpreterException("Dividing by 0 forbidden");
                    result = (int) (((ASTnode.IntNum)tmpLeft).getValue() / ((ASTnode.DoubleNum)tmpRight).getValue());
                }
                else
                {
                    if (((ASTnode.IntNum)tmpRight).getValue() == 0) throw new InterpreterException("Dividing by 0 forbidden");
                    result = ((ASTnode.IntNum)tmpLeft).getValue() / ((ASTnode.IntNum)tmpRight).getValue();
                }
                environment.setLastResult(new ASTnode.IntNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
            else  if(operator.getType() == TokenType.ADDITIVE_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                    result = (int) (((ASTnode.IntNum)tmpLeft).getValue() + ((ASTnode.DoubleNum)tmpRight).getValue());
                else
                    result = ((ASTnode.IntNum)tmpLeft).getValue() + ((ASTnode.IntNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.IntNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
            else  if(operator.getType() == TokenType.MINUS_OP)
            {
                if (tmpRight instanceof ASTnode.DoubleNum)
                    result = (int) (((ASTnode.IntNum)tmpLeft).getValue() - ((ASTnode.DoubleNum)tmpRight).getValue());
                else
                    result = ((ASTnode.IntNum)tmpLeft).getValue() - ((ASTnode.IntNum)tmpRight).getValue();
                environment.setLastResult(new ASTnode.IntNum(new Token(0, 0, result, TokenType.NUMBER)));
            }
        }
        else if(tmpLeft instanceof ASTnode.StringVar && tmpRight instanceof ASTnode.StringVar)
        {
            if(operator.getType() == TokenType.ADDITIVE_OP)
            {
                String result = ((ASTnode.StringVar)tmpLeft).getValue().getValue() + ((ASTnode.StringVar)tmpRight).getValue().getValue();
                environment.setLastResult(new ASTnode.StringVar(new Token(0, 0, result, TokenType.STRING)));
            }
        }
        else throw new InterpreterException("Forbidden operation!");
    }

    public void visit(ASTnode.BinLogicOperator binLogicOperator) throws  InterpreterException
    {
        binLogicOperator.getLeft().accept(this);
        if(!((environment.getLastResult() instanceof ASTnode.IntNum) || (environment.getLastResult() instanceof ASTnode.DoubleNum)
                || (environment.getLastResult() instanceof ASTnode.Unit) || (environment.getLastResult() instanceof ASTnode.BaseUnit)
                || (environment.getLastResult() instanceof ASTnode.StringVar) || (environment.getLastResult() == null))) throw new InterpreterException("280");

    }

    //odwiedzona variable będzie ustawiała dwa pola env
    public void visit(ASTnode.Variable v) throws  InterpreterException
    {
        environment.setLastResultVar(v);
        environment.setLastResult(environment.getVarValue(v));
    }

    public void visit(ASTnode.IntNum num)
    {
        environment.setLastResult(num);
    }

    public void visit(ASTnode.DoubleNum num)
    {
        environment.setLastResult(num);
    }

    public void visit(ASTnode.ParamList paramList)
    {
        //niepotrzebne chyba, rzutuję już wcześniej
    }

    public void visit(ASTnode.StringVar stringVar)
    {
        environment.setLastResult(stringVar);
    }

    public void visit(ASTnode.UnOperator unOperator)
    {

    }
}
