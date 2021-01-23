package Interpreter;

import parser.ASTnode;

import java.util.ArrayDeque;
import java.util.HashMap;

public class Enviroment
{
    ArrayDeque<CallContext> callStack = new ArrayDeque<CallContext>();
}

