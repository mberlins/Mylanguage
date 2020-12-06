package standard;

public class Token
{
    private int x_coor;
    private double y_coor;
    private String value;
    private TokenType type;

    public Token(int x, double y, String val, TokenType type)
    {
        x_coor = x;
        y_coor = y;
        value = val;
        this.type = type;
    }

    public int getX_coor() {
        return x_coor;
    }

    public void setX_coor(int x_coor) {
        this.x_coor = x_coor;
    }

    public double getY_coor() {
        return y_coor;
    }

    public void setY_coor(double y_coor) {
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


}
