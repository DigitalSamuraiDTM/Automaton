package com.digitalsamurai.automaton.backend.api

fun tab(count: Int = 1): String = "    ".repeat(count)

fun String.camelCase(): String {
    var spaced = false
    var out = ""
    forEach { char ->
        if (spaced) {
            out += char.uppercaseChar()
            spaced = false
        } else if (char == ' ') {
            spaced = true
        } else {
            out += char
        }
    }
    return out
}