package com.digitalsamurai.automaton.grabber.storage.api

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi

public interface AutomatonGrabberStorageMeta

public interface AutomatonGrabberStorage<M : AutomatonGrabberStorageMeta> {

    /**
     * Synchronizes the parsed API using storage-specific metadata.
     *
     * @param request parsed API specification.
     * @param meta metadata required by the storage.
     * @return Unit after the synchronization request is handled.
     */
    public fun sync(
        request: AutomatonGrabberParsedApi,
        meta: M,
    )
}
