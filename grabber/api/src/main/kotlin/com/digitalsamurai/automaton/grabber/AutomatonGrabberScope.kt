package com.digitalsamurai.automaton.grabber

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.grabber.factory.AutomatonGrabberFactory
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta

public val Automaton.grabber: GrabberScope
    get() = GrabberScope

public object GrabberScope {

    /**
     * Configures and builds a grabber with the required storage.
     *
     * @param storage storage used by the grabber.
     * @param factoryScope grabber factory configuration.
     * @return configured [AutomatonGrabber].
     */
    fun <M : AutomatonGrabberStorageMeta> factory(
        storage: AutomatonGrabberStorage<M>,
        factoryScope: AutomatonGrabberFactory<M>.() -> Unit = {},
    ): AutomatonGrabber<M> {
        val factory = AutomatonGrabberFactory(storage)
        factory.factoryScope()
        return factory.build()
    }
}
