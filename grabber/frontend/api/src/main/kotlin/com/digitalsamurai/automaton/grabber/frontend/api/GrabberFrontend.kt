package com.digitalsamurai.automaton.grabber.frontend.api

import kotlinx.coroutines.flow.SharedFlow

public interface GrabberFrontend {

    // TODO отказаться от Flow в API и перейти к синхронному методу обработки входных данных.
    // Frontend должен сам управлять исполнением: последовательно в одном потоке или параллельно по своему усмотрению.
    val requestsFlow: SharedFlow<AutomatonGrabberRequest>
}
