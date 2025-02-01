parser grammar TetradParser;

options {
  tokenVocab = TetradLexer;
}

document
    : interfaceDefinition+ EOF
    ;

interfaceDefinition
    : INTERFACE name
    ;

name
    : NAME
    ;