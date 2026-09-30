package com.digitalsamurai.automaton.compiler.factory

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.compiler.AutomatonCompiler
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.frontend.api.AutomatonResult
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.parser.ll1.LL1Parser
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

public class AutomatonCompilerFactory<T: AutomatonFrontend> internal constructor(
    // TODO list of frontends
    private val frontend: T,
) {

    private var backends: List<AutomatonBackend> = emptyList()
    var parser: LL1Parser? = null
    private var logger: AutomatonLogger? = null
    private var resultFlowSettings: MutableSharedFlow<AutomatonResult>? = null

    public fun backends(vararg backends: AutomatonBackend) {
        this.backends = backends.toList()
    }


    public fun logger(logger: AutomatonLogger) {
        this.logger = logger
    }

    public fun resultFlow(flow: MutableSharedFlow<AutomatonResult>) {
        this.resultFlowSettings = flow
    }


    public fun build(): AutomatonCompiler<T> {
        val ll1Parser = LL1Parser(logger = logger)
        parser = ll1Parser
        return AutomatonCompiler(
            backends = backends,
            frontend = frontend,
            parser = ll1Parser,
            logger = logger,
            resultFlow =  resultFlowSettings
        )
    }

}