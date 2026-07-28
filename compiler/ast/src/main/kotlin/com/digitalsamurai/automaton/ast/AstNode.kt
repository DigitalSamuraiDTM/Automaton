package com.digitalsamurai.automaton.ast

import com.digitalsamurai.automaton.grammar.NonTerminal
import com.digitalsamurai.automaton.grammar.Terminal

sealed interface AstNode {
}

data class NonTerminalNode(
    val symbol: NonTerminal,
    val children: List<AstNode>
) : AstNode {

    // TODO надо доработать после получения дерева разбора
    override fun toString(): String {
        var out = ""
        out += symbol.representation + "\n"

        children.forEachIndexed { i, node ->
            out += if (i == children.lastIndex) {
                "└── ${children[i]}"
            } else {
                "├── ${children[i]}"
            }
        }
        return out
    }
}

data class TerminalNode<T>(
    val symbol: Terminal<T>,
    val value: T
) {
    override fun toString(): String {
        return "$symbol($value)"
    }
}