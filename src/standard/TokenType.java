package standard;

public class TokenType
{

    public enum TokenTypes {
        ADDITIVE_OP, //+ -
        MULTIPLICATIVE_OP, //* /
        UNARY_OP, // !
        ASSIGNMENT_OP, // =
        OR_OP, // ||
        AND_OP, // &&
        EQUAL_OP, // ==
        COMPARE_OP, // < > <= >=
        COMMENT, // //
        RIGHT_PARENTESIS,// )
        LEFT_PARENTESIS,// (
        LEFT_BRACKET, // [
        RIGHT_BRACKET, // ]
        RIGHT_BRACE, // }
        LEFT_BRACE, // {
        IF,// if
        ELSE,// else
        ID,// id
        COMMA,// ,
        SEMICOLLON,// ;
        LOOP,// while
        FUNCTION,// function
        VAR,// var
        NUMBER,//[0..9]*[.]
        QUOTE,// " "
        RETURN,
        EOF,
        UNKNOWN

    }
}
