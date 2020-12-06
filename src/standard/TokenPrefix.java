package standard;

public class TokenPrefix
{
    public static boolean isLetter(char ch)
    {
        if (Character.isLetter(ch) == true || ch =='_')
            return true;
        else
            return false;
    }

    public boolean isBigLetter(char ch)
    {
        if (Character.isUpperCase(ch) == true)
            return true;
        else
            return false;
    }

    public static boolean isDigit(char ch)
    {
        if (Character.isDigit(ch) == true)
            return true;
        else
            return false;
    }

    public static boolean isEOF(char ch)
    {
        if (ch == '\u001a')
            return true;
        else
            return false;
    }

    public static boolean isSpace(char ch)
    {
        if (ch == ' ')
            return true;
        else
            return false;
    }

    public static boolean isEOL(char ch)
    {
        if(ch == '\n' || ch == '\r' /*|| ch == '\r\n'*/)
            return true;
        else
            return false;
    }

    public static boolean isSign(char ch)
    {
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}'
            || ch == ',' || ch == '.' || ch == ';' || ch == ':' || ch == '"' || ch == '!' || ch == '=' || ch == '&' || ch == '>' || ch == '<')
            return true;
        else
            return false;
    }

}
