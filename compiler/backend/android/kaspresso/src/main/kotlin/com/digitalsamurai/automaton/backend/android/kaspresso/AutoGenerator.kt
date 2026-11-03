package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

abstract class AutoGenerator {

    abstract fun generate( model: SemanticModel)

    fun autoGenerateMessage(): String {
        val output = StringBuilder()
        output.appendLine("/**")
        output.appendLine("* WARN! AUTO-GENERATED")
        output.appendLine("* Generate with love by Automaton")
        output.appendLine("* Datetime: ${DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss").format(LocalDateTime.now())}")
        output.appendLine("*/")
        return output.toString()
    }

    fun autoGenerateMessageForKotlinPoet(): String {
        val output = StringBuilder()
        output.appendLine("WARN! AUTO-GENERATED")
        output.appendLine("Generate with love by Automaton")
        output.appendLine("Datetime: ${DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm:ss").format(LocalDateTime.now())}")
        return output.toString()
    }


    fun MutableList<String>.enter() {
        add("")
    }

    fun OutputStreamWriter.enter() {
        write("\n")
    }
}