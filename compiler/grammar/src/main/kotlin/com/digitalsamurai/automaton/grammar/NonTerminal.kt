package com.digitalsamurai.automaton.grammar

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URI
import java.net.URL
import java.net.URLStreamHandler

sealed interface NonTerminal : GrammarSymbol<Unit> {
    val isService: Boolean
    fun asToken(sourceLocation: String): Token<Unit> {
        return Token(
            symbol = this::class.objectInstance!!,
            value = Unit,
            sourceLocation = sourceLocation,
        )
    }

    val productions: List<Production>

    public fun hasEpsilon(): Boolean {
        return productions.any { it.isEmpty() }
    }
}

data object CompilationUnit : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "compilationUnit"
    override val productions: List<Production> = listOf(
        production(Context, Test)
    )
}

data object Context : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "context"
    override val productions: List<Production> = listOf(
        production(Elements, Requests, Theme, Permissions)
    )
}

data object Requests : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "requests"
    override val productions: List<Production> = listOf(
        production(Request, Requests),
        epsilon(),
    )
}

data object Request : NonTerminal {
    override val isService: Boolean = true
    override val representation: String = "request"
    override val productions: List<Production> = listOf(
        production(HttpRequest)
        // other request types
    )

    data object Name : Terminal<String> {
        override val representation: String = "requestName"

        override fun asToken(sourceLocation: String, value: Any): Token<String> {
            return Token(
                symbol = Name,
                value = value as String,
                sourceLocation = sourceLocation,
            )
        }
    }

    data object Url : Terminal<URL> {
        override val representation: String = "url"
        override fun asToken(sourceLocation: String, value: Any): Token<URL> {
            return Token(
                sourceLocation = sourceLocation,
                value = URI.create(value as String).toURL(),
                symbol = Url
            )
        }
    }
}

data object HttpRequest : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "http"
    override val productions: List<Production> = listOf(
        production(Request.Name, Request.Url, Headers, Body)
    )

    data object Headers : NonTerminal {
        override val isService: Boolean = false
        override val representation: String = "headers"
        override val productions: List<Production> = listOf(epsilon())
    }

    data object Body : NonTerminal {
        override val isService: Boolean = false
        override val representation: String = "body"
        override val productions: List<Production> = listOf(
            production(Plain),
            production(Json),
        )

        data object Json : Terminal<Json.Type> {
            override val representation: String = "json"
            override fun asToken(sourceLocation: String, value: Any): Token<Type> {
                val tokener = JSONTokener(value as String)
                val value = when(val jsonValue = tokener.nextValue()) {
                    is JSONObject -> Type.Object(value)
                    is JSONArray -> Type.Array(value)
                    else -> error("Unexpected value type ${jsonValue::class}")
                }
                return Token(
                    symbol = Json,
                    value = value,
                    sourceLocation = sourceLocation,
                )
            }
            sealed interface Type {
                class Array(private val jsonArray: String) : JSONArray(jsonArray), Type
                class Object(private val jsonObject: String) : JSONObject(jsonObject), Type
            }

        }

        data object Plain : Terminal<String> {
            override val representation: String = "plain"
            override fun asToken(sourceLocation: String, value: Any): Token<String> {
                return Token(
                    symbol = Plain,
                    value = value as String,
                    sourceLocation = sourceLocation,
                )
            }
        }
    }
}

data object Elements : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "elements"
    override val productions: List<Production> = listOf(
        production(Element, Elements),
        epsilon()
    )
}

data object Element : NonTerminal {

    override val isService: Boolean = false
    override fun asToken(sourceLocation: String): Token<Unit> {
        return Token(
            symbol = Element,
            value = Unit,
            sourceLocation = sourceLocation
        )
    }

    override val productions: List<Production> = listOf(
        production(Name, Layout),
    )
    override val representation: String = "element"


    data object Name : Terminal<String> {
        override val representation: String = "elementName"
        override fun asToken(sourceLocation: String, value: Any): Token<String> {
            return Token(
                symbol = Name,
                value = value as String,
                sourceLocation = sourceLocation,
            )
        }
    }

    // TODO layout будет хранить информацию о визуальном представлении элемента (ссылка на фигму или что-то иное)
    data object Layout : Terminal<String> {
        override val representation: String = "elementLayout"
        override fun asToken(sourceLocation: String, value: Any): Token<String> {
            return Token(
                symbol = Layout,
                value = value as String,
                sourceLocation = sourceLocation,
            )
        }
    }
}

data object Theme : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "theme"
    override val productions: List<Production> = listOf(
        production(ThemeMode),
        epsilon()
    )
}

data object Permissions : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "permissions"
    override val productions: List<Production> = listOf(
        production(Permission, Permissions),
        epsilon(),
    )
}

data object Permission : NonTerminal {
    override val isService: Boolean = true
    override val representation: String = "permission"
    override val productions: List<Production> = listOf(
        production(Microphone),
        production(Camera),
    )
}

data object Test : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "test"
    override val productions: List<Production> = listOf(
        production(Metadata, Steps)
    )
}

data object Metadata : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "metadata"
    override val productions: List<Production> = listOf(
        production(Id, TestName)
    )
}

data object Steps : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "steps"
    override val productions: List<Production> = listOf(
        production(Step, Steps),
        epsilon(),
    )
}

data object Step : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "step"
    override val productions: List<Production> = listOf(
        production(Description, Requests, Actions)
    )

    data object Description : Terminal<String> {
        override val representation: String = "stepDescription"
        override fun asToken(sourceLocation: String, value: Any): Token<String> {
            return Token(
                symbol = Description,
                value = value as String,
                sourceLocation = sourceLocation,
            )
        }
    }
    data object Requests : NonTerminal {
        override val isService: Boolean = false
        override val representation: String = "stepRequests"
        override val productions: List<Production> = listOf(
            production(Request.Name, Requests),
            epsilon(),
        )
    }
}

data object Actions : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "actions"
    override val productions: List<Production> = listOf(
        production(Action, Actions),
        epsilon(),
    )
}

data object Action : NonTerminal {
    override val isService: Boolean = true
    override val representation: String = "action"
    override val productions: List<Production> = listOf(
        production(Tap),
        production(Input),
        production(Wait),
        production(Assert),
    )
}

data object Tap : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "tap"
    override val productions: List<Production> = listOf(
        production(Element.Name, TapType)
    )
}

data object Input : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "input"
    override val productions: List<Production> = listOf(
        production(Element.Name, InputData),
    )
}

data object Wait : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "wait"
    override val productions: List<Production> = listOf(
        production(Condition, Timeout)
    )
}

data object Assert : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "assert"
    override val productions: List<Production> = listOf(
        production(Condition)
    )
}

data object Condition : NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "condition"
    override val productions: List<Production> = listOf(
        production(Element.Name, ElementProperty, PropertyValue)
    )
}
// TODO не до конца понятно, является ли Delay в действительности действием или это явный костыль "ожидания" непонятно зачем
//data object Delay: NonTerminal {
//    override val isService: Boolean = false
//    override val representation: String = "delay"
//    override val productions: List<Production> = listOf(
//        listOf(Duration)
//    )
//}
