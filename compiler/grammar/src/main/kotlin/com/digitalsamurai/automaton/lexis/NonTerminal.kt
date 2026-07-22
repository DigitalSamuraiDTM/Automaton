package com.digitalsamurai.automaton.lexis

sealed interface NonTerminal: GrammarSymbol<Unit>

data object CompilationUnit: NonTerminal {
    override val representation: String = "CompilationUnit"
}

data object Context: NonTerminal {
    override val representation: String = "Context"
}

data object Test: NonTerminal {
    override val representation: String = "Test"
}

data object Metadata: NonTerminal {
    override val representation: String = "Metadata"
}

data object StepList: NonTerminal {
    override val representation: String = "StepList"
}

data object Step: NonTerminal {
    override val representation: String = "Step"
}

data object ActionList: NonTerminal {
    override val representation: String = "ActionList"
}

data object Action: NonTerminal {
    override val representation: String = "Action"
}

data object Tap: NonTerminal {
    override val representation: String = "Tap"
}

data object Input: NonTerminal {
    override val representation: String = "Input"
}

data object Wait: NonTerminal {
    override val representation: String = "Wait"
}

data object Assert: NonTerminal {
    override val representation: String = "Assert"
}

data object Condition: NonTerminal {
    override val representation: String = "Condition"
}

data object Delay: NonTerminal {
    override val representation: String = "Delay"
}
