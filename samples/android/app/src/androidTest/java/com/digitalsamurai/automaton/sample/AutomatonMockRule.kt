package com.digitalsamurai.automaton.sample

import kotlin.Int
import kotlin.String
import mockwebserver3.MockResponse

/**
 * WARN! AUTO-GENERATED
 * Generate with love by Automaton
 * Datetime: 1793055318501
 */
public data class AutomatonMockRule(
  public val path: String,
  public val method: String,
  public val code: Int,
  public val body: String,
) {
  public fun toMockResponse(): MockResponse = MockResponse(
      code = code,
      body = body,
  )
}
