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

    public Lexer (String file_loc, int i) throws FileNotFoundException
    {
        if (i == 0)
            scanner = new Skaner(file_loc, i);
        else
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


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////jednoelementowe tokeny
        if (character == ' ')
            return new Token(++x_coor, y_coor, " ", TokenType.SPACE);                   //TODO dlaczego nie dodaje x?
        else if (character == '+' || character == '-')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.ADDITIVE_OP);
        else if (character == '*')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.MULTIPLICATIVE_OP);
        else if (character == '!')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.UNARY_OP);
        else if (character == '(')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.LEFT_PARENTHESIS);
        else if (character == ')')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.RIGHT_PARENTHESIS);
        else if (character == '[')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.LEFT_BRACKET);
        else if (character == ']')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.RIGHT_BRACKET);
        else if (character == '{')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.LEFT_BRACE);
        else if (character == '}')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.RIGHT_BRACE);
        else if (character == ',')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.COMMA);
        else if (character == ':')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.COLON);
        else if (character == ';')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.SEMICOLON);
        else if (character == '|')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.OR_OP);
        else if (character == '&')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.AND_OP);

       //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////jedno lub dwuelementowe tokeny
        else if (character == '=' || character == '/' || character == '<' || character == '>')
            return findDoubleToken();

        /////////////////////                   //////////////   /////////////////////////////////////////////////////////////////////////////////////       wczytywanie slów
        if (TokenPrefix.isLetter(character))                //modyfikacja - zmienne nie mogą zaczynać się cyfrą
            return findName();

        /////////////////////////////////////////////////////////////                    /////////////////////////////////////////////////////////////// Strings

        if (character == '"')
            return findString();

        ///////////////////////////     //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Numbers

        if (TokenPrefix.isDigit(character))
            return findNumber();


        return new Token(++x_coor, y_coor, "BUG", TokenType.UNKNOWN);
    }
