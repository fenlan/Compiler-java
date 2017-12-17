package com.fenlan.compiler.main;

import com.fenlan.compiler.parser.Parser;

import javax.swing.*;
import java.awt.*;

public class ParserTest extends JFrame {

    public static Graphics graphics;
    public static void main(String[] args) {

//        if(args.length < 2)
//        {
//            System.out.println("please input Source File !");		return ;
//        }
        Parser.Parser("/home/fenlan/code/cpp/compiler/program");
    }
}
