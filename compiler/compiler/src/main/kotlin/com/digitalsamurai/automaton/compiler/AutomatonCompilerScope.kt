package com.digitalsamurai.automaton.compiler

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.compiler.factory.AutomatonCompilerFactory
import com.digitalsamurai.automaton.frontend.api.AutomatonInput

public val Automaton.compiler: CompilerScope
    get() = CompilerScope

public fun Automaton.compiler(compiler: CompilerScope.() -> Unit) {

}

public object CompilerScope {

    fun<T: AutomatonInput> factory(factoryScope: AutomatonCompilerFactory<T>.() -> Unit): AutomatonCompiler<T> {
        val factory = AutomatonCompilerFactory<T>()
        factory.factoryScope()
        return factory.build()
    }





}