///////////////////////////////////////////////////////                         ////////////////////////////////////////// wyszukiwanie po kropce
    public Token afterDot(char CharacterBis, String number)
    {
            number = number + characterBis;
            characterBis = scanner.readNextChar();
            while (TokenPrefix.isDigit(characterBis))
            {
                number = number + Character.toString(characterBis);
                characterBis = scanner.readNextChar();
            }
            if (TokenPrefix.isSpace(characterBis) || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis) || TokenPrefix.isSign(characterBis))
                return new Token(x_coor+= number.length(), y_coor, number, TokenType.NUMBER);                           //sciezka 2.015 - dobra
            else
            {
                //return findEnd(characterBis, number);
                while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                {
                    number = number + characterBis;
                    characterBis = scanner.readNextChar();
                }
                return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 1.0abc - zla
            }
    }

    ///////////////////////////////////////////////////                                     ///////////////////////////////////  wyszukiwanie stringów
    public Token findString()
    {
        x_coor++;
        String message = new String();
        //message += character;
        character = scanner.readNextChar();
        while (!(character == '"' || TokenPrefix.isEOL(character) || TokenPrefix.isEOF(character)))
        {
            message += character;
            character = scanner.readNextChar();
        }
        if (character == '"')
            return new Token(x_coor += message.length()+1, y_coor, message, TokenType.STRING);
        else if (TokenPrefix.isEOL(character))
            return new Token(x_coor += message.length(), y_coor+= 0.5, message, TokenType.UNKNOWN);
        else
            return new Token(x_coor += message.length(), y_coor, message, TokenType.UNKNOWN);
    }
    /////////////////////////////////////                       /////////////////////////////////           //////////////////////////////// wyszukuje nazwy zmiennych
    public Token findName()
    {
        String name = new String();
        name += Character.toString(character);

        characterBis = scanner.readNextChar();
        while (TokenPrefix.isLetter(characterBis) || TokenPrefix.isDigit(characterBis))
        {
            name += Character.toString(characterBis);
            characterBis = scanner.readNextChar();
        }

        switch (name) {                                                                             //name musi zaczynac sie litera
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
    /////////////////////////////////////////                           //////////////////////////////////////
    public Token findDoubleToken()
    {
        if (character == '=')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '=')
            {
                characterBis = '\0';
                return new Token(x_coor+=2, y_coor, "==", TokenType.EQUAL_OP);
            }
            return new Token(++x_coor, y_coor, "=", TokenType.ASSIGNMENT_OP);
        }
        else if (character == '/')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '/')
            {
                while (!(TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    characterBis = scanner.readNextChar();
                characterBis = '\0';
                return new Token(x_coor+=2, y_coor+= 0.5, "//", TokenType.COMMENT);
            }
            return new Token(++x_coor, y_coor, "/", TokenType.MULTIPLICATIVE_OP);
        }
        else if (character == '<')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '=')
            {
                characterBis = '\0';
                return new Token(x_coor+=2, y_coor, "<=", TokenType.COMPARE_OP);
            }
            return new Token(++x_coor, y_coor, "<", TokenType.COMPARE_OP);
        }
        else //if (character == '>')
        {
            characterBis = scanner.readNextChar();
            if(characterBis == '=')
            {
                characterBis = '\0';
                return new Token(x_coor+2, y_coor, ">=", TokenType.COMPARE_OP);
            }
            return new Token(++x_coor, y_coor, ">", TokenType.COMPARE_OP);
        }
    }
    /////////////////////////////////////////////////////                               ////////////////////////////////////// szukanie numoerow
    public Token findNumber()
    {
        String number = new String();

        if (character == '0')                                                       //liczba zaczyna się zerem
        {
            number = number + Character.toString(character);
            character = scanner.readNextChar();
            if (character == '.')
            {
                characterBis = character;
                return afterDot(characterBis, number);
            }
            else
            {
                if(character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))                       // jeśli samo 0
                {
                    characterBis = character;
                    return new Token(++x_coor, y_coor, number, TokenType.NUMBER);                                // sciezka int x = 0 - dobra
                }
                else                                                                        //niedozwolona kombinacja
                {
                    number = number + Character.toString(character);
                    characterBis = scanner.readNextChar();
                    while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    {
                        number = number + Character.toString(characterBis);
                        characterBis = scanner.readNextChar();
                    }
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 0abc - zla
                }
            }
        }
        else //if (TokenPrefix.isDigit(character))                                    //liczba zaczyna się nie zerem
        {
            number += character;
            character = scanner.readNextChar();

            if (character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))                      //jedna cyfra
            {
                characterBis = character;
                return new Token(++x_coor, y_coor, number, TokenType.NUMBER);
            }
            else if (TokenPrefix.isDigit(character))                                    // jesli int
            {
                number += character;
                characterBis = scanner.readNextChar();
                while (TokenPrefix.isDigit(characterBis))
                {
                    number = number + characterBis;
                    characterBis = scanner.readNextChar();
                }

                if(characterBis == ' ' || TokenPrefix.isSign(characterBis) || TokenPrefix.isEOF(characterBis) || TokenPrefix.isEOL(characterBis))                       // jeśli sam int
                    return new Token(++x_coor, y_coor, number.toString(), TokenType.NUMBER);                                // sciezka int x = 123 - dobra
                else if (characterBis == '.')
                {
                    return afterDot(characterBis, number);
                }
                else                                                                        //niedozwolona kombinacja
                {
                    number = number + characterBis;
                    characterBis = scanner.readNextChar();
                    while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                    {
                        number = number + Character.toString(characterBis);
                        characterBis = scanner.readNextChar();
                    }
                    return new Token(x_coor += number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 123abc - zla
                }
            }
            else //if (character == '.')                                              // jesli double
            {
                characterBis = character;
                return afterDot(characterBis, number);
            }
        }
    }
}
