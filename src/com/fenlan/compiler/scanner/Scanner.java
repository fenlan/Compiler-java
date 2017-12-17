package com.fenlan.compiler.scanner;

import com.fenlan.compiler.token.Token;
import com.fenlan.compiler.token.TokenTable;
import com.fenlan.compiler.token.TokenType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

    public static int LineNo;
    private static int TOKEN_LEN = 100;
    private static List TokenBuffer = new ArrayList<String>();
    public static PushbackInputStream reader = null;
    private static int position = 0;

    public static boolean InitScanner(String fileName) {
        File srcFile = new File(fileName);
        TokenTable.setTokenTable();         // 初始化记号表(否则记号表为空)

        try {
            reader = new PushbackInputStream(new FileInputStream(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (reader == null)        return false;
        else {
            LineNo = 1;
            return true;
        }
    }

    public static void CloseScanner() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static char GetChar() {
        int temp = 0;
        char _char = '0';
        try {
            if ((temp = reader.read()) == -1)        return 0;
            _char = (char)temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return _char;
    }

    private static void BackChar(char _char) {
        if (_char != 0) {
            try {
                reader.unread(_char);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void AddCharTokenString(char _char) {
        int TokenLength = TokenBuffer.size();
        if (TokenLength + 1 >= TOKEN_LEN)	return;
        TokenBuffer.add(_char);
    }

    private static void EmptyTokenString() {
        TokenBuffer.clear();
    }

    private static Token JudgeKeyToken(String IDString) {
        for (int i = 0; i < TokenTable.tokenTable.size(); ++i)
        {
            if (IDString.equals(TokenTable.tokenTable.get(i).getLexeme())) {
                return TokenTable.tokenTable.get(i);
            }
        }
        return new Token(TokenType.ERRTOKEN, "", 0);
    }

    public static Token GetToken() {
        char _char;
        Token token = new Token(TokenType.ERRTOKEN, "", 0);

        EmptyTokenString();
        while (true)
        {
            _char = GetChar();
            if (_char == 0)
            {
                token.setType(TokenType.NONTOKEN);
                return token;
            }
            if (_char == '\n')		LineNo++;
            if (!Character.isWhitespace(_char))	break;
        }
        AddCharTokenString(_char);
        if (Character.isAlphabetic(_char))		// 字符记号
        {
            while (true)
            {
                _char = GetChar();
                if (Character.isAlphabetic(_char) || Character.isDigit(_char))		AddCharTokenString(_char);
                else					break;
            }
            BackChar(_char);
            token = JudgeKeyToken(listToString(TokenBuffer));
            token.setLexeme(listToString(TokenBuffer));            // 防止没有识别记号时输出为null
            return token;
        }
        else if (Character.isDigit(_char))		// 数字常数记号
        {
            while (true)
            {
                _char = GetChar();
                if (Character.isDigit(_char))		AddCharTokenString(_char);
                else					break;
            }
            if (_char == '.')
            {
                AddCharTokenString(_char);
                while (true)
                {
                    _char = GetChar();
                    if (Character.isDigit(_char))	AddCharTokenString(_char);
                    else				break;
                }
            }
            BackChar(_char);
            token.setType(TokenType.CONST_ID);
            token.setValue(Double.valueOf(listToString(TokenBuffer)));
            token.setLexeme(listToString(TokenBuffer));
            return token;
        }
        else {				// 操作符号
            switch (_char)
            {
                case ';': token.setType(TokenType.SEMICO); token.setLexeme(";"); break;
                case '(': token.setType(TokenType.L_BRACKET); token.setLexeme("("); break;
                case ')': token.setType(TokenType.R_BRACKET); token.setLexeme(")"); break;
                case ',': token.setType(TokenType.COMMA); token.setLexeme(","); break;
                case '+': token.setType(TokenType.PLUS); token.setLexeme("+"); break;
                case '-':
                    _char = GetChar();
                    if (_char == '-')
                    {
                        while (_char != '\n' && _char != 0)	_char = GetChar();
                        BackChar(_char);
                        return GetToken();
                    }
                    else
                    {
                        BackChar(_char);
                        token.setType(TokenType.MINUS);
                        token.setLexeme("-");
                        break;
                    }
                case '/':
                    _char = GetChar();
                    if (_char == '/')
                    {
                        while (_char != '\n' && _char != 0)	_char = GetChar();
                        BackChar(_char);
                        return GetToken();
                    }
                    else
                    {
                        BackChar(_char);
                        token.setType(TokenType.DIV);
                        token.setLexeme("/");
                        break;
                    }
                case '*':
                    _char = GetChar();
                    if (_char == '*')
                    {
                        token.setType(TokenType.POWER);
                        token.setLexeme("**");
                        break;
                    }
                    else
                    {
                        BackChar(_char);
                        token.setType(TokenType.MUL);
                        token.setLexeme("*");
                        break;
                    }
                default: token.setType(TokenType.ERRTOKEN); break;
            }
        }
        token.setLexeme(listToString(TokenBuffer));
        return token;
    }

    private static String listToString(List list) {
        String result = "";
        for (Object i : list) {
            result = result + i.toString();
        }
        return result;
    }
}
