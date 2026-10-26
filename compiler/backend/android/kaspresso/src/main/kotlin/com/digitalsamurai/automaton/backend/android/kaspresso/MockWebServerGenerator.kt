package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File

class MockWebServerGenerator(val appPackage: String, val outputDir: File): AutoGenerator() {

    private val automatonDispatcherGenerator = AutomatonDispatcherGenerator()

    override fun generate(model: SemanticModel) {
        val imports = mutableMapOf<String, String>()

        val mockWebServerClass = generateMockWebServerClass(imports, model)


        val output = File(outputDir, "AutomatonMockWebServer.kt")
        output.writer().use { writer ->
            writer.write("package $appPackage\n")
            writer.write("\n")
            imports.forEach { (key, import) ->
                writer.write("import $import\n")
            }
            writer.write("\n")
            writer.write(autoGenerateMessage())
            writer.write(mockWebServerClass)
        }

    }

    private fun generateMockWebServerClass(imports: MutableMap<String, String>, model: SemanticModel): String {
        val classString = StringBuilder()

        classString.appendLine("public class AutomatonMockWebServer {")
        mockWebServerClassBody(imports, model).forEach {
            classString.appendLine(tab()+it)
        }
        classString.appendLine("}")
        return classString.toString()
    }

    private fun mockWebServerClassBody(imports: MutableMap<String,String>, model: SemanticModel): List<String> {
        val body = mutableListOf<String>()
        imports["MockRule"] = "${appPackage}.AutomatonMockRule"
        body.enter()
        body.add("@Volatile")
        body.add("private var activeRules: Set<AutomatonMockRule> = emptySet()")
        body.add("private val dispatcher = AutomatonDispatcher()")
        body.enter()

        automatonDispatcherGenerator.generateInner(imports).forEach {
            body.add(tab()+it)
        }
        return body
    }
}