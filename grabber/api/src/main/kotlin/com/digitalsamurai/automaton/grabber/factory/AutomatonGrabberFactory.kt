package com.digitalsamurai.automaton.grabber.factory

import com.digitalsamurai.automaton.grabber.AutomatonGrabber
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta

// TODO: Если для операций потребуются разные metadata, добавлять в фабрику независимые сценарии.
//  У каждого сценария будут свои storage и metadata; frontend сможет использовать несколько AutomatonGrabber.

public class AutomatonGrabberFactory<M : AutomatonGrabberStorageMeta> internal constructor(
    private val storage: AutomatonGrabberStorage<M>,
) {

    /**
     * Builds a grabber with the configured storage.
     *
     * @return configured [AutomatonGrabber].
     */
    public fun build(): AutomatonGrabber<M> {
        return AutomatonGrabber(storage = storage)
    }
}
