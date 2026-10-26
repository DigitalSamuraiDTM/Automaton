package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File

class MockRuleClassGenerator(val appPackage: String, val outputDir: File): AutoGenerator() {

    override fun generate(model: SemanticModel) {
        val classString = StringBuilder()
        val imports = mutableMapOf<String,String>()

        classString.appendLine("public data class AutomatonMockRule(")
        classString.appendLine("${tab()}public val path: String,")
        classString.appendLine("${tab()}public val method: String,")
        classString.appendLine("${tab()}public val code: Int,")
        classString.appendLine("${tab()}public val body: String,")
        classString.appendLine(") {")
        generateClassBody(imports).forEach { classBody ->
            classString.appendLine(tab() + classBody)
        }
        classString.appendLine("")
        classString.appendLine("}")

        val outputFile = File(outputDir,  "AutomatonMockRule.kt")
        outputFile.outputStream().writer().use { writer ->
            writer.write("package ${appPackage}")
            writer.enter()
            writer.enter()
            imports.forEach {
                writer.write("import " + it.value)
                writer.enter()
            }
            writer.enter()

            writer.write(autoGenerateMessage())
            writer.write(classString.toString())
        }

    }

    private fun generateClassBody(imports: MutableMap<String, String>): List<String> {
        imports["MockResponse"] = "mockwebserver3.MockResponse"
        val classBody = mutableListOf<String>()
        classBody.add("fun toMockResponse(): MockResponse {")
        generateToMockResponseFunBody(imports).forEach { funBody ->
            classBody.add(tab() + funBody)
        }
        classBody.add("}")
        return classBody
    }

    private fun generateToMockResponseFunBody(imports: MutableMap<String, String>): List<String> {
        val funBody = mutableListOf<String>()
        funBody.add("return MockResponse(")
        funBody.add(tab() + "code = code,")
//        funBody.add(tab() + "headers = headers,") // TODO implement it some later
        funBody.add(tab() + "body = body,")
        funBody.add(")")
        return funBody
    }
}