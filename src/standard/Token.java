package standard;

public class Token
{
    private int x_coor;
    private int y_coor;
    private String value;
    private double numValue;
    //private int IntValue;
    private TokenType type;

    public Token(int x, int y, String val, TokenType type)
    {
        x_coor = x;
        y_coor = y;
        value = val;
        this.type = type;
    }

    public Token(int x, int y, double numberBis, TokenType type)
    {
        x_coor = x;
        y_coor = y;
        numValue = numberBis;
        this.type = type;
    }

    /*public Token(int x, int y, int numberBis, TokenType type)
    {
        x_coor = x;
        y_coor = y;
        numValue = numberBis;
        this.type = type;
    }*/

    public int getX_coor() {
        return x_coor;
    }

    public void setX_coor(int x_coor) {
        this.x_coor = x_coor;
    }

    public int getY_coor() {
        return y_coor;
    }

    public void setY_coor(int y_coor) {
        this.y_coor = y_coor;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public double getNumValue() {
        return numValue;
    }

    public void setNumValue(double numValue) {
        this.numValue = numValue;
    }

    /*public int getIntValue() {
        return IntValue;
    }*/
}
