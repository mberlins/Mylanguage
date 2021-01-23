package Interpreter;

import parser.ASTnode;
import standard.Token;

import java.util.HashMap;

public class CallContext {
    Token name;
    ActivationType type;
    int nestingLevel;
    HashMap<Token, ASTnode> variables = new HashMap<>();

    public CallContext(Token name, ActivationType type, int nestingLevel) {
        this.name = name;
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    public void setVariables(HashMap<Token, ASTnode> variables) {
        this.variables = variables;
    }

    public ASTnode getItem(Token key) {
        ASTnode ret = variables.get(key);
        return ret;
    }

    public void pushItem(Token key, ASTnode value) {
        variables.put(key, value);
    }

    public Token getName() {
        return name;
    }

    public ActivationType getType() {
        return type;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public HashMap<Token, ASTnode> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("name: " + name + " type: " + type + " nestingLevel: " + nestingLevel + "\n");
        variables.entrySet().forEach(entry -> {
            s.append(entry.getKey() + " " + entry.getValue());
        });
        return s.toString();
    }
}