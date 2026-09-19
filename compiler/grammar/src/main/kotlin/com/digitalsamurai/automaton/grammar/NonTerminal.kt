package com.digitalsamurai.automaton.grammar

sealed interface NonTerminal: GrammarSymbol<Unit> {
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

data object CompilationUnit: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "compilationUnit"
    override val productions: List<Production> = listOf(
        production(Context,Test)
    )
}

data object Context: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "context"
    override val productions: List<Production> = listOf(
        production(Elements, Theme, Permissions)
    )
}

data object Elements: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "elements"
    override val productions: List<Production> = listOf(
        production(Element, Elements),
        epsilon()
    )
}

data object Element: NonTerminal {

    override val isService: Boolean = false
    override fun asToken(sourceLocation: String): Token<Unit> {
        return Token(
            symbol = Element,
            value = Unit,
            sourceLocation = sourceLocation
        )
    }


    data object Name: Terminal<String> {

    }
}

data object Theme: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "theme"
    override val productions: List<Production> = listOf(
        production(ThemeMode),
        epsilon()
    )
}

data object Permissions: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "permissions"
    override val productions: List<Production> = listOf(
        production(Permission, Permissions),
        epsilon(),
    )
}

data object Permission: NonTerminal {
    override val isService: Boolean = true
    override val representation: String = "permission"
    override val productions: List<Production> = listOf(
        production(Microphone),
        production(Camera),
    )
}

data object Test: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "test"
    override val productions: List<Production> = listOf(
        production(Metadata, Steps)
    )
}

data object Metadata: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "metadata"
    override val productions: List<Production> = listOf(
        production(Id, TestName)
    )
}

data object Steps: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "steps"
    override val productions: List<Production> = listOf(
        production(Step, Steps),
        epsilon(),
    )
}

data object Step: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "step"
    override val productions: List<Production> = listOf(
        production(Actions)
    )
}

data object Actions: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "actions"
    override val productions: List<Production> = listOf(
        production(Action, Actions),
        epsilon(),
    )
}

data object Action: NonTerminal {
    override val isService: Boolean = true
    override val representation: String = "action"
    override val productions: List<Production> = listOf(
        production(Tap),
        production(Input),
        production(Wait),
        production(Delay),
        production(Assert),
    )
}

data object Tap: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "tap"
    override val productions: List<Production> = listOf(
        production(Element, TapType)
    )
}

data object Input: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "input"
    override val productions: List<Production> = listOf(
        production(Element, InputData),
    )
}

data object Wait: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "wait"
    override val productions: List<Production> = listOf(
        production(Condition, Timeout)
    )
}

data object Assert: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "assert"
    override val productions: List<Production> = listOf(
        production(Condition)
    )
}

data object Condition: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "condition"
    override val productions: List<Production> = listOf(
        production(Element, ElementProperty, PropertyValue)
    )
}

data object Delay: NonTerminal {
    override val isService: Boolean = false
    override val representation: String = "delay"
    override val productions: List<Production> = listOf(
        listOf(Duration)
    )
}
