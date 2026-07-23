package com.digitalsamurai.automaton.grammar

import kotlin.reflect.KClass
import kotlin.reflect.typeOf

public interface GrammarSymbol<T> {
    val representation: String


    companion object {
        val all: Map<String, GrammarSymbol<*>> by lazy {
            val nonTerminals = collectSealedSubClasses(NonTerminal::class)
                .mapNotNull { it.objectInstance as? GrammarSymbol<*> }
                .associateBy { it.representation }

            val terminals = collectSealedSubClasses(Terminal::class)
                .mapNotNull { it.objectInstance as? GrammarSymbol<*> }
                .associateBy { it.representation }
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