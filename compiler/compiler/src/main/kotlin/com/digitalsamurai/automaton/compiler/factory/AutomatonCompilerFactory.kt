package com.digitalsamurai.automaton.compiler.factory

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.compiler.AutomatonCompiler
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.parser.api.AutomatonParser
import com.digitalsamurai.automaton.parser.ll1.LL1Parser

public class AutomatonCompilerFactory<T: AutomatonFrontend> internal constructor(
    private val frontend: T,
) {

    private var backends: List<AutomatonBackend> = emptyList()
    private var parser: AutomatonParser = LL1Parser

    public fun backends(vararg backends: AutomatonBackend) {
        this.backends = backends.toList()
    }

    public fun parser(parser: AutomatonParser) {
        this.parser = parser
    }


    public fun build(): AutomatonCompiler<T> {
        parser.initialize()
        return AutomatonCompiler(
            backends = backends,
            frontend = frontend,
            parser = parser
        )
    }

}