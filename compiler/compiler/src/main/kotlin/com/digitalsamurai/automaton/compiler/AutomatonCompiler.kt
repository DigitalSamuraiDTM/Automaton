package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.grammar.Token
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import com.digitalsamurai.automaton.semantic.SemanticAnalyzer
import com.digitalsamurai.automaton.semantic.SemanticModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

public class AutomatonCompiler<T : AutomatonFrontend> internal constructor(
    private val backends: List<AutomatonBackend>,
    private val frontend: T,
    private val parser: AutomatonParser,
) {
    private val automatonScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val semanticAnalyzer: SemanticAnalyzer = SemanticAnalyzer()

    init {
        listenFrontend()
    }

    private fun listenFrontend() {
        automatonScope.launch {
            frontend.tokensFlow
                .makeGrammarAnalyze()
                .makeSemanticAnalyze()
                .buildBackendOutputs()
                .collect { backendResult ->
                    println(backendResult)
                }
        }
    }

    /**
     * Make token sequence analyze and convert it to tree structure
     * @return [Ast] tree or throw [AutomatonGrammarException]
     */
    private fun Flow<List<Token<*>>>.makeGrammarAnalyze(): Flow<Ast> {
        return map { list -> parser.parse(list) }
    }

    /**
     * Make semantic analyze with logic checks
     * @return [Ast] the same structure or throw [AutomatonSemanticException]
     */
    private fun Flow<Ast>.makeSemanticAnalyze(): Flow<SemanticModel> {
        return map { semanticAnalyzer.analyzeFull(it) }
    }

    private fun Flow<SemanticModel>.buildBackendOutputs(): Flow<List<Result<BackendOutput>>> {
        return map { tree ->
            backends.map { backend -> backend.buildOutput(tree) }
        }
    }
}