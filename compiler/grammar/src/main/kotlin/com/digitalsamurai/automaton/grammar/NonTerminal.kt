package com.digitalsamurai.automaton.grammar

sealed interface NonTerminal: GrammarSymbol<Unit> {
    fun asToken(sourceLocation: String): Token<Unit> {
        return Token(
            symbol = this::class.objectInstance!!,
            value = Unit,
            sourceLocation = sourceLocation,
        )
    }

    val productions: List<Production>
}

data object CompilationUnit: NonTerminal {
    override val representation: String = "compilationUnit"
    override val productions: List<Production> = listOf(
        production(Context::class, Test::class)
    )
}

data object Context: NonTerminal {
    override val representation: String = "context"
    override val productions: List<Production> = listOf(
        // need to do
    )
}

data object Test: NonTerminal {
    override val representation: String = "test"
    override val productions: List<Production> = listOf(
        production(Metadata::class, Steps::class)
    )
}

data object Metadata: NonTerminal {
    override val representation: String = "metadata"
    override val productions: List<Production> = listOf(
        production(Id::class, Name::class)
    )
}

data object Steps: NonTerminal {
    override val representation: String = "steps"
    override val productions: List<Production> = listOf(
        production(Step::class, Steps::class),
        epsilon(),
    )
}

data object Step: NonTerminal {
    override val representation: String = "step"
    override val productions: List<Production> = listOf(
        production(Actions::class)
    )
}

data object Actions: NonTerminal {
    override val representation: String = "actions"
    override val productions: List<Production> = listOf(
        production(Action::class, Actions::class),
        epsilon(),
    )
}

data object Action: NonTerminal {
    override val representation: String = "action"
    override val productions: List<Production> = listOf(
        production(Tap::class),
        production(Input::class),
        production(Wait::class),
        production(Delay::class),
        production(Assert::class),
    )
}

data object Tap: NonTerminal {
    override val representation: String = "tap"
    override val productions: List<Production> = listOf(
        production(Element::class, TapType::class)
    )
}

data object Input: NonTerminal {
    override val representation: String = "input"
    override val productions: List<Production> = listOf(
        production(Element::class, InputData::class),
    )
}

data object Wait: NonTerminal {
    override val representation: String = "wait"
    override val productions: List<Production> = listOf(
        production(Condition::class, Timeout::class)
    )
}

data object Assert: NonTerminal {
    override val representation: String = "assert"
    override val productions: List<Production> = listOf(
        production(Condition::class)
    )
}

data object Condition: NonTerminal {
    override val representation: String = "condition"
    override val productions: List<Production> = listOf(
        production(Element::class, ElementProperty::class, PropertyValue::class)
    )
}

data object Delay: NonTerminal {
    override val representation: String = "delay"
    override val productions: List<Production> = listOf(
        listOf(Duration::class)
    )
}
