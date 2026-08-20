package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.grammar.Token
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

public class AutomatonCompiler<T: AutomatonFrontend> internal constructor(
    private val backends: List<AutomatonBackend>,
    private val frontend: T,
    private val parser: AutomatonParser,
) {
    private val automatonScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        listenFrontend()
    }

    private fun listenFrontend() {
        automatonScope.launch {
            frontend.tokensFlow.mapToAst().collect { tree ->
                println(tree.toStringTree(""))
            }
        }
    }

    private fun Flow<List<Token<*>>>.mapToAst(): Flow<AstNode> {
        return map { list -> parser.parse(list)}
    }
}