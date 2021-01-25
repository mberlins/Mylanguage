package Interpreter;

import parser.AST;
import parser.ASTnode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class Environment {
    ArrayDeque<CallContext> callStack = new ArrayDeque<>();
    HashMap<String, AST> funcDefs;
    Object lastResult;
    Object lastResultVar;
    ArrayList<AST> parameters;      // Variables
    ArrayList<AST> parametersValues;        // wartości poszczególnych parameters

    public void setLastResult(Object lastResult) {
        this.lastResult = lastResult;
    }

    public Object getLastResult() {
        return lastResult;
    }

    public Object getLastResultVar() {
        return lastResultVar;
    }

    public void setLastResultVar(Object lastResultVar) {
        this.lastResultVar = lastResultVar;
    }

    public ArrayList<AST> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<AST> parameters) {
        this.parameters = parameters;
    }

    public ArrayList<AST> getParametersValues() {
        return parametersValues;
    }

    public void setParametersValues(ArrayList<AST> parametersValues) {
        this.parametersValues = parametersValues;
    }

    public HashMap<String, AST> getFuncDefs() {
        return funcDefs;
    }

    public Environment(HashMap<String, AST> funcDefs)
    {
        this.funcDefs = funcDefs;
    }

    public void addBlockContext()
    {
        CallContext funcCallContext = new CallContext();
        callStack.push(funcCallContext);
    }
    public void deleteBlockContext()
    {
        callStack.pop();
    }

    public void addVarContext()
    {
        assert callStack.peek() != null;
        callStack.peek().addVarContext();
    }

    public void deleteVarContext()
    {
        assert callStack.peek() != null;
        callStack.peek().deleteVarContext();
    }

    public void updateVarInCurrentBlockContext(AST name, AST value) throws InterpreterException
    {
        assert callStack.peek() != null;
        callStack.peek().updateVarInBlockContext(name, value);
    }

    public void declareVarInCurrentScope(AST name, AST value) throws InterpreterException
    {
        assert callStack.peek() != null;
        callStack.peek().declareVarInCurrentScope(name, value);
    }

    public AST getVarValue(AST name) throws InterpreterException {
        assert callStack.peek() != null;
        return callStack.peek().getVarValue(name);
    }

}
