package com.digitalsamurai.automaton.grabber

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta

public class AutomatonGrabber<M : AutomatonGrabberStorageMeta> internal constructor(
    private val storage: AutomatonGrabberStorage<M>,
) {

    /**
     * Synchronizes a parsed API through the configured storage.
     *
     * @param request parsed API specification.
     * @param meta metadata required by the storage.
     * @return Unit after the storage handles the request.
     */
    public fun sync(
        request: AutomatonGrabberParsedApi,
        meta: M,
    ) {
        storage.sync(request = request, meta = meta)
    }
}
