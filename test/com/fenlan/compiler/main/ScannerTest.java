package com.fenlan.compiler.main;

import com.fenlan.compiler.scanner.Scanner;
import com.fenlan.compiler.token.Token;
import com.fenlan.compiler.token.TokenTable;
import com.fenlan.compiler.token.TokenType;

public class MainTest {
    public static void main(String[] args) {
        Token token;
//        if(args.length < 2)
//        {
//            System.out.println("please input Source File !");		return ;
//        }
        if(!Scanner.InitScanner("/home/fenlan/code/cpp/compiler/program"))
        {
            System.out.println("Open Source File Error!");
        }
        System.out.println("Token_type    String    Const    FuncP ");
        System.out.println("_______________________________________________");
        while(true)
        {
            token = Scanner.GetToken();
            if(token.getType() != TokenType.NONTOKEN)
                System.out.println(token.getType() + "    " +  token.getLexeme() + "    " + token.getValue());
            else    break;
        }
        System.out.println("_______________________________________________");
        Scanner.CloseScanner();

    }

}
