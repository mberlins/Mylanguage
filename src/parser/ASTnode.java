package parser;

import Interpreter.Interpreter;
import Interpreter.InterpreterException;
import standard.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class ASTnode
{
    public static class Program implements AST
    {
        private HashMap<String, AST> functions = new HashMap<>();

        public HashMap<String, AST> getFunctions() {
            return functions;
        }
        public void addFunction(String name, AST function) {
            functions.put(name, function);
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class FunctionDef implements AST
    {
        Token name;
        AST paramList;
        AST functionBody;

        public FunctionDef(Token name, AST paramList, AST functionBody)
        {
            this.name = name;
            this.paramList = paramList;
            this.functionBody = functionBody;
        }

        public Token getName() {
            return name;
        }

        public AST getParamList() {
            return paramList;
        }

        public AST getFunctionBody() {
            return functionBody;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class ParamList implements AST
    {
        ArrayList<AST> names;

        ParamList(ArrayList<AST> names)
        {
            this.names = names;
        }

        public ArrayList<AST> getNames() {
            return names;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }
    }

    public static class FunctionBody implements AST
    {
        ArrayList<AST> statements;

        FunctionBody(ArrayList<AST> statements)
        {
            this.statements = statements;
        }

        public ArrayList<AST> getStatements() {
            return statements;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class Variable implements AST
    {
        Token name;
        AST value; //todo był token

        Variable(Token name)
        {
            this.name = name;
        }

        public Token getName() {
            return name;
        }

        public AST getValue() {
            return value;
        }

        public void setValue(AST value) {
            this.value = value;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
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

        public Token getName() {
            return name;
        }

        public ArrayList<AST> getArguments() {
            return arguments;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class Assignment implements AST
    {
        AST variable;
        AST assignmentValue;

        Assignment(AST var, AST addExp)
        {
            this.variable = var;
            this.assignmentValue = addExp;
        }

        public AST getVariable() {
            return variable;
        }

        public AST getAssignmentValue() {
            return assignmentValue;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class VarDeclaration implements AST
    {
        AST name;   //todo byl token
        AST assignmentValue;

        VarDeclaration(AST name, AST addExp)
        {
            this.name = name;
            this.assignmentValue = addExp;
        }

        public AST getName() {
            return name;
        }

        public AST getAssignmentValue() {
            return assignmentValue;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
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

        public AST getCondition() {
            return condition;
        }

        public AST getIfBody() {
            return ifBody;
        }

        public AST getElseBody() {
            return elseBody;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
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

        public AST getCondition() {
            return condition;
        }

        public AST getWhileBody() {
            return whileBody;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class ReturnStatement implements AST
    {
        AST retValue;

        ReturnStatement(AST retValue)
        {
            this.retValue = retValue;
        }

        public AST getRetValue() {
            return retValue;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class PrintCall implements AST
    {
        AST printCall;

        PrintCall(AST printCall)
        {
            this.printCall = printCall;
        }

        public AST getPrintCall() {
            return printCall;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
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

        public AST getRight() {
            return right;
        }

        public AST getLeft() {
            return left;
        }

        public Token getOperation() {
            return operation;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class BinLogicOperator implements AST
    {
        public AST right, left;
        public Token operation;

        public BinLogicOperator(AST left, Token operation, AST right)
        {
            this.right = right;
            this.left = left;
            this.operation = operation;
        }

        public AST getRight() {
            return right;
        }

        public AST getLeft() {
            return left;
        }

        public Token getOperation() {
            return operation;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class UnOperator implements AST
    {
        Token token;
        AST expression;
        public UnOperator(Token token, AST expression)
        {
            this.token = token ;
            this.expression = expression;
        }

        public String getType(){
            return token.getValue();
        }
        public AST getExpression(){
            return expression;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }
    }

    public static class IntNum implements AST
    {
        Integer value;
        int line;
        public IntNum(Token token)
        {
            this.value = token.getIntValue();
            this.line = token.getY_coor();
        }

        public Integer getValue() {
            return value;
        }

        public int getLine() {
            return line;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }

        @Override
        public String toString()
        {
            return "" + value;
        }
    }

    public static class DoubleNum implements AST
    {
        Double value;
        int line;
        public DoubleNum(Token token)
        {
            this.value = token.getDoubleValue();
            this.line = token.getY_coor();
        }

        public Double getValue() {
            return value;
        }

        public int getLine() {
            return line;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }

        @Override
        public String toString()
        {
            return "" + value;
        }
    }

    public static class StringVar implements AST
    {
        Token value;
        public StringVar(Token token)
        {
            this.value = token;
        }

        public Token getValue() {
            return value;
        }

        public void accept(Interpreter visitor)
        {
            visitor.visit(this);
        }

        @Override
        public String toString()
        {
            return "" + value.getValue();
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

        public Token getName() {
            return name;
        }

        public Token getNumber() {
            return number;
        }

        public Token getParentName() {
            return parentName;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }

    public static class UnitResult implements AST
    {
        Token name;
        Double number;
        Double multiplicity;
        Token parentName;

        public UnitResult(Token number, Token name)
        {
            this.name = name;
            if (number.getIntValue() == Integer.MIN_VALUE)
                this.number = number.getDoubleValue();
            else
                this.number = (double)number.getIntValue();
        }

        public UnitResult(Token number, Token name, Double multiplicity, Token parentName)
        {
            this.name = name;
            this.multiplicity = multiplicity;
            this.parentName = parentName;
            if (number.getIntValue() == Integer.MIN_VALUE)
                this.number = number.getDoubleValue();
            else
                this.number = (double)number.getIntValue();
        }
        public Token getName() {
            return name;
        }

        public Double getNumber() {
            return number;
        }

        public Double getMultiplicity() {
            return multiplicity;
        }

        public void setMultiplicity(Double multiplicity) {
            this.multiplicity = multiplicity;
        }

        public Token getParentName() {
            return parentName;
        }

        public void setParentName(Token parentName) {
            this.parentName = parentName;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }

        public boolean equals(UnitResult unitResult) throws InterpreterException
        {
            String name = this.name.getValue();
            String nameBis = unitResult.getName().getValue();
            if (!this.parentName.getValue().equals(unitResult.parentName.getValue())) throw new InterpreterException("Impossible to compare units from different dimensions, units: " + name + " and " + nameBis);

            return (this.multiplicity * this.number) == (unitResult.getMultiplicity() * unitResult.getNumber());
        }

        public boolean isBigger(UnitResult unitResult) throws InterpreterException
        {
            String name = this.name.getValue();
            String nameBis = unitResult.getName().getValue();
            if (!this.parentName.getValue().equals(unitResult.parentName.getValue())) throw new InterpreterException("Impossible to compare units from different dimensions, units: " + name + " and " + nameBis);

            return (this.multiplicity * this.number) > (unitResult.getMultiplicity() * unitResult.getNumber());
        }

        public boolean isSmaller(UnitResult unitResult) throws InterpreterException
        {
            String name = this.name.getValue();
            String nameBis = unitResult.getName().getValue();
            if (!this.parentName.getValue().equals(unitResult.parentName.getValue())) throw new InterpreterException("Impossible to compare units from different dimensions, units: " + name + " and " + nameBis);

            return (this.multiplicity * this.number) < (unitResult.getMultiplicity() * unitResult.getNumber());
        }

        public boolean isBiggerEqual(UnitResult unitResult) throws InterpreterException
        {
            String name = this.name.getValue();
            String nameBis = unitResult.getName().getValue();
            if (!this.parentName.getValue().equals(unitResult.parentName.getValue())) throw new InterpreterException("Impossible to compare units from different dimensions, units: " + name + " and " + nameBis);

            return (this.multiplicity * this.number) >= (unitResult.getMultiplicity() * unitResult.getNumber());
        }

        public boolean isSmallerEqual(UnitResult unitResult) throws InterpreterException
        {
            String name = this.name.getValue();
            String nameBis = unitResult.getName().getValue();
            if (!this.parentName.getValue().equals(unitResult.parentName.getValue())) throw new InterpreterException("Impossible to compare units from different dimensions, units: " + name + " and " + nameBis);

            return (this.multiplicity * this.number) <= (unitResult.getMultiplicity() * unitResult.getNumber());
        }

        @Override
        public String toString()
        {
            return "" + number + " " + name.getValue();
        }
    }

    public static class BaseUnit implements AST
    {
        Token name;
        Token unitField;

        public BaseUnit(Token name, Token unitField)
        {
            this.name = name;
            this.unitField = unitField;
        }

        public Token getName() {
            return name;
        }

        public Token getUnitField() {
            return unitField;
        }

        public void accept(Interpreter visitor) throws InterpreterException {
            visitor.visit(this);
        }
    }
}

