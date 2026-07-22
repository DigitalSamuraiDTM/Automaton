package com.digitalsamurai.automaton.compiler.factory

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.compiler.AutomatonCompiler
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend

public class AutomatonCompilerFactory<T: AutomatonFrontend> internal constructor(
    private val frontend: T,
) {

    private var backends: List<AutomatonBackend> = emptyList()

    public fun backends(vararg backends: AutomatonBackend) {
        this.backends = backends.toList()
    }


    public fun build(): AutomatonCompiler<T> {
        return AutomatonCompiler(
            backends = backends,
            frontend = frontend
        )
    }

}