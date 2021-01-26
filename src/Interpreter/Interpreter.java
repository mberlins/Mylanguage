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
            throw new InterpreterException("Function " + name + " not declared.");
        else
            return environment.getFuncDefs().get(name);
    }

    public void visit(ASTnode.Program program) throws InterpreterException
    {
        environment = new Environment(program.getFunctions());
        AST main = getFunc("main");
        main.accept(this);
    }

    public void visit(ASTnode.FunctionDef functionDef) throws InterpreterException
    {
        environment.addBlockContext();

        if(((ASTnode.FunctionDef)(getFunc(functionDef.getName().getValue()))).getParamList() != null)
        {
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
        if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("Wrong if statement parameter"); //todo zmien

        if((Boolean) (environment.getLastResult()))
            ifStatement.getIfBody().accept(this);
        else
        {
            if (ifStatement.getElseBody() != null)
                ifStatement.getElseBody().accept(this);
        }
    }

    public void visit(ASTnode.WhileStatement whileStatement) throws InterpreterException
    {
        whileStatement.getCondition().accept(this);
        if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("Wrong while statement parameter"); //todo zmien

        while((Boolean) (environment.getLastResult()))
        {
            whileStatement.getWhileBody().accept(this);
            whileStatement.getCondition().accept(this);
            if(!(environment.getLastResult() instanceof Boolean)) throw new InterpreterException("Wrong while statement parameter"); //todo zmien
        }
    }

    public void visit(ASTnode.Assignment assignment) throws InterpreterException
    {
        AST var = assignment.getVariable();
        assignment.getAssignmentValue().accept(this);

        if(!((environment.getLastResult() instanceof ASTnode.IntNum) || (environment.getLastResult() instanceof ASTnode.DoubleNum)
                || (environment.getLastResult() instanceof ASTnode.Unit) || (environment.getLastResult() instanceof ASTnode.BaseUnit)
                || (environment.getLastResult() instanceof ASTnode.StringVar) || (environment.getLastResult() instanceof ASTnode.UnitResult))) throw new InterpreterException("134");

        environment.updateVarInCurrentBlockContext(var, (AST) environment.getLastResult());
    }

    public void visit(ASTnode.VarDeclaration varDeclaration) throws InterpreterException
    {
        AST var = varDeclaration.getName();
        if (varDeclaration.getAssignmentValue() != null)
            varDeclaration.getAssignmentValue().accept(this);
        if(!((environment.getLastResult() instanceof ASTnode.IntNum) || (environment.getLastResult() instanceof ASTnode.DoubleNum)
                || (environment.getLastResult() instanceof ASTnode.Unit) || (environment.getLastResult() instanceof ASTnode.BaseUnit)
                || (environment.getLastResult() instanceof ASTnode.StringVar) || (environment.getLastResult() == null
                || (environment.getLastResult() instanceof ASTnode.UnitResult)))) throw new InterpreterException("146");

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
        environment.declareBasicUnit(baseUnit);
    }

    public void visit(ASTnode.Unit unit) throws InterpreterException
    {
        if ((environment.getUnits()).get(unit.getName().getValue()) != null)
            return;;
        if((environment.getBasicUnits()).get(unit.getParentName().getValue()) == null)
            throw new InterpreterException("Parent unit of unit is not declared at line:" + unit.getName().getY_coor());

        environment.declareUnit(unit);
    }

    public void visit(ASTnode.UnitResult unitResult) throws InterpreterException
    {
        if ((environment.getUnits()).get(unitResult.getName().getValue()) == null && (environment.getBasicUnits()).get(unitResult.getName().getValue()) == null)
            throw new InterpreterException("Unit not declared at line "+ unitResult.getName().getY_coor());

        AST unit = (environment.getUnits()).get(((ASTnode.UnitResult) unitResult).getName().getValue());
        Token parent = null;
        double multiplicity = 1;

        if (unit != null)
        {
            parent = ((ASTnode.Unit)unit).getParentName();
            multiplicity = Math.max(((ASTnode.Unit)unit).getNumber().getDoubleValue(), ((ASTnode.Unit)unit).getNumber().getIntValue());
        }
        if (unit == null)
        {
            unit = (environment.getBasicUnits()).get(((ASTnode.UnitResult) unitResult).getName().getValue());
            parent = ((ASTnode.BaseUnit)unit).getName();
        }

        unitResult.setMultiplicity(multiplicity);
        unitResult.setParentName(parent);
        environment.setLastResult(unitResult);
    }

    public void visit(ASTnode.BinOperator binOperator) throws InterpreterException
    {
        binOperator.getLeft().accept(this);
        AST tmpLeft = (AST) environment.getLastResult();
        binOperator.getRight().accept(this);
        AST tmpRight = (AST)environment.getLastResult();
        Token operator = binOperator.getOperation();

        if((tmpLeft instanceof ASTnode.DoubleNum) && (tmpRight instanceof ASTnode.DoubleNum))
        {
            double result = 0;
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() * ((ASTnode.DoubleNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.DIVIDE_OP)
            {
                if (((ASTnode.DoubleNum) tmpRight).getValue() == 0.0) throw new InterpreterException("Dividing by 0 forbidden at line "+ ((ASTnode.DoubleNum) tmpRight).getLine());
                result = ((ASTnode.DoubleNum) tmpLeft).getValue() / ((ASTnode.DoubleNum) tmpRight).getValue();
            }
            else  if(operator.getType() == TokenType.ADDITIVE_OP)
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() + ((ASTnode.DoubleNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.MINUS_OP)
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() - ((ASTnode.DoubleNum)tmpRight).getValue();

            environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
        }
        else if((tmpLeft instanceof ASTnode.IntNum) && (tmpRight instanceof ASTnode.IntNum))
        {
            int result = 0;
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
                result = ((ASTnode.IntNum)tmpLeft).getValue() * ((ASTnode.IntNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.DIVIDE_OP)
            {
                if (((ASTnode.IntNum)tmpRight).getValue() == 0) throw new InterpreterException("Dividing by 0 forbidden at line "+ ((ASTnode.IntNum) tmpRight).getLine());
                result = ((ASTnode.IntNum)tmpLeft).getValue() / ((ASTnode.IntNum)tmpRight).getValue();
            }
            else  if(operator.getType() == TokenType.ADDITIVE_OP)
                result = ((ASTnode.IntNum)tmpLeft).getValue() + ((ASTnode.IntNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.MINUS_OP)
                result = ((ASTnode.IntNum)tmpLeft).getValue() - ((ASTnode.IntNum)tmpRight).getValue();

            environment.setLastResult(new ASTnode.IntNum(new Token(0, 0, result, TokenType.NUMBER)));
        }
        else if((tmpLeft instanceof ASTnode.IntNum) && (tmpRight instanceof ASTnode.DoubleNum))
        {
            double result = 0;
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
                result = ((ASTnode.IntNum)tmpLeft).getValue() * ((ASTnode.DoubleNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.DIVIDE_OP)
            {
                if (((ASTnode.DoubleNum)tmpRight).getValue() == 0.0) throw new InterpreterException("Dividing by 0 forbidden at line "+ ((ASTnode.DoubleNum) tmpRight).getLine());
                result = ((ASTnode.IntNum)tmpLeft).getValue() / ((ASTnode.DoubleNum)tmpRight).getValue();
            }
            else  if(operator.getType() == TokenType.ADDITIVE_OP)
                result = ((ASTnode.IntNum)tmpLeft).getValue() + ((ASTnode.DoubleNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.MINUS_OP)
                result = ((ASTnode.IntNum)tmpLeft).getValue() - ((ASTnode.DoubleNum)tmpRight).getValue();

            environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
        }
        else if((tmpLeft instanceof ASTnode.DoubleNum) && (tmpRight instanceof ASTnode.IntNum))
        {
            double result = 0;
            if(operator.getType() == TokenType.MULTIPLICATIVE_OP)
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() * ((ASTnode.IntNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.DIVIDE_OP)
            {
                if (((ASTnode.IntNum)tmpRight).getValue() == 0) throw new InterpreterException("Dividing by 0 forbidden at line "+ ((ASTnode.IntNum) tmpRight).getLine());
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() / ((ASTnode.IntNum)tmpRight).getValue();
            }
            else  if(operator.getType() == TokenType.ADDITIVE_OP)
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() + ((ASTnode.IntNum)tmpRight).getValue();
            else  if(operator.getType() == TokenType.MINUS_OP)
                result = ((ASTnode.DoubleNum)tmpLeft).getValue() - ((ASTnode.IntNum)tmpRight).getValue();

            environment.setLastResult(new ASTnode.DoubleNum(new Token(0, 0, result, TokenType.NUMBER)));
        }
        else if(tmpLeft instanceof ASTnode.StringVar && tmpRight instanceof ASTnode.StringVar)
        {
            if(operator.getType() == TokenType.ADDITIVE_OP)
            {
                String result = ((ASTnode.StringVar)tmpLeft).getValue().getValue() + ((ASTnode.StringVar)tmpRight).getValue().getValue();
                environment.setLastResult(new ASTnode.StringVar(new Token(0, 0, result, TokenType.STRING)));
            }
        }
        else if (tmpLeft instanceof ASTnode.UnitResult && tmpRight instanceof ASTnode.UnitResult)
        {
            double tmp = 0;

            if (!((ASTnode.UnitResult) tmpLeft).getParentName().getValue().equals(((ASTnode.UnitResult) tmpRight).getParentName().getValue()))
                throw new InterpreterException("Impossible to add units from different dimensions at line "+ ((ASTnode.UnitResult) tmpLeft).getName().getY_coor());

            if(operator.getType() == TokenType.ADDITIVE_OP)
            {
                if (((ASTnode.UnitResult) (tmpLeft)).getName().getValue().equals(((ASTnode.UnitResult) (tmpRight)).getName().getValue()))
                {
                    tmp = ((ASTnode.UnitResult) tmpLeft).getNumber() + ((ASTnode.UnitResult) tmpRight).getNumber();
                    environment.setLastResult(new ASTnode.UnitResult((new Token(0,0, tmp, TokenType.NUMBER)), ((ASTnode.UnitResult)(tmpLeft)).getName(), ((ASTnode.UnitResult) tmpLeft).getMultiplicity(), ((ASTnode.UnitResult) tmpLeft).getParentName()));
                }
                else {
                    tmp = (((ASTnode.UnitResult) tmpLeft).getMultiplicity() * ((ASTnode.UnitResult) tmpLeft).getNumber()) + ((ASTnode.UnitResult) tmpRight).getMultiplicity() * (((ASTnode.UnitResult) tmpRight).getNumber());
                    tmp = tmp/((ASTnode.UnitResult) tmpLeft).getMultiplicity();
                    environment.setLastResult(new ASTnode.UnitResult((new Token(0, 0, tmp, TokenType.NUMBER)), ((ASTnode.UnitResult)(tmpLeft)).getName(),((ASTnode.UnitResult) tmpLeft).getMultiplicity(), ((ASTnode.UnitResult) tmpLeft).getParentName()));
                }
            }
            else if (operator.getType() == TokenType.MINUS_OP)
            {
                if (((ASTnode.UnitResult) (tmpLeft)).getName().getValue().equals(((ASTnode.UnitResult) (tmpRight)).getName().getValue()))
                {
                    tmp = ((ASTnode.UnitResult) tmpLeft).getNumber() - ((ASTnode.UnitResult) tmpRight).getNumber();
                    environment.setLastResult(new ASTnode.UnitResult((new Token(0,0, tmp, TokenType.NUMBER)), ((ASTnode.UnitResult)(tmpLeft)).getName(), ((ASTnode.UnitResult) tmpLeft).getMultiplicity(), ((ASTnode.UnitResult) tmpLeft).getParentName()));
                }
                else {
                    tmp = (((ASTnode.UnitResult) tmpLeft).getMultiplicity() * ((ASTnode.UnitResult) tmpLeft).getNumber()) - ((ASTnode.UnitResult) tmpRight).getMultiplicity() * (((ASTnode.UnitResult) tmpRight).getNumber());
                    tmp = tmp/((ASTnode.UnitResult) tmpLeft).getMultiplicity();
                    environment.setLastResult(new ASTnode.UnitResult((new Token(0, 0, tmp, TokenType.NUMBER)), ((ASTnode.UnitResult)(tmpLeft)).getName(), ((ASTnode.UnitResult) tmpLeft).getMultiplicity(), ((ASTnode.UnitResult) tmpLeft).getParentName()));
                }
            }
        }
        else throw new InterpreterException("Forbidden operation at line " + binOperator.getOperation().getY_coor());
    }

    public void visit(ASTnode.BinLogicOperator binLogicOperator) throws  InterpreterException
    {
        binLogicOperator.getLeft().accept(this);
        Object leftExp = environment.getLastResult();
        binLogicOperator.getRight().accept(this);
        Object rightExp = environment.getLastResult();

        if (leftExp instanceof Boolean && rightExp instanceof Boolean)
        {
            if (binLogicOperator.getOperation().getType() == TokenType.AND_OP)
                environment.setLastResult((Boolean)leftExp && (Boolean)rightExp);
            else if (binLogicOperator.getOperation().getType() == TokenType.OR_OP)
                environment.setLastResult((Boolean)leftExp || (Boolean)rightExp);
            else if (binLogicOperator.getOperation().getType() == TokenType.EQUAL_OP)
                environment.setLastResult((Boolean)leftExp == (Boolean)rightExp);
            else if (binLogicOperator.getOperation().getType() == TokenType.UNEQUAL_OP)
                environment.setLastResult((Boolean)leftExp != (Boolean)rightExp);
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else if(leftExp instanceof ASTnode.IntNum && rightExp instanceof ASTnode.IntNum)
        {
            if(binLogicOperator.getOperation().getType() == TokenType.EQUAL_OP)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue().equals(((ASTnode.IntNum) rightExp).getValue()));
            else if(binLogicOperator.getOperation().getType() == TokenType.UNEQUAL_OP)
                environment.setLastResult(!((ASTnode.IntNum) leftExp).getValue().equals(((ASTnode.IntNum) rightExp).getValue()));
            else if(binLogicOperator.getOperation().getType() == TokenType.BIGGER)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() > ((ASTnode.IntNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.BIGGER_EQUAL)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() >= ((ASTnode.IntNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() < ((ASTnode.IntNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER_EQUAL)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() <= ((ASTnode.IntNum) rightExp).getValue());
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else if(leftExp instanceof ASTnode.DoubleNum && rightExp instanceof ASTnode.DoubleNum)
        {
            if(binLogicOperator.getOperation().getType() == TokenType.EQUAL_OP)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue().equals(((ASTnode.DoubleNum) rightExp).getValue()));
            else if(binLogicOperator.getOperation().getType() == TokenType.UNEQUAL_OP)
                environment.setLastResult(!((ASTnode.DoubleNum) leftExp).getValue().equals(((ASTnode.DoubleNum) rightExp).getValue()));
            else if(binLogicOperator.getOperation().getType() == TokenType.BIGGER)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() > ((ASTnode.DoubleNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.BIGGER_EQUAL)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() >= ((ASTnode.DoubleNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() < ((ASTnode.DoubleNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER_EQUAL)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() <= ((ASTnode.DoubleNum) rightExp).getValue());
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else if(leftExp instanceof ASTnode.IntNum && rightExp instanceof ASTnode.DoubleNum)
        {
            if(binLogicOperator.getOperation().getType() == TokenType.BIGGER)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() > ((ASTnode.DoubleNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.BIGGER_EQUAL)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() >= ((ASTnode.DoubleNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() < ((ASTnode.DoubleNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER_EQUAL)
                environment.setLastResult(((ASTnode.IntNum) leftExp).getValue() <= ((ASTnode.DoubleNum) rightExp).getValue());
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else if(leftExp instanceof ASTnode.DoubleNum && rightExp instanceof ASTnode.IntNum)
        {
            if(binLogicOperator.getOperation().getType() == TokenType.BIGGER)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() > ((ASTnode.IntNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.BIGGER_EQUAL)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() >= ((ASTnode.IntNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() < ((ASTnode.IntNum) rightExp).getValue());
            else if(binLogicOperator.getOperation().getType() == TokenType.SMALLER_EQUAL)
                environment.setLastResult(((ASTnode.DoubleNum) leftExp).getValue() <= ((ASTnode.IntNum) rightExp).getValue());
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else if(leftExp instanceof ASTnode.StringVar && rightExp instanceof ASTnode.StringVar)
        {
            if(binLogicOperator.getOperation().getType() == TokenType.EQUAL_OP)
                environment.setLastResult(((ASTnode.StringVar) leftExp).getValue().getValue().equals(((ASTnode.StringVar) rightExp).getValue().getValue()));
            else if(binLogicOperator.getOperation().getType() == TokenType.UNEQUAL_OP)
                environment.setLastResult(!((ASTnode.StringVar) leftExp).getValue().getValue().equals(((ASTnode.StringVar) rightExp).getValue().getValue()));
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else if(leftExp instanceof  ASTnode.UnitResult && rightExp instanceof ASTnode.UnitResult)
        {
            if(binLogicOperator.getOperation().getType() == TokenType.EQUAL_OP)
                environment.setLastResult(((ASTnode.UnitResult) leftExp).equals((ASTnode.UnitResult)rightExp));
            else if (binLogicOperator.getOperation().getType() == TokenType.UNEQUAL_OP)
                environment.setLastResult(!(((ASTnode.UnitResult) leftExp).equals((ASTnode.UnitResult)rightExp)));
            else if (binLogicOperator.getOperation().getType() == TokenType.BIGGER)
                environment.setLastResult(((ASTnode.UnitResult) leftExp).isBigger((ASTnode.UnitResult)rightExp));
            else if (binLogicOperator.getOperation().getType() == TokenType.SMALLER)
                environment.setLastResult(!(((ASTnode.UnitResult) leftExp).isBigger((ASTnode.UnitResult)rightExp)));
            else if (binLogicOperator.getOperation().getType() == TokenType.BIGGER_EQUAL)
                environment.setLastResult(((ASTnode.UnitResult) leftExp).isBiggerEqual((ASTnode.UnitResult)rightExp));
            else if (binLogicOperator.getOperation().getType() == TokenType.SMALLER_EQUAL)
                environment.setLastResult(((ASTnode.UnitResult) leftExp).isSmallerEqual((ASTnode.UnitResult)rightExp));
            else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
        }
        else throw new InterpreterException("Forbidden logical Operation at line "+ binLogicOperator.getOperation().getY_coor());
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

    }

    public void visit(ASTnode.StringVar stringVar)
    {
        environment.setLastResult(stringVar);
    }

    public void visit(ASTnode.UnOperator unOperator)
    {

    }

}
