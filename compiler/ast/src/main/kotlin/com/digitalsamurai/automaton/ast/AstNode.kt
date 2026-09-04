package com.digitalsamurai.automaton.ast

import com.digitalsamurai.automaton.grammar.NonTerminal
import com.digitalsamurai.automaton.grammar.Terminal

sealed interface AstNode {
    fun toStringTree(prefix: String = ""): String
}

data class NonTerminalNode(
    val symbol: NonTerminal,
    val children: List<AstNode>
) : AstNode {

    override fun toString(): String = toStringTree("")

    override fun toStringTree(prefix: String): String {
        var out = ""
        out += symbol.representation + "\n"
        children.forEachIndexed { i, node ->
            out += if (i == children.lastIndex) {
                "${prefix}└── ${children[i].toStringTree("$prefix    ")}"
            } else {
                "${prefix}├── ${children[i].toStringTree("$prefix│   ")}\n"
            }
        }
        return out
    }
}

data class TerminalNode<T>(
    val symbol: Terminal<T>,
    val value: T
) : AstNode {
    override fun toString(): String {
        return "$symbol($value)"
    }

    override fun toStringTree(prefix: String): String {
        return toString()
    }
}