package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.AutoGenerator
import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File

class EnvironmentGenerator(
    val appPackage: String,
    val outputDir: File,
): AutoGenerator() {

    private val mockRuleClassGenerator = MockRuleClassGenerator(appPackage, outputDir)
    private val mockWebServerGenerator = MockWebServerGenerator(appPackage, outputDir)


    override fun generate(model: SemanticModel) {
        generateEnvironmentHolderClass()
        mockRuleClassGenerator.generate(model)
        mockWebServerGenerator.generate(model)
    }

    private fun generateEnvironmentHolderClass() {
        val classString = StringBuilder()
        classString.appendLine("public object AutomatonEnvironment {")
        classString.appendLine()
        classString.appendLine("${tab()}public val mockWebServer = AutomatonMockWebServer()")
        classString.appendLine()
        classString.appendLine("}")

        val outputFile = File(outputDir,  "AutomatonEnvironment.kt")
        outputFile.outputStream().writer().use { writer ->
            writer.write("package ${appPackage}")
            writer.enter()
            writer.write("import ${appPackage}.AutomatonMockWebServer")
            writer.enter()
            writer.enter()
            writer.write(autoGenerateMessage())
            writer.write(classString.toString())
        }
    }


}