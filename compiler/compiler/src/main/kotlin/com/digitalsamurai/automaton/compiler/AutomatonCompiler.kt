package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.ast.AstData
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.frontend.api.AutomatonRequest
import com.digitalsamurai.automaton.frontend.api.AutomatonResult
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import com.digitalsamurai.automaton.semantic.SemanticAnalyzer
import com.digitalsamurai.automaton.semantic.SemanticModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
public class AutomatonCompiler<T : AutomatonFrontend> internal constructor(
    private val backends: List<AutomatonBackend>,
    private val frontend: T,
    private val parser: AutomatonParser,
    private val logger: AutomatonLogger?,
    private val resultFlow: MutableSharedFlow<AutomatonResult>?,
) {

    private val automatonScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val semanticAnalyzer: SemanticAnalyzer = SemanticAnalyzer(logger)

    init {
        listenFrontend()
    }

    private fun listenFrontend() {
        automatonScope.launch() {
            frontend
                .tokensFlow
                .collect { request ->
                    logger?.info("Begin process Automaton request: ${request.requestId}")
                    val astData = request.makeGrammarAnalyze()
                    val semanticModel = astData.makeSemanticAnalyze()
                    val backendOutputs = semanticModel.buildBackendOutputs()
                    backendOutputs.emitResults()
                }

        }
    }

    /**
     * Make token sequence analyze and convert it to tree structure
     * @return [Ast] tree or throw [com.digitalsamurai.automaton.grammar.AutomatonGrammarException]
     */
    private fun AutomatonRequest.makeGrammarAnalyze(): AstData {
        return parser.parse(this)
    }

    /**
     * Make semantic analyze with logic checks
     * @return [Ast] the same structure or throw [com.digitalsamurai.automaton.semantic.AutomatonSemanticException]
     */
    private suspend fun AstData.makeSemanticAnalyze(): SemanticModel {
        return semanticAnalyzer.analyzeFull(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun SemanticModel.buildBackendOutputs(): List<BackendOutput> {
        return backends.map { backend -> backend.buildOutput(this) }
    }

    private suspend fun List<BackendOutput>.emitResults() {
        resultFlow?.let { resultFlow ->
            this.forEach { result ->
                resultFlow.emit(AutomatonResult.Success(
                    requestId = result.requestId,
                    backendMetadata = result.metaData
                ))
            }
        }
    }
}