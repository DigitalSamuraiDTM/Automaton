package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.semantic.SemanticModel

internal fun SemanticModel.Request.toAutomatonMockRule(): List<String> {
    // TODO add method
    // TODO add code
    return when(this) {
        is SemanticModel.Request.Http -> {
            listOf(
           "AutomatonMockRule(",
           "path = ${this.url.path}",
           "method = \"POST\",",
           "code = 200,",
           "body = ${this.body}",
           ")"
            )
        }
    }
}

private fun SemanticModel.Request.Http.Body.bodyString(): String {
    return when(this) {
        is SemanticModel.Request.Http.Body.JsonArray -> this.toString()
        is SemanticModel.Request.Http.Body.JsonObject -> this.toString()
        is SemanticModel.Request.Http.Body.Plain -> this.value
    }
}