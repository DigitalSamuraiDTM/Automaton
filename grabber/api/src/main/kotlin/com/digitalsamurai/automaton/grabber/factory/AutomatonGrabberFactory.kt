package com.digitalsamurai.automaton.grabber.factory

import com.digitalsamurai.automaton.grabber.AutomatonGrabber
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageFactory
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta

// TODO: Если для операций потребуются разные metadata, добавлять в фабрику независимые сценарии.
//  У каждого сценария будут свои storage и metadata; frontend сможет использовать несколько AutomatonGrabber.

public class AutomatonGrabberFactory<M : AutomatonGrabberStorageMeta> internal constructor(
    private val storageFactory: AutomatonGrabberStorageFactory<M>,
) {

    /**
     * Builds the storage and a grabber that uses it.
     *
     * @return configured [AutomatonGrabber].
     */
    public fun build(): AutomatonGrabber<M> {
        return AutomatonGrabber(storage = storageFactory.build())
    }
}
