package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.grammar.Token
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

public class AutomatonCompiler<T : AutomatonFrontend> internal constructor(
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
            frontend.tokensFlow
                .makeGrammarAnalyze()
                .onEach {
                    println(it.toString())
                }
                .makeSemanticAnalyze()
                .buildBackendOutputs()
                .collect { backendResult ->
                    println(backendResult)
                }
        }
    }

    /**
     * Make token sequence analyze and convert it to tree structure
     * @return [AstNode] tree or throw [AutomatonGrammarException]
     */
    private fun Flow<List<Token<*>>>.makeGrammarAnalyze(): Flow<AstNode<*>> {
        return map { list -> parser.parse(list) }
    }

    /**
     * Make semantic analyze with logic checks
     * @return [AstNode] the same structure or throw [AutomatonSemanticException]
     */
    private fun Flow<AstNode<*>>.makeSemanticAnalyze(): Flow<AstNode<*>> {
        // TODO make sematic analyze
        return this
    }

    private fun Flow<AstNode<*>>.buildBackendOutputs(): Flow<List<Result<BackendOutput>>> {
        return map { tree ->
            backends.map { backend -> backend.buildOutput(tree) }
        }
    }
}