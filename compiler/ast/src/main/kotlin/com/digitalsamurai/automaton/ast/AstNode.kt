package com.digitalsamurai.automaton.ast

import com.digitalsamurai.automaton.grammar.*
import kotlin.time.Duration

sealed class Ast {

    abstract val symbol: GrammarSymbol<*>
    protected fun toStringTree(prefix: String): String {
        when (this) {
            is Leaf<*> -> return "${symbol.representation}($value)\n"
            is Node -> {
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
    }


    data class Node(
        override val symbol: GrammarSymbol<*>,
        val childs: List<Ast>,
    ) : Ast() {
        override fun toString(): String {
            return toStringTree("")
        }
    }


    sealed class Leaf<T> : Ast() {
        abstract val value: T

        data class AstInputData(override val value: String) : Leaf<String>() {
            override val symbol = InputData
        }

        data class AstTapType(override val value: Type) : Leaf<AstTapType.Type>() {
            override val symbol = TapType

            enum class Type {
                SINGLE,
                DOUBLE,
                LONG,
            }
        }

        data class AstTimeout(override val value: Duration) : Leaf<Duration>() {
            override val symbol = Timeout
        }

        data class AstDuration(override val value: Duration) : Leaf<Duration>() {
            override val symbol = com.digitalsamurai.automaton.grammar.Duration
        }

        data class AstElementProperty(override val value: String) : Leaf<String>() {
            override val symbol = ElementProperty
        }

        data class AstPropertyValue(override val value: String) : Leaf<String>() {
            override val symbol = PropertyValue
        }

        data class AstId(override val value: String) : Leaf<String>() {
            override val symbol = Id
        }

        data class AstTestName(override val value: String) : Leaf<String>() {
            override val symbol = TestName
        }

        data class AstThemeMode(override val value: Mode) : Leaf<AstThemeMode.Mode>() {
            override val symbol = ThemeMode

            enum class Mode {
                LIGHT, DARK, SYSTEM
            }
        }

        data class AstCameraLeaf(override val value: Boolean) : Leaf<Boolean>() {
            override val symbol = Camera
        }

        data class AstMicrophoneLeaf(override val value: Boolean) : Leaf<Boolean>() {
            override val symbol = Microphone
        }

        data class AstElementNameLeaf(override val value: String) : Leaf<String>() {
            override val symbol = Element.Name
        }

        data class AstElementLayoutLeaf(override val value: String) : Leaf<String>() {
            override val symbol = Element.Layout
        }
    }
}