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

    public void setLastResult(AST lastResult) {
        this.lastResult = lastResult;
    }

    public Object getLastResult() {
        return lastResult;
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

    public void updateVarInCurrentBlockContext(AST name, AST value) throws InterpreterException {
        assert callStack.peek() != null;
        callStack.peek().updateVarInBlockContext(name, value);
    }

    public void declareVarInCurrentScope(AST name, AST value) throws InterpreterException {
        assert callStack.peek() != null;
        callStack.peek().declareVarInCurrentScope(name, value);
    }

}
