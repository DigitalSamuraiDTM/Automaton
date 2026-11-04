package com.digitalsamurai.automaton.sample

import kotlin.Int
import kotlin.String
import kotlin.collections.Map
import mockwebserver3.MockResponse
import okhttp3.Headers.Companion.toHeaders

/**
 * WARN! AUTO-GENERATED
 * Generate with love by Automaton
 * Datetime: 04.11.2026 03:19:33
 */
public data class AutomatonMockRule(
  public val path: String,
  public val method: String,
  public val code: Int,
  public val headers: Map<String, String>,
  public val body: String,
) {
  public fun toMockResponse(): MockResponse = MockResponse(
      code = code,
      headers = headers.toHeaders(),
      body = body,
  )
}
