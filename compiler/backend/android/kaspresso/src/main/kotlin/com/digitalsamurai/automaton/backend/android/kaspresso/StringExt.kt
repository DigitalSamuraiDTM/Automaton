package com.digitalsamurai.automaton.backend.android.kaspresso

fun tab(): String = "    "

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