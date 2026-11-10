package com.digitalsamurai.automaton.creator

import com.digitalsamurai.automaton.api.AutomatonException
import com.digitalsamurai.automaton.ast.Ast
import com.digitalsamurai.automaton.ast.AstData
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendResult
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
public class AutomatonCreator<T : AutomatonFrontend> internal constructor(
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
        automatonScope.launch {
            frontend
                .tokensFlow
                .collect { request ->
                    try {
                        logger?.info("Begin process Automaton request: ${request.requestId}")
                        val astData = request.makeGrammarAnalyze()
                        val semanticModel = astData.makeSemanticAnalyze()
                        val backendOutputs = semanticModel.makeOutput()
                        backendOutputs.emitResults()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: AutomatonException) {
                        logger?.error(e)
                        emitFailure(request.requestId, e)
                    } catch (e: Exception) {
                        val exception = AutomatonCreatorException(e)
                        logger?.error(exception)
                        emitFailure(request.requestId, exception)
                    }
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
    private suspend fun SemanticModel.makeOutput(): List<BackendResult> {
        return backends.map { backend -> backend.makeOutput(this, logger) }
    }

    /**
     * Публикует результаты всех backends.
     *
     * @return Unit после публикации всех результатов или при отсутствии настроенного result flow.
     */
    private suspend fun List<BackendResult>.emitResults() {
        forEach { result ->
            emitResult(
                AutomatonResult.Success(
                    requestId = result.requestId,
                    backendMetadata = result.metadata,
                )
            )
        }
    }

    /**
     * Публикует ошибку обработки Automaton-запроса.
     *
     * @param requestId идентификатор запроса.
     * @param exception ошибка компиляции или выполнения backend.
     * @return Unit после публикации ошибки или при отсутствии настроенного result flow.
     */
    private suspend fun emitFailure(requestId: String, exception: AutomatonException) {
        emitResult(
            AutomatonResult.Failure(
                requestId = requestId,
                exception = exception,
            )
        )
    }

    /**
     * Публикует результат и логирует его отправку.
     *
     * @param result результат обработки Automaton-запроса.
     * @return Unit после публикации результата или при отсутствии настроенного result flow.
     */
    private suspend fun emitResult(result: AutomatonResult) {
        val resultDescription = when (result) {
            is AutomatonResult.Failure -> "failure: ${result.exception.message}"
            is AutomatonResult.Success -> "success: ${result.backendMetadata}"
        }
        val flow = resultFlow
        if (flow == null) {
            logger?.warning("Result was not emitted because result flow is not configured. Request: ${result.requestId}; result: $resultDescription")
            return
        }
        flow.emit(result)
        logger?.info("Emitted result for Automaton request ${result.requestId}: $resultDescription")
    }
}