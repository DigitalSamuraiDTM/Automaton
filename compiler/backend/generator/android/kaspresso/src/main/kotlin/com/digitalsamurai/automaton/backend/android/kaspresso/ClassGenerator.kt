package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

abstract class ClassGenerator {

    abstract fun generateClassFile(model: SemanticModel): GeneratorOutput.FileData

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