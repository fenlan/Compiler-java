package com.fenlan.compiler.parser;

import com.fenlan.compiler.scanner.Scanner;
import com.fenlan.compiler.token.Token;
import com.fenlan.compiler.token.TokenType;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static Token token;
    public static List<double[]> list = new ArrayList<>();
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
        double[] tuple;
        double start, end, step, x_ptr, y_ptr;
        MatchToken(TokenType.FOR);
        MatchToken(TokenType.T);
        MatchToken(TokenType.FROM);
        start = Expression();
        MatchToken(TokenType.TO);
        end = Expression();
        MatchToken(TokenType.STEP);
        step = Expression();
        Scanner.flag = true;            // 开始缓冲DRAW里面的记号
        MatchToken(TokenType.DRAW);
        for(Parameter = start; Parameter <= end; Parameter += step)
        {
            MatchToken(TokenType.L_BRACKET);
            x_ptr = Expression();
            MatchToken(TokenType.COMMA);
            y_ptr = Expression();
            MatchToken(TokenType.R_BRACKET);
            tuple = CalcCoord(x_ptr, y_ptr);
            list.add(tuple);
            if (Parameter+step <= end) {
                Scanner.BackToken();
                token = Scanner.GetToken();
            }
        }
        Scanner.DrawBuffer = "";        // 将DRAW缓冲记号清空
        Scanner.flag = false;           // 结束DRAW缓冲记号
        Parameter = 0;                  // 将参数T的值重置0
    }

    private static double Expression() {
        double left = Term();

        while (true) {
            switch (token.getType()) {
                case TokenType.PLUS:
                    MatchToken(token.getType());
                    left += Term();
                    break;
                case TokenType.MINUS:
                    MatchToken(token.getType());
                    left -= Term();
                    break;
                default:
                    return left;
            }
        }
    }

    private static double Term() {
        double left = Factor();

        while (true) {
            switch (token.getType()) {
                case TokenType.MUL:
                    MatchToken(token.getType());
                    left *= Factor();
                    break;
                case TokenType.DIV:
                    MatchToken(token.getType());
                    double tmp = Factor();
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

    private static double Factor() {
        double right;
        if (token.getType() == TokenType.PLUS)
        {
            MatchToken(TokenType.PLUS);
            right = Factor();
        }
        else if (token.getType() == TokenType.MINUS)
        {
            MatchToken(TokenType.MINUS);
            right = Factor();
            right = 0 - right;
        }
        else right = Component();
        return right;
    }

    private static double Component() {
        double left, right;
        left = Atom();
        if(token.getType() == TokenType.POWER)
        {
            MatchToken(TokenType.POWER);
            right = Component();
            left = Math.pow(left, right);
        }
        return left;
    }

    private static double Atom() {
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
                                    tmp = Expression();
                                    address = Math.sin(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "COS" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression();
                                    address = Math.cos(tmp);
                                    MatchToken(TokenType.R_BRACKET);

                                    break;
                    case "TAN" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression();
                                    address = Math.tan(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "LN"  :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression();
                                    address = Math.log(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "EXP" :    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression();
                                    address = Math.exp(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    case "SQRT":    MatchToken(TokenType.FUNC);
                                    MatchToken(TokenType.L_BRACKET);
                                    tmp = Expression();
                                    address = Math.sqrt(tmp);
                                    MatchToken(TokenType.R_BRACKET);
                                    break;
                    default:        address = 0;
                }
                break;
            case TokenType.L_BRACKET:
                MatchToken(TokenType.L_BRACKET);
                address = Expression();
                MatchToken(TokenType.R_BRACKET);
                break;
            default:
                SyntaxError(2);
        }
        return address;
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
