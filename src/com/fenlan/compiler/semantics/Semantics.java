package com.fenlan.compiler.semantics;

import com.fenlan.compiler.main.Main;
import com.fenlan.compiler.parser.Parser;
import java.awt.*;

public class Semantics {

    private static void DrawPixel(Graphics g, int x, int y) {
        g.drawOval(x, y, 2, 2);
    }
}
