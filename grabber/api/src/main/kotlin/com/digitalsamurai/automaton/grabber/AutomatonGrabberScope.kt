package com.digitalsamurai.automaton.grabber

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.grabber.factory.AutomatonGrabberFactory
import com.digitalsamurai.automaton.grabber.frontend.api.GrabberFrontend

public val Automaton.grabber: GrabberScope
    get() = GrabberScope

public object GrabberScope {

    /**
     * Configures and builds a grabber with the required frontend.
     *
     * @param frontend source of grabber requests.
     * @param factoryScope grabber factory configuration.
     * @return configured [AutomatonGrabber].
     */
    fun <T : GrabberFrontend> factory(
        frontend: T,
        factoryScope: AutomatonGrabberFactory<T>.() -> Unit,
    ): AutomatonGrabber<T> {
        val factory = AutomatonGrabberFactory(frontend)
        factory.factoryScope()
        return factory.build()
    }
}
