package com.fenlan.compiler.token;

import java.util.ArrayList;
import java.util.List;

public class TokenTable {

    public static List<Token> tokenTable = new ArrayList<>();

    public static void setTokenTable() {
        tokenTable.add(new Token(TokenType.CONST_ID, "PI", 3.1415926));
        tokenTable.add(new Token(TokenType.CONST_ID, "E", 2.71828));
        tokenTable.add(new Token(TokenType.T, "T", 0));
        tokenTable.add(new Token(TokenType.FUNC, "SIN", 0));
        tokenTable.add(new Token(TokenType.FUNC, "COS", 0));
        tokenTable.add(new Token(TokenType.FUNC, "TAN", 0));
        tokenTable.add(new Token(TokenType.FUNC, "LN", 0));
        tokenTable.add(new Token(TokenType.FUNC, "EXP", 0));
        tokenTable.add(new Token(TokenType.FUNC, "SQRT", 0));
        tokenTable.add(new Token(TokenType.ORIGIN, "ORIGIN", 0));
        tokenTable.add(new Token(TokenType.SCALE, "SCALE", 0));
        tokenTable.add(new Token(TokenType.ROT, "ROT", 0));
        tokenTable.add(new Token(TokenType.IS, "IS", 0));
        tokenTable.add(new Token(TokenType.FOR, "FOR", 0));
        tokenTable.add(new Token(TokenType.FROM, "FROM", 0));
        tokenTable.add(new Token(TokenType.TO, "TO", 0));
        tokenTable.add(new Token(TokenType.STEP, "STEP", 0));
        tokenTable.add(new Token(TokenType.DRAW, "DRAW", 0));
    }
}
