package com.digitalsamurai.automaton.grammar

import kotlin.time.Duration

sealed interface Terminal<T>: GrammarSymbol<T> {
    fun asToken(sourceLocation: String, value: Any) : Token<T>
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

data object ElementReference: Terminal<String> {
    override val representation: String = "element"

    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = ElementReference,
            value = value as String,
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

data object TestName: Terminal<String> {
    override val representation: String = "name"
    override fun asToken(sourceLocation: String, value: Any): Token<String> {
        return Token(
            symbol = TestName,
            value = value as String,
            sourceLocation = sourceLocation,
        )
    }
}

data object ThemeMode: Terminal<ThemeMode.Mode> {
    override val representation: String = "themeMode"
    override fun asToken(sourceLocation: String, value: Any): Token<Mode> {
        return Token(
            symbol = ThemeMode,
            value = Mode.entries.first { it.representation == value },
            sourceLocation = sourceLocation,
        )
    }
    enum class Mode(val representation: String) {
        LIGHT("light"), DARK("dark"), SYSTEM("system"),
    }
}

data object Microphone: Terminal<Boolean> {
    override val representation: String = "microphone"
    override fun asToken(sourceLocation: String, value: Any): Token<Boolean> {
        return Token(
            symbol = Microphone,
            value = value.toBoolean(),
            sourceLocation = sourceLocation,
        )
    }
}
data object Camera: Terminal<Boolean> {
    override val representation: String = "camera"
    override fun asToken(sourceLocation: String, value: Any): Token<Boolean> {
        return Token(
            symbol = Camera,
            value = value.toBoolean(),
            sourceLocation = sourceLocation,
        )
    }
}


private fun Any.toBoolean(): Boolean = when (this) {
    is Boolean -> this
    is String -> if (this == "true") true else if (this == "false") false else throw ClassCastException("$this not supported")
    is Int -> if (this == 1) true else if (this == 0) false else throw ClassCastException("$this not supported")
    is Double -> if (this == 1.0) true else if (this == 0.0) false else throw ClassCastException("$this not supported")
    else -> throw ClassCastException("$this not supported")
}