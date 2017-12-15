package com.fenlan.compiler.token;

public class Token {

    private int   type;       // 记号类型
    private String      lexeme;     // 构成记号字符串
    private double      value;      // 记号为常数时,记号的值


    public Token() {
    }

    public Token(int type, String lexeme, double value) {
        this.type = type;
        this.lexeme = lexeme;
        this.value = value;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}
