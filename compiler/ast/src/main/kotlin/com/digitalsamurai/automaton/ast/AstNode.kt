package com.digitalsamurai.automaton.ast

import com.digitalsamurai.automaton.grammar.GrammarSymbol

data class AstNode<T>(
    val childs: List<AstNode<*>>,
    val value: T,
    val symbol: GrammarSymbol<T>,
) {
    override fun toString(): String {
        return toStringTree("")
    }
    private fun toStringTree(prefix: String): String {
        if (childs.isEmpty()) {
            return "${symbol.representation}($value)\n"
        }
        var out = ""
        out += symbol.representation + "\n"
        childs.forEachIndexed { i, child ->
            out += if (i == childs.lastIndex) {
                "${prefix}└── ${child.toStringTree("$prefix    ")}"
            } else {
                "${prefix}├── ${child.toStringTree("$prefix│   ")}"
            }
        }
        return out
    }
}