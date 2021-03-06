package standard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Lexer
{
    private Skaner scanner;
    private TokenType type;
    private List<Character> characters = new ArrayList<>();
    private char character;
    private TokenPrefix tokenPrefix;
    int x_coor = 0;
    int y_coor = 0;

    public List<Character> peekCharacters() {
        return characters;
    }

    public Lexer (File file) throws FileNotFoundException
    {
            scanner = new Skaner(file);
    }

    public Lexer (String code)
    {
        scanner = new Skaner(code);
    }

    public Token nextToken()
    {
        if (characters.size() == 0)
            character = scanner.readNextChar();
        else
        {
            character = characters.get(0);
            characters.remove(0);
        }

        if (TokenPrefix.isEOF(character))
            return new Token(x_coor,y_coor, "EOF", TokenType.EOF);

        if (TokenPrefix.isEOL(character))
        {
            if (character == '\r')
                ++y_coor;
            x_coor = 0;
            return nextToken();
        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////jednoelementowe tokeny
        if (character == ' ' || character == '\t')
        {
            ++x_coor;
            return nextToken();
        }
        else if (character == '+' )
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.ADDITIVE_OP);
        else if (character == '-')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.MINUS_OP);
        else if (character == '*')
            return new Token(++x_coor, y_coor, Character.toString(character), TokenType.MULTIPLICATIVE_OP);
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
        else if (character == '=' || character == '/' || character == '<' || character == '>' || character == '!')
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
    public Token afterDot(char characterBis, double numberBis)
    {
            //number = number + characterBis;
            characterBis = scanner.readNextChar();
            ++x_coor;
            int i = 1;
            while (TokenPrefix.isDigit(characterBis))
            {
                double tmp = characterBis - 48;
                numberBis = numberBis + tmp/Math.pow(10, i);
                i++;
                x_coor++;
                characterBis = scanner.readNextChar();
            }
            characters.add(characterBis);
            if (TokenPrefix.isSpace(characterBis) || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis) || TokenPrefix.isSign(characterBis))
                return new Token(x_coor, y_coor, numberBis, TokenType.NUMBER);                           //sciezka 2.015 - dobra
            else
            {
                String number = String.valueOf(numberBis);
                characters.remove(characters.size()-1);
                while (!(TokenPrefix.isSign(characterBis) || characterBis == ' ' || TokenPrefix.isEOL(characterBis) || TokenPrefix.isEOF(characterBis)))
                {
                    number = number + characterBis;
                    ++x_coor;
                    characterBis = scanner.readNextChar();
                }
                characters.add(characterBis);
                return new Token(x_coor, y_coor, number, TokenType.UNKNOWN);                         // sciezka 1.0abc - zla
            }
    }

    ///////////////////////////////////////////////////                                     ///////////////////////////////////  wyszukiwanie stringów
    public Token findString()
    {
        x_coor++;
        String message = new String();
        character = scanner.readNextChar();
        while (!(character == '"' || TokenPrefix.isEOL(character) || TokenPrefix.isEOF(character)))
        {
            message += character;
            character = scanner.readNextChar();
            if(character == '\\')
            {
                character = scanner.readNextChar();
                message+= character;
                character = scanner.readNextChar();
            }
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

        character = scanner.readNextChar();
        while (TokenPrefix.isLetter(character) || TokenPrefix.isDigit(character))
        {
            name += Character.toString(character);
            character = scanner.readNextChar();
        }
        characters.add(character);

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
            case "print":
                return new Token(x_coor += name.length(), y_coor, name, TokenType.PRINT);
            default: return new Token(x_coor += name.length(), y_coor, name, TokenType.NAME);
        }
    }
    /////////////////////////////////////////                           //////////////////////////////////////
    public Token findDoubleToken()
    {
        if (character == '=')
        {
            character = scanner.readNextChar();
            if(character == '=')
                return new Token(x_coor+=2, y_coor, "==", TokenType.EQUAL_OP);
            characters.add(character);
            return new Token(++x_coor, y_coor, "=", TokenType.ASSIGNMENT_OP);
        }
        if (character == '!')
        {
            character = scanner.readNextChar();
            if(character == '=')
                return new Token(x_coor+=2, y_coor, "!=", TokenType.UNEQUAL_OP);
            characters.add(character);
            return new Token(++x_coor, y_coor, "!", TokenType.NEGATION_OP);
        }
        else if (character == '/')
        {
            character = scanner.readNextChar();
            if(character == '/')
            {
                while (!(TokenPrefix.isEOL(character) || TokenPrefix.isEOF(character)))
                    character = scanner.readNextChar();
                characters.add(character);
                return nextToken();
            }
            return new Token(++x_coor, y_coor, "/", TokenType.DIVIDE_OP);
        }
        else if (character == '<')
        {
            character = scanner.readNextChar();
            if(character == '=')
                return new Token(x_coor+=2, y_coor, "<=", TokenType.SMALLER_EQUAL);
            characters.add(character);
            return new Token(++x_coor, y_coor, "<", TokenType.SMALLER);
        }
        else //if (character == '>')
        {
            character = scanner.readNextChar();
            if(character == '=')
                return new Token(x_coor+2, y_coor, ">=", TokenType.BIGGER_EQUAL);
            characters.add(character);
            return new Token(++x_coor, y_coor, ">", TokenType.BIGGER);
        }
    }
    /////////////////////////////////////////////////////                               ////////////////////////////////////// szukanie numoerow
    public Token findNumber()
    {
        String number = new String();
        int numberBis = 0;

        if (character == '0')                                                       //liczba zaczyna się zerem
        {
            ++x_coor;
            character = scanner.readNextChar();
            if (character == '.')
                return afterDot(character, numberBis);
            else
            {
                if(character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))                       // jeśli samo 0
                {
                    characters.add(character);
                    return new Token(x_coor, y_coor, numberBis, TokenType.NUMBER);                                // sciezka int x = 0 - dobra
                }
                else                                                                        //niedozwolona kombinacja
                {
                    number = String.valueOf(numberBis);
                    number = number + character;
                    character = scanner.readNextChar();
                    while (!(TokenPrefix.isSign(character) || character == ' ' || TokenPrefix.isEOL(character) || TokenPrefix.isEOF(character)))
                    {
                        number = number + character;
                        character = scanner.readNextChar();
                    }
                    characters.add(character);
                    return new Token(x_coor+= number.length(), y_coor, number, TokenType.UNKNOWN);                         // sciezka 0abc - zla
                }
            }
        }
        else //if (TokenPrefix.isDigit(character))                                    //liczba zaczyna się nie zerem
        {
            int tmp = character;
            numberBis = numberBis*10 + (tmp - 48);
            ++x_coor;
            character = scanner.readNextChar();

            if (character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))                      //jedna cyfra
            {
                characters.add(character);
                return new Token(x_coor, y_coor, numberBis, TokenType.NUMBER);
            }
            else if (TokenPrefix.isDigit(character))                                    // jesli int
            {
                tmp = character;
                numberBis = numberBis*10 + (tmp - 48);
                ++x_coor;
                character = scanner.readNextChar();
                while (TokenPrefix.isDigit(character))
                {
                    tmp = character;
                    numberBis = numberBis*10 + (tmp - 48);
                    ++x_coor;
                    character = scanner.readNextChar();
                }

                if(character == ' ' || TokenPrefix.isSign(character) || TokenPrefix.isEOF(character) || TokenPrefix.isEOL(character))                       // jeśli sam int
                {
                    characters.add(character);
                    return new Token(x_coor, y_coor, numberBis, TokenType.NUMBER);                                // sciezka int x = 123 - dobra
                }
                else if (character == '.')
                {
                    return afterDot(character, numberBis);
                }
                else                                                                        //niedozwolona kombinacja
                {
                    number = String.valueOf(numberBis);
                    number = number + character;
                    ++x_coor;
                    character = scanner.readNextChar();
                    while (!(TokenPrefix.isSign(character) || character == ' ' || TokenPrefix.isEOL(character) || TokenPrefix.isEOF(character)))
                    {
                        number = number + character;
                        ++x_coor;
                        character = scanner.readNextChar();
                    }
                    characters.add(character);
                    return new Token(x_coor, y_coor, number, TokenType.UNKNOWN);                         // sciezka 123abc - zla
                }
            }
            else //if (character == '.')                                              // jesli double
                return afterDot(character, numberBis);
        }
    }
}
