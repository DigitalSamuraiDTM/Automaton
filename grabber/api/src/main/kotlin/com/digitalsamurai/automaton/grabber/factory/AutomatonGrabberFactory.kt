package com.digitalsamurai.automaton.grabber.factory

import com.digitalsamurai.automaton.grabber.AutomatonGrabber
import com.digitalsamurai.automaton.grabber.frontend.api.GrabberFrontend

public class AutomatonGrabberFactory<T : GrabberFrontend> internal constructor(
    private val frontend: T,
) {

    /**
     * Builds a grabber with the required frontend.
     *
     * @return configured [AutomatonGrabber].
     */
    public fun build(): AutomatonGrabber<T> {
        return AutomatonGrabber(frontend = frontend)
    }
}
