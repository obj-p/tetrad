package org.tetrad

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun main(args: Array<String>) = Main().main(args)

class Main {
    fun main(args: Array<String>) {
        val input = CharStreams.fromStream(System.`in`)
        val lexer = TetradLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = TetradParser(tokens)
        parser.document()
    }
}