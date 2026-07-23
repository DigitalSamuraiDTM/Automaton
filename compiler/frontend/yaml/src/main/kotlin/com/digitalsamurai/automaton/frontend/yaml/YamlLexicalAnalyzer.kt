package com.digitalsamurai.automaton.frontend.yaml

import com.digitalsamurai.automaton.frontend.api.AutomatonLexicalAnalyzer
import com.digitalsamurai.automaton.grammar.GrammarSymbol
import com.digitalsamurai.automaton.grammar.Token

public class YamlLexicalAnalyzer: AutomatonLexicalAnalyzer<Map<String, Any>> {

    override fun analyze(input: Map<String, Any>): Sequence<Token<*>> {
        println("INPUT: ${input}")
        println(GrammarSymbol.all)
        return emptySequence()
    }
}