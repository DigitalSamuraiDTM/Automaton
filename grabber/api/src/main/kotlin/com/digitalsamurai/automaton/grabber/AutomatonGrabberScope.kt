package com.digitalsamurai.automaton.grabber

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.grabber.factory.AutomatonGrabberFactory
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageFactory
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta
import com.digitalsamurai.automaton.grabber.storage.api.GrabberStoragesScope

public val Automaton.grabber: GrabberScope
    get() = GrabberScope

public object GrabberScope {

    public val storages: GrabberStoragesScope
        get() = GrabberStoragesScope

    /**
     * Configures a grabber factory with the required storage factory.
     *
     * @param storageFactory factory that builds the grabber storage.
     * @param factoryScope grabber factory configuration.
     * @return configured [AutomatonGrabberFactory].
     */
    fun <M : AutomatonGrabberStorageMeta> factory(
        storageFactory: AutomatonGrabberStorageFactory<M>,
        factoryScope: AutomatonGrabberFactory<M>.() -> Unit = {},
    ): AutomatonGrabberFactory<M> {
        val factory = AutomatonGrabberFactory(storageFactory)
        factory.factoryScope()
        return factory
    }
}

