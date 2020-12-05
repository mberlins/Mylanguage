package standard;

public class TokenPrefix
{
    public boolean isSmallLetter(char ch)
    {
        if (Character.isLowerCase(ch) == true)
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

    public boolean isDigit(char ch)
    {
        if (Character.isDigit(ch) == true)
            return true;
        else
            return false;
    }

    public boolean isEOF(char ch)
    {
        if (ch == '\u001a')
            return true;
        else
            return false;
    }

    public boolean isSpace(char ch)
    {
        if (ch == ' ')
            return true;
        else
            return false;
    }

    public boolean isEOL(char ch)
    {
        if(ch == '\n' || ch == '\r' /*|| ch == '\r\n'*/)
            return true;
        else
            return false;
    }

    public boolean isSign(char ch)
    {
        if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == '{' || ch == '}'
            || ch == ',' || ch == '.' || ch == ';' || ch == ':' || ch == '"' || ch == '!' || ch == '=' || ch == '&' || ch == '>' || ch == '<')
            return true;
        else
            return false;
    }

}
