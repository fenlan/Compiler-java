package com.fenlan.compiler.main;

import com.fenlan.compiler.parser.Parser;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static Graphics graphics;
    public static void main(String[] args) {

//        if(args.length < 2)
//        {
//            System.out.println("please input Source File !");		return ;
//        }

        JFrame frame=new Main();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1080, 1080);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        graphics = g;
        Parser.Parser("/home/fenlan/code/cpp/compiler/program");
    }

}
