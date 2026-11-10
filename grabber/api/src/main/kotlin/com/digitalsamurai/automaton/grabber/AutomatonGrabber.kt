package com.digitalsamurai.automaton.grabber

import com.digitalsamurai.automaton.grabber.frontend.api.GrabberFrontend
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
public class AutomatonGrabber<T : GrabberFrontend> internal constructor(
    private val frontend: T,
) {

    private val automatonScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    init {
        listenFrontend()
    }

    /**
     * Starts printing requests received from the frontend.
     *
     * @return Unit after launching frontend flow collection.
     */
    private fun listenFrontend() {
        automatonScope.launch {
            frontend.requestsFlow.collect { request ->
                println(request)
            }
        }
    }
}
