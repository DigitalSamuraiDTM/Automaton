package com.digitalsamurai.automaton.sample

import com.digitalsamurai.automaton.sample.AutomatonMockRule
import java.util.concurrent.atomic.AtomicReference
import mockwebserver3.MockWebServer
import java.net.InetAddress
import mockwebserver3.Dispatcher
import mockwebserver3.RecordedRequest
import mockwebserver3.MockResponse

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1793649711708
*/
public class AutomatonMockWebServer {
    
    @Volatile
    private var activeRules: AtomicReference<ActiveServerMocks> = AtomicReference(ActiveServerMocks(emptySet(), emptySet()))
    private val dispatcher = AutomatonDispatcher()
    private val server = MockWebServer()
    
    fun start() {
        server.dispatcher = dispatcher
        server.start(
            inetAddress = InetAddress.getByName("127.0.0.1"),
            port = 8080,
        )
    }
    
    fun stop() {
        server.close()
    }
    
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
            val mockRule = activeRules.get().rules.firstOrNull { it.method == request.method && it.path == request.url.encodedPath }
                ?: return MockResponse(code = 404, body = "No active Automaton mock")
            return mockRule.toMockResponse()
        }
    }
}
