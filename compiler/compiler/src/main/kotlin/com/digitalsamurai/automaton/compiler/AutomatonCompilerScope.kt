package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.compiler.factory.AutomatonCompilerFactory
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend

public val Automaton.compiler: CompilerScope
    get() = CompilerScope

public object CompilerScope {

    fun<T: AutomatonFrontend> factory(frontend: T, factoryScope: AutomatonCompilerFactory<T>.() -> Unit): AutomatonCompiler<T> {
        val factory = AutomatonCompilerFactory(frontend)
        factory.factoryScope()
        return factory.build()
    }





}
