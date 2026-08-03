package com.digitalsamurai.automaton.parser.ll1

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.grammar.AutomatonGrammar
import com.digitalsamurai.automaton.grammar.GrammarSymbol
import com.digitalsamurai.automaton.grammar.NonTerminal
import com.digitalsamurai.automaton.grammar.Token
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import java.util.concurrent.atomic.AtomicBoolean

// TODO почитать, что это не совсем LL(1)
public object LL1Parser : AutomatonParser {

    private val _isInitialized: AtomicBoolean = AtomicBoolean(false)

    override val isInitialized: Boolean
        get() = _isInitialized.get()

    private var parsingTable: Map<NonTerminal, Map<GrammarSymbol<*>, List<GrammarSymbol<*>>>>? = null

    override fun initialize() {
        if (_isInitialized.compareAndSet(false, true)) {
            analyzeProductions()
        } else {
            // nothing to do
        }
    }

    override fun parse(tokens: Sequence<Token<*>>): AstNode {
        TODO()
    }

    private fun analyzeProductions() {
        val table: MutableMap<NonTerminal, Map<GrammarSymbol<*>, List<GrammarSymbol<*>>>> = mutableMapOf()
        val grammar = AutomatonGrammar.nonTerminals.values
        grammar.forEach { symbol ->
            if (symbol.isService) {
                // skip all service non terminals. They will inlined recursively at parsing table
                return@forEach
            }
            val firstSymbolTable = mutableMapOf<GrammarSymbol<*>, List<GrammarSymbol<*>>>()
            symbol.productions.forEach { production ->
                if (production.isEmpty()) {
                    // epsilon check make at parser
                } else {
                    // production
                    val inlinedProductions = getInlinedProductions(production)
                    inlinedProductions.forEach {  inlinedProduction ->
                        firstSymbolTable[inlinedProduction.first()] = inlinedProduction
                    }
                }
            }
            table[symbol] = firstSymbolTable
        }
        parsingTable = table
        // todo тут распарсить нетерминалы, вычислить first, для эпсилон продукции follow
    }

    // only for non terminals find their productions and inline it
    private fun getInlinedProductions(production: List<GrammarSymbol<*>>): List<List<GrammarSymbol<*>>> {
        var outProductions = mutableListOf<MutableList<GrammarSymbol<*>>>()
        production.forEach { symbol ->
            // inline
            if (symbol is NonTerminal && symbol.isService) {
                val newOutProductions = mutableListOf<MutableList<GrammarSymbol<*>>>()
                symbol.productions.forEach { productionOfServiceNonTerminal ->
                    val serviceNonTerminalProductions = getInlinedProductions(productionOfServiceNonTerminal)
                    serviceNonTerminalProductions.forEach { servicedProduction ->
                        if (outProductions.isEmpty()) {
                            newOutProductions.add(servicedProduction.toMutableList())
                        } else {
                            outProductions.forEach { outProduction ->
                                newOutProductions.add((outProduction + servicedProduction).toMutableList())
                            }
                        }
                    }
                }
                outProductions = newOutProductions
            } else {
                if (outProductions.isEmpty()) {
                    outProductions.add(mutableListOf(symbol))
                } else {
                    outProductions.forEach { outProduction ->
                        outProduction.add(symbol)
                    }
                }
            }
        }
        return outProductions
    }

    override fun toString(): String {
        var output = ""
        parsingTable?.forEach { (key, values) ->
            output+="[$key]\n"
            var index = 0
            for (entry in values) {
                val firstSymbol = entry.key
                val productions = entry.value
                output +=(if (index == values.size - 1) "  └── " else "  ├── ")
                index++
                output+=("[$firstSymbol]:[${productions.joinToString(" ")}]\n")
            }
        } ?: error("Parser not initialized")
        return output
    }

}