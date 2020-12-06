package standard;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Lexer
{
    private Skaner scanner;
    private TokenType type;
    private char character;
    private char characterBis = '\0';
    private TokenPrefix tokenPrefix;
    int x_coor = 0;
    double y_coor = 0;

    public Lexer (String file_loc) throws FileNotFoundException
    {
        scanner = new Skaner(file_loc);
    }

    public Token nextToken()
    {
        if (characterBis != '\0')
        {
            character = characterBis;
            characterBis = '\0';
        }
        else
        {
            character = scanner.readNextChar();
        }

        if (TokenPrefix.isEOF(character))
        {
           return new Token(x_coor,y_coor, "EOF", TokenType.EOF);
        }

        if (TokenPrefix.isEOL(character))
        {
            return new Token(x_coor = 0,y_coor+=0.5, "EOL", TokenType.EOL);
        }


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////jednoelementowe tokeny
        if (character == ' ')
            return new Token(x_coor++, y_coor, " ", TokenType.SPACE);                   //TODO dlaczego nie dodaje x?
        else if (character == '+' || character == '-')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.ADDITIVE_OP);
        else if (character == '*')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.MULTIPLICATIVE_OP);
        else if (character == '!')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.UNARY_OP);
        else if (character == '(')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.LEFT_PARENTHESIS);
        else if (character == ')')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.RIGHT_PARENTHESIS);
        else if (character == '[')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.LEFT_BRACKET);
        else if (character == ']')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.RIGHT_BRACKET);
        else if (character == '{')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.LEFT_BRACE);
        else if (character == '}')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.RIGHT_BRACE);
        else if (character == ',')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.COMMA);
        else if (character == ':')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.COLON);
        else if (character == ';')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.SEMICOLON);
        else if (character == '|')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.OR_OP);
        else if (character == '&')
            return new Token(x_coor++, y_coor, Character.toString(character), TokenType.AND_OP);

       ////////////////////////////////////////////////////////////////////////////////////////////////////////////jedno lub dwuelementowe tokeny
        else if (character == '=')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '=')
            {
                characterBis = '\0';
                return new Token(x_coor++, y_coor, "==", TokenType.EQUAL_OP);
            }
            return new Token(x_coor++, y_coor, "=", TokenType.ASSIGNMENT_OP);
        }
        else if (character == '/')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '/')
            {
                characterBis = '\0';
                return new Token(x_coor++, y_coor, "//", TokenType.COMMENT);
            }
            return new Token(x_coor++, y_coor, "/", TokenType.MULTIPLICATIVE_OP);
        }
        else if (character == '<')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '=')
            {
                characterBis = '\0';
                return new Token(x_coor++, y_coor, "<=", TokenType.COMPARE_OP);
            }
            return new Token(x_coor++, y_coor, "<", TokenType.EQUAL_OP);
        }
        else if (character == '>')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '=')
            {
                characterBis = '\0';
                return new Token(x_coor++, y_coor, ">=", TokenType.EQUAL_OP);
            }
            return new Token(x_coor++, y_coor, ">", TokenType.EQUAL_OP);
        }

        /////////////////////                   //////////////          wczytywanie slów
        if (TokenPrefix.isLetter(character))                //modyfikacja - zmienne nie mogą zaczynać się cyfrą
        {
            String name = new String();
            name += Character.toString(character);

            characterBis = scanner.readNextChar();
            while (TokenPrefix.isLetter(characterBis) || TokenPrefix.isDigit(characterBis))
            {
                name += Character.toString(characterBis);
                characterBis = scanner.readNextChar();
            }

            switch (name) {
                case "var":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.VAR);
                case "function":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.FUNCTION);
                case "def":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.DEF);
                case "if":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.IF);
                case "else":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.ELSE);
                case "while":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.LOOP);
                case "return":
                    return new Token(x_coor += name.length(), y_coor, name, TokenType.RETURN);
                default: return new Token(x_coor += name.length(), y_coor, name, TokenType.NAME);
            }
        }
        //Numbers
        String number = new String();

        if (character == '0')                                                       //liczba zaczyna się zerem
        {
            number = number + Character.toString(character);
            character = scanner.readNextChar();
            if (character == '.')                                                   //TODO zastąp
            {
                number = number + character;
                characterBis = scanner.readNextChar();
                while (TokenPrefix.isDigit(characterBis))
                {
                    number = number + Character.toString(characterBis);
                    characterBis = scanner.readNextChar();
                }
                if (TokenPrefix.isSpace(characterBis) || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis))
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.NUMBER);                           //sciezka 0.015 - dobra
                else
                {
                    //return findEnd(characterBis, number);
                    while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    {
                        number = number + Character.toString(characterBis);
                        characterBis = scanner.readNextChar();
                    }
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 0.0abc - zla
                }
            }
            else
            {
                if(character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))                       // jeśli samo 0
                {
                    characterBis = character;
                    return new Token(x_coor++, y_coor, number.toString(), TokenType.NUMBER);                                // sciezka int x = 0 - dobra
                }
                else                                                                        //niedozwolona kombinacja
                {
                    number = number + Character.toString(character);
                    characterBis = scanner.readNextChar();
                    //return findEnd(characterBis, number);
                    while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    {
                        number = number + Character.toString(characterBis);
                        characterBis = scanner.readNextChar();
                    }
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 0abc - zla
                }
            }                                                                          //TODO koniec do zastapienia
        }
        else if (TokenPrefix.isDigit(character))                                    //liczba zaczyna się nie zerem
        {
            number += character;
            character = scanner.readNextChar();

            if (character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))
            {
                characterBis = character;
                return new Token(x_coor++, y_coor, number.toString(), TokenType.NUMBER);
            }
            else if (TokenPrefix.isDigit(character))                                    // jesli int
            {
                characterBis = scanner.readNextChar();
                while (TokenPrefix.isDigit(characterBis))
                {
                    number = number + Character.toString(characterBis);
                    characterBis = scanner.readNextChar();
                }

                if(characterBis == ' ' || TokenPrefix.isSign(characterBis) || TokenPrefix.isEOF(characterBis) || TokenPrefix.isEOL(characterBis))                       // jeśli sam int
                    return new Token(x_coor++, y_coor, number.toString(), TokenType.NUMBER);                                // sciezka int x = 123 - dobra
                else                                                                        //niedozwolona kombinacja
                {
                    number = number + Character.toString(characterBis);
                    characterBis = scanner.readNextChar();
                    //return findEnd(characterBis, number);
                    while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    {
                        number = number + Character.toString(characterBis);
                        characterBis = scanner.readNextChar();
                    }
                    return new Token(x_coor += number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 123abc - zla
                }
            }
            else if (character == '.')                                              // jesli double
            {
                number = number + character;
                characterBis = scanner.readNextChar();
                while (TokenPrefix.isDigit(characterBis))
                {
                    number = number + Character.toString(characterBis);
                    characterBis = scanner.readNextChar();
                }
                if (TokenPrefix.isSpace(characterBis) || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis))
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.NUMBER);                           //sciezka 2.015 - dobra
                else
                {
                    //return findEnd(characterBis, number);
                    while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    {
                        number = number + Character.toString(characterBis);
                        characterBis = scanner.readNextChar();
                    }
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 1.0abc - zla
                }
            }
        }
        return new Token(x_coor++, y_coor, "UNKNOWN", TokenType.UNKNOWN);
    }


    /*public Token findEnd(char ch, String number)
    {
        while (!(TokenPrefix.isSign(ch) || ch == ' ' || TokenPrefix.isEOL(ch) || TokenPrefix.isEOF(ch)))
        {
            number = number + Character.toString(ch);
            ch = scanner.readNextChar();
        }
        return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);
    }*/
}
