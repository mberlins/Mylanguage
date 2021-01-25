package Interpreter;

import parser.AST;
import parser.ASTnode;
import standard.Token;
import standard.TokenType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class CallContext {

    ArrayList<HashMap<String, AST>> localVariablesStack = new ArrayList<>();

    public void addVarContext()
    {
        HashMap<String, AST> newVarContext = new HashMap<String, AST>();
        localVariablesStack.add(newVarContext);
    }

    public void deleteVarContext()
    {
        localVariablesStack.remove(localVariablesStack.size() - 1);
    }

    public void updateVarInBlockContext(AST name, AST value) throws InterpreterException
    {
        AST tmp = null; // wskazanie na variable
        for(int i = 1; i <= localVariablesStack.size(); i++) {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((ASTnode.Variable) (name)).getName().getValue())) != null)
            {
                localVariablesStack.get(localVariablesStack.size() - i).replace(((ASTnode.Variable) (name)).getName().getValue(), value);
                break;
            }
        }
        if(tmp == null) throw new InterpreterException("Variable is not declared in this scope");
    }

    public void declareVarInCurrentScope(AST name, AST value) throws InterpreterException
    {
        if (value != null)
            localVariablesStack.get(localVariablesStack.size() - 1).put(((ASTnode.Variable)name).getName().getValue(), value);
        else
            localVariablesStack.get(localVariablesStack.size() - 1).put(((ASTnode.Variable)name).getName().getValue(), new ASTnode.StringVar(null));
    }

    public AST getVarValue(AST name) throws InterpreterException
    {
        AST tmp = null; // wskazanie na variable
        for (int i = 1; i <= localVariablesStack.size(); i++)
        {
            if ((tmp = localVariablesStack.get(localVariablesStack.size() - i).get(((ASTnode.Variable) (name)).getName().getValue())) != null)
            {
                if (tmp instanceof ASTnode.StringVar)
                {
                    if (((ASTnode.StringVar)(localVariablesStack.get(localVariablesStack.size() - i).get(((ASTnode.Variable) (name)).getName().getValue()))).getValue() == null)
                    {
                        throw new InterpreterException("Variable not initialized");
                    }
                }
                return tmp;
            }
        }
        throw new InterpreterException("Variable is not declared in this scope");
    }
}