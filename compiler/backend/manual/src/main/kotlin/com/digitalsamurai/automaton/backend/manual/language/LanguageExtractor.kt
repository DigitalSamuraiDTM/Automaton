package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language

import com.digitalsamurai.automaton.ast.AstNode

interface LanguageExtractor {
    fun extractText(node: AstNode<*>): String
}
