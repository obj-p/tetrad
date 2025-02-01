package org.tetrad

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

fun main(args: Array<String>) = Main().main(args)

class Main {
    fun main(args: Array<String>) {
        val input = CharStreams.fromStream(System.`in`)
        val lexer = TetradLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = TetradParser(tokens)
        val tree = parser.init()
        val walker = ParseTreeWalker()

        walker.walk(ShortToUnicodeString(), tree)
    }

    class ShortToUnicodeString : TetradParserBaseListener() {
        override fun enterInit(ctx: TetradParser.InitContext) {
            print("\"")
        }

        override fun enterValue(ctx: TetradParser.ValueContext) {
            val value = ctx.INT().text.toInt()
            System.out.printf("\\u%04x", value)
        }

        override fun exitInit(ctx: TetradParser.InitContext) {
            println("\"")
        }
    }
}