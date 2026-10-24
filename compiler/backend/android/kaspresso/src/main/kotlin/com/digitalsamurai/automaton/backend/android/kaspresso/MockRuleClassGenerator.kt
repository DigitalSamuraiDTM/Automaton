package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.tab
import java.io.File

class MockRuleClassGenerator(val appPackage: String, val outputDir: File): AutoGenerator() {

    fun generate() {
        val classString = StringBuilder()

        classString.appendLine("public data class AutomatonMockRule(")
        classString.appendLine("${tab()}public val path: String,")
        classString.appendLine("${tab()}public val method: String,")
//        envObjectStringBuilder.appendLine("${tab()}public val response: String,") // TODO OBAMA
        classString.appendLine(")")

        val outputFile = File(outputDir,  "AutomatonMockRule.kt")
        outputFile.outputStream().writer().use { writer ->
            writer.write("package ${appPackage}\n")

            writer.write(autoGenerateMessage())
            writer.write(classString.toString())
        }

    }
}