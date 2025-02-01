lexer grammar TetradLexer;

LBRACE : '{';
RBRACE : '}';
COMMA  : ',';
INT    :   [0-9]+;
WS     :   [ \t\r\n]+ -> skip;
