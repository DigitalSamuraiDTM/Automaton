package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.AutoGenerator
import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File

// TODO проблема синхронизации активных моков и теста. При быстром тесте результаты запросов в шаге 1 и шаге 2 могут быть разными, но из-за отсутствия
//  синхронизации мы получаем проблему: ответ запроса, который совершон на шаге 1 получит ответ мока ответа 2, потому что не успеет выполниться в рамках
//  шага 1
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
        imports["atomicReference"] = "java.util.concurrent.atomic.AtomicReference"
        body.enter()
        body.add("@Volatile")
        body.add("private var activeRules: AtomicReference<ActiveServerMocks> = AtomicReference(ActiveServerMocks(emptySet(), emptySet()))")
        body.add("private val dispatcher = AutomatonDispatcher()")
        body.enter()

        body.add("fun setStepRules(newRules: Set<AutomatonMockRule>) {")
        body.add(tab() + "activeRules.updateAndGet { rules ->")
        body.add(tab(2) + "ActiveServerMocks(")
        body.add(tab(3) + "general = rules.general,")
        body.add(tab(3) + "step = newRules,")
        body.add(tab(2) + ")")
        body.add(tab(1) +"}")
        body.add("}")
        body.enter()

        body.add("fun setGeneralRules(newRules: Set<AutomatonMockRule>) {")
        body.add(tab() + "activeRules.updateAndGet { rules ->")
        body.add(tab(2) + "ActiveServerMocks(")
        body.add(tab(3) + "general = newRules,")
        body.add(tab(3) + "step = rules.step,")
        body.add(tab(2) + ")")
        body.add(tab(1) +"}")
        body.add("}")
        body.enter()

        body.add("data class ActiveServerMocks(")
        body.add(tab()+ "val general: Set<AutomatonMockRule>,")
        body.add(tab()+ "val step: Set<AutomatonMockRule>,")
        body.add(") {")
        body.add(tab()+"val rules = general + step")
        body.add("}")
        body.enter()

        automatonDispatcherGenerator.generateInner(imports).forEach {
            body.add(it)
        }
        return body
    }
}