package com.fenlan.compiler.parser;

import com.fenlan.compiler.main.Main;
import com.fenlan.compiler.scanner.Scanner;
import com.fenlan.compiler.token.Token;
import com.fenlan.compiler.token.TokenType;

import java.io.IOException;

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
        System.exit(1);
    }

    public static void ParserAn(String FileName) {
        if(!Scanner.InitScanner(FileName))
        {
            System.out.println("Open Source File Error !");
            return;
        }
        FetchToken();
        System.out.println("Parser");
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
        // System.out.println("Entering Statement");
        switch(token.getType())
        {
            case TokenType.ORIGIN:	OriginStatement();break;
            case TokenType.SCALE:		ScaleStatement();break;
            case TokenType.ROT:		RotStatement();break;
            case TokenType.FOR:		ForStatement();break;
            default :		SyntaxError(2);
        }
        // System.out.println("Exited Statement");
    }

    private static void OriginStatement() {
        MatchToken(TokenType.ORIGIN);
        MatchToken(TokenType.IS);
        MatchToken(TokenType.L_BRACKET);
        Origin_x  = Expression(false);
        MatchToken(TokenType.COMMA);
        Origin_y = Expression(false);
        MatchToken(TokenType.R_BRACKET);
    }

    private static void RotStatement() {
        MatchToken(TokenType.ROT);
        MatchToken(TokenType.IS);
        Rot_angle = Expression(false);
    }

    private static void ScaleStatement() {
        MatchToken(TokenType.SCALE);
        MatchToken(TokenType.IS);
        MatchToken(TokenType.L_BRACKET);
        Scale_x = Expression(false);
        MatchToken(TokenType.COMMA);
        Scale_y = Expression(false);
        MatchToken(TokenType.R_BRACKET);
    }

    private static void ForStatement() {
        double[] tuple;
        double x, y;
        double start, end, step, x_ptr, y_ptr;
        MatchToken(TokenType.FOR);
        MatchToken(TokenType.T);
        MatchToken(TokenType.FROM);
        start = Expression(false);
        MatchToken(TokenType.TO);
        end = Expression(false);
        MatchToken(TokenType.STEP);
        step = Expression(false);
        Scanner.flag = true;
        MatchToken(TokenType.DRAW);
        for(Parameter = start; Parameter <= end; Parameter += step)
        {
            MatchToken(TokenType.L_BRACKET);
            x_ptr = Expression(false);
            MatchToken(TokenType.COMMA);
            y_ptr = Expression(false);
            MatchToken(TokenType.R_BRACKET);
            tuple = CalcCoord(x_ptr, y_ptr);
            x = tuple[0];   y = tuple[1];
            Main.graphics.drawOval((int)x, (int)y, 2, 2);
            if (Parameter+step < end) {
                Scanner.BackToken();
                token = Scanner.GetToken();
            }
        }
        Scanner.flag = false;
    }

    private static double Expression(boolean get) {
        double left = Term(get);

        while (true) {
            switch (token.getType()) {
                case TokenType.PLUS:
                    left += Term(true);
                    break;
                case TokenType.MINUS:
                    left -= Term(true);
                    break;
                default:
                    return left;
            }
        }
    }

    private static double Term(boolean get) {
        double left = Component(get);

        while (true) {
            switch (token.getType()) {
                case TokenType.MUL:
                    left *= Component(true);
                    break;
                case TokenType.DIV:
                    double tmp = Component(true);
                    if (tmp != 0.0) {
                        left /= tmp;
                        break;
                    }
                    ErrMsg(Scanner.LineNo, "0 can not DIV", String.valueOf(tmp));
                default:
                    return left;
            }
        }
    }

    private static double Component(boolean get) {
        double left, right;
        left = Atom(get);
        if(token.getType() == TokenType.POWER)
        {
            MatchToken(TokenType.POWER);
            right = Component(false);
            left = Math.pow(left, right);
        }
        return left;
    }

    private static double Atom(boolean get) {
        if (get)    FetchToken();

        double address = 0, tmp;
        switch(token.getType())
        {
            case TokenType.CONST_ID:
                address = token.getValue();
                MatchToken(TokenType.CONST_ID);
                break;
            case TokenType.T:
                address = Parameter;
                MatchToken(TokenType.T);
                break;
            case TokenType.FUNC:
                switch (token.getLexeme()) {
                    case "SIN" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression(false);
                                    address = Math.sin(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "COS" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression(false);
                                    address = Math.cos(tmp);
                                    MatchToken(TokenType.R_BRACKET);

                                    break;
                    case "TAN" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression(false);
                                    address = Math.tan(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "LN"  :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression(false);
                                    address = Math.log(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "EXP" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression(false);
                                    address = Math.exp(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "SQRT":    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression(false);
                                    address = Math.sqrt(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    default:        address = 0;
                }
                break;
            case TokenType.L_BRACKET:
                MatchToken(TokenType.L_BRACKET);
                address = Expression(false);
                MatchToken(TokenType.R_BRACKET);
                break;
            default:
                SyntaxError(2);
        }
        return address;
    }

    public static void DrawLoop(double start, double end, double step, double HorPtr, double VerPtr) {
        double[] tuple;
        double x, y;
        for(Parser.Parameter = start; Parser.Parameter <= end; Parser.Parameter += step)
        {
            tuple = CalcCoord(HorPtr, VerPtr);
            x = tuple[0];   y = tuple[1];
            Main.graphics.drawOval((int)x, (int)y, 2, 2);
        }
        System.out.println("Drawing.....");
    }

    private static double[] CalcCoord(double Hor_Exp, double Ver_Exp) {
        double[] tuple = new double[2];
        double HorCord, VerCord, Hor_tmp;
        HorCord = Hor_Exp;
        VerCord = Ver_Exp;
        HorCord *= Parser.Scale_x;
        VerCord *= Parser.Scale_y;
        Hor_tmp = HorCord * Math.cos(Parser.Rot_angle) + VerCord * Math.sin(Parser.Rot_angle);
        VerCord = VerCord * Math.cos(Parser.Rot_angle) - HorCord * Math.sin(Parser.Rot_angle);
        HorCord = Hor_tmp;
        HorCord += Parser.Origin_x;
        VerCord += Parser.Origin_y;
        tuple[0] = HorCord;
        tuple[1] = VerCord;

        return tuple;
    }
}
