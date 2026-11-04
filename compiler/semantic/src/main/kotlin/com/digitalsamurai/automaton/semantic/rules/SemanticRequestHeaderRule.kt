package com.digitalsamurai.automaton.semantic.rules

import com.digitalsamurai.automaton.semantic.SemanticModel

/**
 * Semantic rule "Content type of mocked responses" (README): resolves headers of the mocked response.
 * Body type defines default `Content-Type` (json -> application/json, plain -> text/plain),
 * explicit `Content-Type` overrides it (case-insensitive, compatibility is not checked).
 * Result always contains `Content-Type`. Changes of the rule must be reflected in README.
 */
internal class SemanticRequestHeaderRule {

    /**
     * Resolves headers of the mocked response
     * @param body body of the mocked response
     * @param explicitHeaders headers written in the program explicitly
     * @return headers of the mocked response, always contains `Content-Type`
     */
    fun resolve(
        body: SemanticModel.Request.Http.Body,
        explicitHeaders: Map<String, String>,
    ): Map<String, String> {
        if (explicitHeaders.keys.any { it.equals(CONTENT_TYPE_HEADER, ignoreCase = true) }) {
            return explicitHeaders
        }
        return mapOf(CONTENT_TYPE_HEADER to body.defaultContentType()) + explicitHeaders
    }

    /**
     * Default content type defined by the body type
     */
    private fun SemanticModel.Request.Http.Body.defaultContentType(): String {
        return when (this) {
            is SemanticModel.Request.Http.Body.JsonArray -> "application/json"
            is SemanticModel.Request.Http.Body.JsonObject -> "application/json"
            is SemanticModel.Request.Http.Body.Plain -> "text/plain"
        }
    }

    private companion object {
        const val CONTENT_TYPE_HEADER = "Content-Type"
    }
}
