package com.fenlan.compiler.main;

import com.fenlan.compiler.parser.Parser;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    public static Graphics graphics;

    public Main() {
        setTitle("函数绘图语言运行结果");
        setSize(720, 480);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
        Main main = new Main();
        Parser.ParserAn("src/com/fenlan/compiler/program/program1");
    }

    @Override
    public void paint(Graphics g) {
        graphics = g;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (double[] tuple : Parser.list) {
            graphics.drawOval((int)tuple[0], (int)tuple[1], 2, 2);
            System.out.println("(" + tuple[0] + "," + tuple[1] + ")");
        }
    }

}
