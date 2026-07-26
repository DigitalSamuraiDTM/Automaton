package com.digitalsamurai.automaton.grammar

import kotlin.reflect.KClass
import kotlin.reflect.typeOf

public interface GrammarSymbol<T> {
    val representation: String

    companion object {
        val nonTerminals: Map<String, NonTerminal> by lazy {
            collectSealedSubClasses(NonTerminal::class)
                .mapNotNull { it.objectInstance as? NonTerminal }
                .associateBy { it.representation }
        }
        val terminals: Map<String, Terminal<*>> by lazy {
            collectSealedSubClasses(Terminal::class)
                .mapNotNull { it.objectInstance as? Terminal<*> }
                .associateBy { it.representation }
        }
        val all: Map<String, GrammarSymbol<*>> by lazy {
            buildMap {
                putAll(nonTerminals)
                putAll(terminals)
            }
        }

        private fun collectSealedSubClasses(
            type: KClass<*>,
        ): List<KClass<*>> = type.sealedSubclasses.flatMap { subclass ->
            if (subclass.isSealed) {
                collectSealedSubClasses(subclass)
            } else {
                listOf(subclass)
            }
        }
    }
}