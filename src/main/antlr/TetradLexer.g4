lexer grammar TetradLexer;

COMPONENT  : 'component';
FUNCTION   : 'fun';
INTERFACE  : 'interface';
TYPE       : 'type';

COLON    : ':';
COMMA    : ',';
LCURLY   : '{';
QUESTION : '?';
RCURLY   : '}';

NAME
    : [_A-Za-z] [_0-9A-Za-z]*
    ;

INT    :   [0-9]+;

WS     :   [ \t\r\n]+ -> skip;
