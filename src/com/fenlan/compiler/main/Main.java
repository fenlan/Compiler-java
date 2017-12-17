package com.fenlan.compiler.main;

import com.fenlan.compiler.parser.Parser;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static Graphics graphics;

    public Main() {
        setTitle("函数绘图语言运行结果");
        setSize(1080, 1080);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {

//        if(args.length < 2)
//        {
//            System.out.println("please input Source File !");		return ;
//        }
        Main main = new Main();
    }

    @Override
    public void paint(Graphics g) {
        graphics = g;
        System.out.println("执行到这里");
        Parser.ParserAn("/home/fenlan/code/cpp/compiler/program");
    }

}
