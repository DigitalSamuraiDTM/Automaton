package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.OutputStream
import java.io.OutputStreamWriter

abstract class AutoGenerator {

    abstract fun generate( model: SemanticModel)

    fun autoGenerateMessage(): String {
        val output = StringBuilder()
        output.appendLine("/**")
        output.appendLine("* WARN! AUTO-GENERATED")
        output.appendLine("* Generate with love by Automaton")
        output.appendLine("* Datetime: ${System.currentTimeMillis()}")
        output.appendLine("*/")
        return output.toString()
    }


    fun MutableList<String>.enter() {
        add("")
    }

    fun OutputStreamWriter.enter() {
        write("\n")
    }
}