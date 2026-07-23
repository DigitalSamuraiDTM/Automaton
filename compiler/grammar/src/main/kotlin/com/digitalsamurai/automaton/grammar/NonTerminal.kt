package com.digitalsamurai.automaton.grammar

sealed interface NonTerminal: GrammarSymbol<Unit>

data object CompilationUnit: NonTerminal {
    override val representation: String = "compilationUnit"
}

data object Context: NonTerminal {
    override val representation: String = "context"
}

data object Test: NonTerminal {
    override val representation: String = "test"
}

data object Metadata: NonTerminal {
    override val representation: String = "metadata"
}

data object StepList: NonTerminal {
    override val representation: String = "stepList"
}

data object Step: NonTerminal {
    override val representation: String = "step"
}

data object ActionList: NonTerminal {
    override val representation: String = "actionList"
}

data object Action: NonTerminal {
    override val representation: String = "action"
}

data object Tap: NonTerminal {
    override val representation: String = "tap"
}

data object Input: NonTerminal {
    override val representation: String = "input"
}

data object Wait: NonTerminal {
    override val representation: String = "wait"
}

data object Assert: NonTerminal {
    override val representation: String = "assert"
}

data object Condition: NonTerminal {
    override val representation: String = "condition"
}

data object Delay: NonTerminal {
    override val representation: String = "delay"
}
