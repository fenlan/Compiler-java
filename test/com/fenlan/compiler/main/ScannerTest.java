package com.fenlan.compiler.main;

import com.fenlan.compiler.scanner.Scanner;
import com.fenlan.compiler.token.Token;
import com.fenlan.compiler.token.TokenTable;
import com.fenlan.compiler.token.TokenType;

public class ScannerTest {
    public static void main(String[] args) {
        Token token;
        if(!Scanner.InitScanner("src/com/fenlan/compiler/program/program1"))
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
