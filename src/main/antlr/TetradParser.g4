parser grammar TetradParser;

options {
  tokenVocab = TetradLexer;
}

init  : '{' value (',' value)* '}' ;

value : init
      | INT
      ;