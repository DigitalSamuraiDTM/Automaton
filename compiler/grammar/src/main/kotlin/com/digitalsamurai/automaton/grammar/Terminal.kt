package com.digitalsamurai.automaton.grammar

import kotlin.time.Duration

sealed interface Terminal<T>: GrammarSymbol<T> {
    fun asToken(sourceLocation: String, value: Any) : Token<T>
}

data object Element: Terminal<String> {
    override val representation: String = "element"

    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = Element,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}

data object InputData: Terminal<String> {
    override val representation: String = "inputData"
    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = InputData,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}

data object TapType: Terminal<TapType.Type> {

    override val representation: String = "tapType"

    override fun asToken(sourceLocation: String, value: Any): Token<Type> {
        return Token(
            symbol = TapType,
            value = Type.entries.first { it.raw == value },
            sourceLocation = sourceLocation,
        )
    }

    enum class Type(val raw: String) {
        SINGLE("single"),
    }
}

data object Timeout: Terminal<Duration> {
    override val representation: String = "timeout"
    override fun asToken(sourceLocation: String, value: Any): Token<Duration> {
        return Token(
            symbol = Timeout,
            value = Duration.parse(value as String),
            sourceLocation = sourceLocation,
        )
    }
}

data object Duration: Terminal<Duration> {
    override val representation: String = "duration"
    override fun asToken(sourceLocation: String, value: Any): Token<Duration> {
        return Token(
            symbol = Timeout,
            value = Duration.parse(value as String),
            sourceLocation = sourceLocation,
        )
    }
}

data object ElementProperty: Terminal<String> {
    override val representation: String = "elementProperty"
    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = ElementProperty,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}

data object PropertyValue: Terminal<String> {
    override val representation: String = "propertyValue"
    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = PropertyValue,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}

data object Id: Terminal<String> {
    override val representation: String = "id"
    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = Id,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}

data object Name: Terminal<String> {
    override val representation: String = "name"
    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = Name,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}