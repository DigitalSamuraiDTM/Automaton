package com.digitalsamurai.automaton.sample

import com.digitalsamurai.automaton.sample.AutomatonMockRule
import java.util.concurrent.atomic.AtomicReference
import mockwebserver3.Dispatcher
import mockwebserver3.RecordedRequest
import mockwebserver3.MockResponse

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1793055318658
*/
public class AutomatonMockWebServer {
    
    @Volatile
    private var activeRules: AtomicReference<ActiveServerMocks> = AtomicReference(ActiveServerMocks(emptySet(), emptySet()))
    private val dispatcher = AutomatonDispatcher()
    
    fun setStepRules(newRules: Set<AutomatonMockRule>) {
        activeRules.updateAndGet { rules ->
            ActiveServerMocks(
                general = rules.general,
                step = newRules,
            )
        }
    }
    
    fun setGeneralRules(newRules: Set<AutomatonMockRule>) {
        activeRules.updateAndGet { rules ->
            ActiveServerMocks(
                general = newRules,
                step = rules.step,
            )
        }
    }
    
    data class ActiveServerMocks(
        val general: Set<AutomatonMockRule>,
        val step: Set<AutomatonMockRule>,
    ) {
        val rules = general + step
    }
    
    inner class AutomatonDispatcher(): Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val mockRules = activeRules.get().rules.filter { it.method == request.method && it.path == request.url.encodedPath }
            check(mockRules.isEmpty()) { return MockResponse(code = 404, body = "No active Automaton mock") }
            val mockRule = mockRules.single()
            return mockRule.toMockResponse()
        }
    }
}
