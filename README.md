# <p align='center'>简易解释器</p>

## 简单介绍
程序为实现一个简单的函数绘图语言的解释器,该解释器接受用绘图语言编写的源程序,经过语法和语义分析之后,直接将源程序所规定的图形显示在显示屏(或显示窗口)中。

## 实现方法
用java语言和递归下降子程序的方法编写完整的解释器，运行函数绘图语言的解释器，以该语言书写的源程序为输入，就会从屏幕上看到源程序所规定的图形。异于c实现，java实现没有构建语法树，在进行语法分析时同时进行了语义分析，其中for语法通过反复读取DRAW函数的参数，反复计算，将所有的点记录下来，最后一次遍历所有的点绘出图形。

## 运行环境
操作系统: Ubuntu 16.04<br>
代码编译器: IntelliJ IDEA-2017.2<br>
JDK版本: JDK-1.8<br>
已实现代码功能: 词法分析、词法分析测试、语法语义分析，编译绘图

## 绘图源程序示例

### 源程序一:
``` bash
ORIGIN IS(360, 240);
ROT IS PI/2;
SCALE IS (100,100/3);
FOR T FROM 0 TO 2*PI STEP PI/50 DRAW (COS(T), SIN(T));
```
### 绘图结果:
<images src="https://github.com/fenlan/Mycode/blob/master/images/program1.png" align=center>
</images>

### 源程序二:
``` bash
ROT IS 0;
ORIGIN IS (50,400);
SCALE IS (2,1);
FOR T FROM 0 TO 300 STEP 1 DRAW(0,-T);
FOR T FROM 0 TO 300 STEP 2 DRAW(T,0);
FOR T FROM 0 TO 300 STEP 1 DRAW(T,-T);
SCALE IS (2,0.1);
FOR T FROM 0 TO 55 STEP 1 DRAW(T,-(T*T));
SCALE IS (10,5);
FOR T FROM 0 TO 60 STEP 1 DRAW(T,-SQRT(T));
SCALE IS (20,0.1);
FOR T FROM 0 TO 8 STEP 0.1 DRAW(T,-EXP(T));
```
### 绘图结果
<images src="https://github.com/fenlan/Mycode/blob/master/images/program2.png" align=center>
</images>

## c实现源码地址
[https://github.com/fenlan/Compiler](https://github.com/fenlan/Compiler)