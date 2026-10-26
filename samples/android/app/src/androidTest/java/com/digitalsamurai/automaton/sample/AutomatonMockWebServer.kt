package com.digitalsamurai.automaton.sample

import com.digitalsamurai.automaton.sample.AutomatonMockRule
import mockwebserver3.Dispatcher
import mockwebserver3.RecordedRequest
import mockwebserver3.MockResponse

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1792968885516
*/
public class AutomatonMockWebServer {
    
    @Volatile
    private var activeRules: Set<AutomatonMockRule> = emptySet()
    private val dispatcher = AutomatonDispatcher()
    
        inner class AutomatonDispatcher(): Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val mockRules = activeRules.filter { it.method == request.method && it.path == request.url.encodedPath }
                check(mockRules.isEmpty()) { return MockResponse(code = 404, body = "No active Automaton mock") }
                val mockRule = mockRules.single()
                return mockRule.toMockResponse()
            }
        }
}
