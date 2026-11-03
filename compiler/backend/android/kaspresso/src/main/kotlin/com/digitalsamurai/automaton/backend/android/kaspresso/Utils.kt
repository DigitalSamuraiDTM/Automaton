package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel

internal fun SemanticModel.Request.toAutomatonMockRule(): List<String> {
    return when(this) {
        is SemanticModel.Request.Http -> {
            buildList {
                add("AutomatonMockRule(")
                add(tab() + "path = ${url.path.toStringLiteral()},")
                add(tab() + "method = ${requestType.name.toStringLiteral()},")
                add(tab() + "code = 200,") // TODO get from semantic model
                generateHeaders(headers).forEach { headersLine ->
                    add(tab() + headersLine)
                }
                add(tab() + "body = ${body.bodyString().toStringLiteral()},")
                add("),")
            }
        }
    }
}

/**
 * Generates "headers" argument of AutomatonMockRule. Headers are resolved by semantic analyzer
 */
private fun generateHeaders(headers: Map<String, String>): List<String> {
    if (headers.isEmpty()) return listOf(tab() + "emptyMap(),")
    return buildList {
        add(tab() + "headers = mapOf(")
        headers.forEach { (name, value) ->
            add(tab(2) + "${name.toStringLiteral()} to ${value.toStringLiteral()},")
        }
        add(tab() + "),")
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
public fun String.toStringLiteral(): String {
    val escaped = this
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("$", "\\$")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
    return "\"$escaped\""
}
