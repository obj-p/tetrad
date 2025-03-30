parser grammar TetradParser;

options {
  tokenVocab = TetradLexer;
}

document
    : interface_declaration+ EOF
    ;

interface_declaration
    : INTERFACE name interface_body
    ;

interface_body
    : LCURLY interface_members RCURLY
    ;

interface_members
    : member*
    ;

member
    : type_name QUESTION? type_annotation
    ;

type_name
    : name
    ;

type_annotation
    : COLON type_identifier
    ;

type_identifier
    : name
    ;

name
    : NAME
    ;
