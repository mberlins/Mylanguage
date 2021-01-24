package Interpreter;

import parser.AST;
import parser.ASTnode;
import standard.Token;
import standard.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class CallContext {

    ArrayList<HashMap<String, ASTnode.Variable>> localVariablesStack = new ArrayList<>();

    public void addVarContext()
    {
        HashMap<String, ASTnode.Variable> newVarContext = new HashMap<String, ASTnode.Variable>();
        localVariablesStack.add(newVarContext);
    }

    public void deleteVarContext()
    {
        localVariablesStack.remove(localVariablesStack.size() - 1);
    }

    public void addVariable(AST var)
    {
        ASTnode.Variable x = (ASTnode.Variable)var;
        localVariablesStack.get(localVariablesStack.size() - 1).put(x.getName().getValue(), (ASTnode.Variable)var);
    }

    public void updateVarInBlockContext(AST name, AST value) throws InterpreterException {
        ASTnode.Variable tmp = null; // wskazanie na variable
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((ASTnode.Variable) (name)).getName().getValue())) != null) {
                break;
            }
        }
        if(tmp == null) throw new InterpreterException("Variable is not declared in this scope");

        tmp.setValue(value);
    }

    public void declareVarInCurrentScope(AST name, AST value) throws InterpreterException {
        localVariablesStack.get(localVariablesStack.size() - 1).put(((ASTnode.Variable)name).getName().getValue(), (ASTnode.Variable)value);
    }

}