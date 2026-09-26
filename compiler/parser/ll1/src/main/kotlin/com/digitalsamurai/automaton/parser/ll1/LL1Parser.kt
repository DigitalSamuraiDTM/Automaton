package com.digitalsamurai.automaton.parser.ll1

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.grammar.*
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import java.util.concurrent.atomic.AtomicBoolean

// TODO почитать, что это не совсем LL(1)
public object LL1Parser : AutomatonParser {
    private val leafParser = AstLeafParser()

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

    override fun parse(tokens: List<Token<*>>): Ast {
        val listIterator = tokens.listIterator()
        val firstToken = listIterator.next()
        val ast = recursion(
            currentToken = firstToken,
            lastTokens = listIterator,
        )
        return ast
    }

    private fun recursion(currentToken: Token<*>, lastTokens: ListIterator<Token<*>>): Ast {
        if (currentToken.symbol is Terminal<*>) {
            return parseAstLeaf(currentToken)
        }
        if (currentToken.symbol is NonTerminal) {
            return parseNonTerminal(
                currentToken = currentToken,
                lastTokens = lastTokens,
            )
        }
        error("Unexpected symbol format")
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
                    inlinedProductions.forEach { inlinedProduction ->
                        firstSymbolTable[inlinedProduction.first()] = inlinedProduction
                    }
                }
            }
            table[symbol] = firstSymbolTable
        }
        parsingTable = table
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

    private fun parseNonTerminal(currentToken: Token<*>, lastTokens: ListIterator<Token<*>>): Ast {
        val currentSymbol = currentToken.symbol as NonTerminal
        val nextToken = if (lastTokens.hasNext()) {
            lastTokens.next()
        } else {
            if (currentSymbol.hasEpsilon()) {
                return Ast.Node(
                    childs = emptyList(),
                    symbol = currentSymbol,
                )
            } else {
                error("Unexpected finish parsing tokens list")
            }
        }

        val production = parsingTable!![currentSymbol]?.get(nextToken.symbol)
        // найдена продукция, строим
        if (production != null) {
            val childs = mutableListOf<Ast>()
            production.forEachIndexed { index, symbol ->
                if (index == 0) {
                    childs.add(recursion(currentToken = nextToken, lastTokens = lastTokens))
                    return@forEachIndexed
                }
                // встретили рекурсию (сами себя). Идем вниз по рекурсии
                if (currentSymbol == symbol) {
                    val recursed = recursion(currentToken = currentToken, lastTokens = lastTokens)
                    childs.addAll((recursed as Ast.Node).childs)
                    return@forEachIndexed
                }
                val next = lastTokens.next()

                if (symbol == next.symbol) {
                    childs.add(recursion(currentToken = next, lastTokens = lastTokens))
                } else if ((symbol as? NonTerminal)?.hasEpsilon() == true) {
                    // текущий символ в продукции имеет эпсилон и его можно свернуть в "ничего"
                    // при этом нам надо откатиться назад, чтобы не пропустить читаемый символ
                    lastTokens.previous()
                    // TODO log свернулись в эпсилон
                } else {
                    error("Unknown symbol: ${next.symbol} at production: $production. Required: ${symbol}")
                }
            }

            return Ast.Node(
                symbol = currentSymbol,
                childs = childs.toList(),
            )
        } else {
            // если продукция для следующего символа не найдена, но есть эпсилон переход, то возвращаемся по эпсилону
            if (currentSymbol.hasEpsilon()) {
                // откатываемся назад, чтобы повторно считать символ и построить продукцию по нему
                // TODO: сейчас сделано костылем, что при вхождении в рекурсию мы получаем AstNode из которого читаем childrens
                lastTokens.previous()
                return Ast.Node(
                    symbol = currentSymbol,
                    childs = emptyList(),
                )
            }
            error("Production at token '${currentSymbol}' with next symbol '${nextToken.symbol}' not found")
        }
    }

    private fun parseAstLeaf(currentToken: Token<*>): Ast {
        // создаем листья типизированные для упрощения дальнейшего семантического анализа
        return leafParser.parse(currentToken)
    }

    override fun toString(): String {
        var output = ""
        parsingTable?.forEach { (key, values) ->
            output += "[$key]\n"
            var index = 0
            for (entry in values) {
                val firstSymbol = entry.key
                val productions = entry.value
                output += (if (index == values.size - 1) "  └── " else "  ├── ")
                index++
                output += ("[$firstSymbol]:[${productions.joinToString(" ")}]\n")
            }
        } ?: error("Parser not initialized")
        return output
    }

}