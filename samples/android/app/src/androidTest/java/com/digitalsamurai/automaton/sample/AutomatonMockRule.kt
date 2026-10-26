package com.digitalsamurai.automaton.sample

import mockwebserver3.MockResponse

/**
* WARN! AUTO-GENERATED
* Generate with love by Automaton
* Datetime: 1792968885516
*/
public data class AutomatonMockRule(
    public val path: String,
    public val method: String,
    public val code: Int,
    public val body: String,
) {
    fun toMockResponse(): MockResponse {
        return MockResponse(
            code = code,
            body = body,
        )
    }

}
