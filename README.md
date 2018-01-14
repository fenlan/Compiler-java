# <p align='center'>简易解释器</p>

![build status](https://travis-ci.org/fenlan/Compiler-java.svg?branch=master)

## 简单介绍
程序为实现一个简单的函数绘图语言的解释器,该解释器接受用绘图语言编写的源程序,经过语法和语义分析之后,直接将源程序所规定的图形显示在显示屏(或显示窗口)中。

## 实现方法
用java语言和递归下降子程序的方法编写完整的解释器，运行函数绘图语言的解释器，以该语言书写的源程序为输入，就会从屏幕上看到源程序所规定的图形。异于c实现，java实现没有构建语法树，在进行语法分析时同时进行了语义分析，其中for语法通过反复读取DRAW函数的参数，反复计算，将所有的点记录下来，最后一次遍历所有的点绘出图形。

## 运行环境
- Ubuntu 16.04
- IntelliJ IDEA-2017.2
- JDK-1.8


- 已实现代码功能: 词法分析、词法分析测试、语法语义分析，编译绘图

## 绘图源程序示例

### 源程序一:
``` bash
ORIGIN IS(360, 240);		// 横、纵坐标平移距离
ROT IS PI/2;				// 旋转角度
SCALE IS (100,100/3);		// 横、纵坐标比例因子
FOR T FROM 0 TO 2*PI STEP PI/50 DRAW (COS(T), SIN(T));
```
### 绘图结果:
<p align="center">
<img src="https://github.com/fenlan/Mycode/blob/master/images/program1.png" width="480" height="320"/>
</p>

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
<p align="center">
<img src="https://github.com/fenlan/Mycode/blob/master/images/program2.png" width="480" height="320"/>
</p>

## 词法分析

### 记号
``` java
private int   type;       // 记号类型
private String      lexeme;     // 构成记号字符串
private double      value;      // 记号为常数时,记号的值
```

### 记号类型
``` java
public class TokenType {

    public static final int ORIGIN = 0;		// 保留字
    public static final int SCALE = 1;
    public static final int ROT = 2;
    public static final int IS = 3;
    public static final int TO = 4;
    public static final int STEP = 5;
    public static final int DRAW = 6;
    public static final int FOR = 7;
    public static final int FROM = 8;		// 保留字
    public static final int T = 9;			// 参数
    public static final int SEMICO = 10;
    public static final int L_BRACKET = 11;
    public static final int R_BRACKET = 12;
    public static final int COMMA = 13;		// 分隔符
    public static final int PLUS = 14;		// 运算符
    public static final int MINUS = 15;
    public static final int MUL = 16;
    public static final int DIV = 17;
    public static final int POWER = 18;		// 运算符
    public static final int FUNC = 19;
    public static final int CONST_ID = 20;
    public static final int NONTOKEN = 21;	// 空记号
    public static final int ERRTOKEN = 22;	// 错误记号
}
```
程序中的保留字、参数、分隔符、运算符等采用的是"一字一码"，即一个记号就是一个实例;函数和常数可以有多个实例，FUNC可以是sin，也可以是cos等;空记号标记源程序已经结束;出错记号用于标记非法输入,当分析到非法字符或字符串时，词法分析器就返回一个错误记号，以便语法分析器的错误处理。

### 描述词法的正规式
<p align="center">
<img src="https://github.com/fenlan/Mycode/blob/master/images/tokentable.png" width="480" height="320"/>
</p>

### 词法分析正规式DFA
<p align="center">
<img src="https://github.com/fenlan/Mycode/blob/master/images/DFA.png" width="480" height="320"/>
</p>

根据正规式DFA编写出`GetToken()`方法，每次识别一个记号，返回值为一个记号类型。大体编程框架:
``` java
AddCharTokenString(_char);
if (Character.isAlphabetic(_char)) { ... ... }		// 字符记号
else if (Character.isDigit(_char)) { ... ... }		// 数字常数记号
else {				// 操作符号
	switch (_char) {
    	case ';': token.setType(TokenType.SEMICO); token.setLexeme(";"); break;
        ... ...
    }
}
```

### 词法分析流程
<p align="center">
<img src="https://github.com/fenlan/Mycode/blob/master/images/scanner.png" width="480" height="320"/>
</p>

词法分析器提供记号给语法分析器分析: InitScanner初始化词法分析器并打开源程序文件，如果文件打不开则指出错误告知语法分析器停止分析;GetToken返回一个记号;CloseScanner关闭词法分析器。初始化完后不断调用GetToken识别记号，当识别当NONTOKEN时调用CloseScanner来关闭词法分析器。

## 语法语义分析
在我的c实现的解释器中，语法语义分析分开进行，在java代码中，将语法语义分析同时进行，在进行语法分析时将表达式的结果求解出来，不再构建语法树，这种方法适合于比较简单的语法分析，较复杂的语法，应尽量构建语法树，构建语法树有利于语言本身扩展语法。

### 文法
``` bash
Program --> { Statement SEMICO }

Statement --> OriginStatement | ScaleStatement | RotStatement | ForStatement
OriginStatement --> ORIGINIS L_BRACKET Expression COMMA Expression R_BRACKET
ScaleStatement --> SCALE IS L_BRACKET Expression COMMA Expression R_BRACKET
RotStatement --> ROT IS Expression
ForStatement
	--> FOR T FROM Expression TO Expression STEP Expression
    	DRAW L_BRACKET Expression COMMA Expression R_BRACKET

Expression --> Term {(PLUS|MINUS) Term}
Term --> Factor {(MUL|DIV) Factor}
Factor --> PLUS Factor | MINUS Factor | Component
Component --> Atom POWER Component | Atom
Atom --> CONST_ID
		| T
        | FUNC L_BRACKET Expression R_BRACKET
        | L_BRACKET Expression R_BRACKET
```

### 主要的递归下降子程序
``` java
public static void Parser(String FileName) { ... ... }
private static void Program() { ... ... }
private static void Statement() { ... ... }
private static void OriginStatement() { ... ... }
private static void RotStatement() { ... ... }
private static void ScaleStatement() { ... ... }
private static void ForStatement() { ... ... }

private static double Expression() { ... ... }
private static double Term() { ... ... }
private static double Factor() { ... ... }
private static double Component() { ... ... }
private static double Atom() { ... ... }
```

### 全程变量
``` java
private static double	Parameter=0,
                            Origin_x=0,Origin_y=0,
                            Scale_x=1,	Scale_y=1,
                            Rot_angle=0;
```

### For语句处理
c处理`ForStatement`是构建语法树，处理For的每次循环，只需要查询语法树，给变量T赋值，计算坐标。java实现没有构建语法树，本程序采用的方法是如下：
1. 词法分析`ForStatement`到`DRAW`子句，从`(`开始缓冲之后的记号，直到`)`(包括`)`)。
2. 计算`DRAW`子句的坐标，放入坐标点集合中。
2. 判断是否需要下一次循环，如果需要循环，将缓冲内容退回到源文件读取流中，计算变量`T`的下一个值。重复步骤1、2。
3. 如果不需要循环，将缓冲区清空，进行下一个`Statement`分析。

### 缺陷
1. 语法分析没有扩展性
2. 循环读取字符流增加了程序耗时
3. 程序不够模块化，程序思路不清晰


## c实现源码地址
[https://github.com/fenlan/Compiler](https://github.com/fenlan/Compiler)