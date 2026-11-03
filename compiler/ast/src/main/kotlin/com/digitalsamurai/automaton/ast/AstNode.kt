package com.digitalsamurai.automaton.ast

import com.digitalsamurai.automaton.grammar.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import kotlin.time.Duration

sealed class Ast {

    // TODO перейти на полное типизированное дерево и отказаться от хранения символа грамматики внутри
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

        data class AstHttpRequestName(override val value: String) : Leaf<String>() {
            override val symbol: GrammarSymbol<*> = Request.Name
        }

        data class AstHttpRequestUrl(override val value: URL): Leaf<URL>() {
            override val symbol: GrammarSymbol<*> = Request.Url
        }

        data class AstHttpRequestType(override val value: Type) : Leaf<AstHttpRequestType.Type>() {
            override val symbol: GrammarSymbol<*> = HttpRequest.RequestType

            enum class Type {
                GET, POST, PUT, DELETE
            }
        }

        data class AstHttpRequestJsonBody(override val value: AstHttpRequestJsonBody.Type): Leaf<AstHttpRequestJsonBody.Type>() {
            override val symbol: GrammarSymbol<*> = HttpRequest.Body.Json

            sealed interface Type {
                class Array(val jsonArray: JSONArray) : JSONArray(jsonArray), Type
                // JSONObject has no copy constructor: JSONObject(Object) is a bean constructor and loses all keys
                class Object(val jsonObject: JSONObject) : JSONObject(jsonObject.toMap()), Type
            }
        }

        data class AstHttpRequestPlainBody(override val value: String): Leaf<String>() {
            override val symbol: GrammarSymbol<*> = HttpRequest.Body.Plain
        }

        data class AstStepDescription(override val value: String): Leaf<String>() {
            override val symbol: GrammarSymbol<*> = Step.Description
        }

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

        data class AstElementProperty(override val value: Type) : Leaf<AstElementProperty.Type>() {
            override val symbol = ElementProperty

            enum class Type(val raw: String) {
                VISIBLE("visible"),
            }
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