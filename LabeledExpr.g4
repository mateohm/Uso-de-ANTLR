grammar LabeledExpr;

prog: stat+ ;

stat
    : expr NEWLINE            # printExpr
    | ID '=' expr NEWLINE     # assign
    | DEG NEWLINE             # setDeg
    | RAD NEWLINE             # setRad
    | NEWLINE                 # blank
    ;

expr
    : expr op=('*'|'/') expr  # MulDiv
    | expr op=('+'|'-') expr  # AddSub
    | SUB expr                # Negate
    | expr FACT               # Factorial
    | func '(' expr ')'       # Func
    | NUMBER                  # number
    | ID                      # id
    | '(' expr ')'            # parens
    ;

func
    : SIN
    | COS
    | TAN
    | SQRT
    | LN
    | LOG
    ;

/* Tokens para operadores */
MUL  : '*' ;
DIV  : '/' ;
ADD  : '+' ;
SUB  : '-' ;
FACT : '!' ;

/* Palabras clave y funciones */
DEG  : [Dd][Ee][Gg] ;
RAD  : [Rr][Aa][Dd] ;
SIN  : [Ss][Ii][Nn] ;
COS  : [Cc][Oo][Ss] ;
TAN  : [Tt][Aa][Nn] ;
SQRT : [Ss][Qq][Rr][Tt] ;
LN   : [Ll][Nn] ;
LOG  : [Ll][Oo][Gg] ;

/* Números: enteros */
NUMBER : [0-9]+ ('.' [0-9]+)? ;

/* Ids y utilidades */
ID      : [a-zA-Z_][a-zA-Z_0-9]* ;
LPAREN  : '(' ;
RPAREN  : ')' ;
NEWLINE : [\r\n]+ ;
WS      : [ \t]+ -> skip ;
