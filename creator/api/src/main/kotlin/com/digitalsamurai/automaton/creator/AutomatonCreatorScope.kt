package com.digitalsamurai.automaton.creator

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.creator.factory.AutomatonCreatorFactory
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend

public val Automaton.creator: CreatorScope
    get() = CreatorScope

public object CreatorScope {

    fun<T: AutomatonFrontend> factory(frontend: T, factoryScope: AutomatonCreatorFactory<T>.() -> Unit): AutomatonCreator<T> {
        val factory = AutomatonCreatorFactory(frontend)
        factory.factoryScope()
        return factory.build()
    }





}
