package standard;

    public enum TokenType {
        ADDITIVE_OP, //+
        MINUS_OP,// -
        MULTIPLICATIVE_OP, //*
        DIVIDE_OP,// /
        NEGATION_OP, // !
        ASSIGNMENT_OP, // =
        OR_OP, // |
        AND_OP, // &
        EQUAL_OP, // ==
        SMALLER,// <
        BIGGER,// >
        SMALLER_EQUAL,// <=
        BIGGER_EQUAL,// >=
        //COMMENT, // //
        RIGHT_PARENTHESIS,// )
        LEFT_PARENTHESIS,// (
        LEFT_BRACKET, // [
        RIGHT_BRACKET, // ]
        RIGHT_BRACE, // }
        LEFT_BRACE, // {
        IF,// if
        ELSE,// else
        DEF,// def
        LOOP,// while
        FUNCTION,// function
        VAR, // var
        RETURN,
        NAME, // var names etc
        COMMA, // ,
        COLON, // :
        SEMICOLON,// ;
        //SPACE, //spacja
        NUMBER,//[0..9]*[.]
        STRING,// " message "
        EOF, // end of file
        //EOL, // end of line
        PRINT,      // sprawdzic printowanie
        UNKNOWN
    }

