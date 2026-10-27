package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel

internal fun SemanticModel.Request.toAutomatonMockRule(): List<String> {
    // TODO add method
    // TODO add code
    return when(this) {
        is SemanticModel.Request.Http -> {
            listOf(
                "AutomatonMockRule(",
                tab() + "path = ${this.url.path.toKotlinStringLiteral()},",
                tab() + "method = \"POST\",",
                tab() + "code = 200,",
                tab() + "body = ${this.body.bodyString().toKotlinStringLiteral()},",
                "),",
            )
        }
    }
}

private fun SemanticModel.Request.Http.Body.bodyString(): String {
    return when(this) {
        is SemanticModel.Request.Http.Body.JsonArray -> this.value.toString()
        is SemanticModel.Request.Http.Body.JsonObject -> this.value.toString()
        is SemanticModel.Request.Http.Body.Plain -> this.value
    }
}

/**
 * Escapes string to valid kotlin string literal (with quotes)
 */
private fun String.toKotlinStringLiteral(): String {
    val escaped = this
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("$", "\\$")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
    return "\"$escaped\""
}
