package com.fenlan.compiler.parser;

import com.fenlan.compiler.scanner.Scanner;
import com.fenlan.compiler.token.Token;
import com.fenlan.compiler.token.TokenType;

public class Parser {

    private static Token token;
    private static double	Parameter=0,
                            Origin_x=0,Origin_y=0,
                            Scale_x=1,	Scale_y=1,
                            Rot_angle=0;

    private static void FetchToken() {
        token = Scanner.GetToken();
        if(token.getType() == TokenType.ERRTOKEN)		SyntaxError(1);
    }

    private static void MatchToken(int tokenType) {
        if(token.getType() != tokenType)		SyntaxError(2);
        FetchToken();
    }

    private static void SyntaxError(int case_of) {
        switch(case_of)
        {
            case 1:		ErrMsg(Scanner.LineNo, " Error Token ", token.getLexeme());break;
            case 2:		ErrMsg(Scanner.LineNo, " Not the expected token ", token.getLexeme());break;
        }
    }

    private static void ErrMsg(int LineNo, String descrip, String string) {
        System.out.println("Line No " + LineNo + ": " + descrip + " " + string);
        Scanner.CloseScanner();
        System.exit(0);
    }

    private void Parser(String FileName) {
        if(!Scanner.InitScanner(FileName))
        {
            System.out.println("Open Source File Error !");
            return;
        }
        FetchToken();
        Program();
        Scanner.CloseScanner();
    }

    private static void Program() {
        while(token.getType() != TokenType.NONTOKEN)
        {
            Statement();
            MatchToken(TokenType.SEMICO);
        }
    }

    private static void Statement(){
        switch(token.getType())
        {
            case TokenType.ORIGIN:	OriginStatement();break;
            case TokenType.SCALE:		ScaleStatement();break;
            case TokenType.ROT:		RotStatement();break;
            case TokenType.FOR:		ForStatement();break;
            default :		SyntaxError(2);
        }
    }

    private static void OriginStatement() {
        MatchToken(TokenType.ORIGIN);
        MatchToken(TokenType.IS);
        MatchToken(TokenType.L_BRACKET);
        Origin_x  = Expression();
        MatchToken(TokenType.COMMA);
        Origin_y = Expression();
        MatchToken(TokenType.R_BRACKET);
    }

    private static void RotStatement() {
        MatchToken(TokenType.ROT);
        MatchToken(TokenType.IS);
        Rot_angle = Expression();
    }

    private static void ScaleStatement() {
        MatchToken(TokenType.SCALE);
        MatchToken(TokenType.IS);
        MatchToken(TokenType.L_BRACKET);
        Scale_x = Expression();
        MatchToken(TokenType.COMMA);
        Scale_y = Expression();
        MatchToken(TokenType.R_BRACKET);
    }

    private static void ForStatement() {
        double start, end, step, x_ptr, y_ptr;
        MatchToken(TokenType.FOR);
        MatchToken(TokenType.T);
        MatchToken(TokenType.FROM);
        start = Expression();
        MatchToken(TokenType.TO);
        end = Expression();
        MatchToken(TokenType.STEP);
        step = Expression();
        MatchToken(TokenType.DRAW);
        MatchToken(TokenType.L_BRACKET);
        x_ptr = Expression();
        MatchToken(TokenType.COMMA);
        y_ptr = Expression();
        MatchToken(TokenType.R_BRACKET);
        DrawLoop(start, end, step, x_ptr, y_ptr);
    }

    private static double Expression() {
        
    }

    private static double Term() {

    }

    private static double Factor() {

    }

    private static double Component() {

    }

    private static double Atom() {

    }

    private static void DrawLoop(double start, double end, double step, double HorPtr, double VerPtr) {

    }
}
