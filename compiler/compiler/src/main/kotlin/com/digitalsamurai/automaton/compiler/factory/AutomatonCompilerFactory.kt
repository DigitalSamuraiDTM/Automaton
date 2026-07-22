package com.digitalsamurai.automaton.compiler.factory

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.frontend.api.AutomatonInput
import com.digitalsamurai.automaton.compiler.AutomatonCompiler

public class AutomatonCompilerFactory<T: AutomatonInput> internal constructor() {

    private var backends: List<AutomatonBackend> = emptyList()

    public fun backends(vararg backends: AutomatonBackend) {
        this.backends = backends.toList()

    }


    public fun build(): AutomatonCompiler<T> {
        return AutomatonCompiler(
            backends = backends
        )
    }

}