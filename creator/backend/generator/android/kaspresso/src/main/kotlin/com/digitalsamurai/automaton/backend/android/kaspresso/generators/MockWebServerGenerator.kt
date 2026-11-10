package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.ClassGenerator
import com.digitalsamurai.automaton.backend.android.kaspresso.toStringLiteral
import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File

// TODO проблема синхронизации активных моков и теста. При быстром тесте результаты запросов в шаге 1 и шаге 2 могут быть разными, но из-за отсутствия
//  синхронизации мы получаем проблему: ответ запроса, который совершон на шаге 1 получит ответ мока ответа 2, потому что не успеет выполниться в рамках
//  шага 1
class MockWebServerGenerator(
    val appPackage: String,
    val host: String,
    val port: Int,
) : ClassGenerator() {

    private val automatonDispatcherGenerator = AutomatonDispatcherGenerator()

    override fun generateClassFile(model: SemanticModel): GeneratorOutput.FileData {
        val imports = mutableMapOf<String, String>()

        val mockWebServerClass = generateMockWebServerClass(imports, model)
        
        val data = buildString {
            appendLine("package $appPackage\n")
            appendLine("\n")
            imports.forEach { (key, import) ->
                appendLine("import $import\n")
            }
            appendLine("\n")
            appendLine(autoGenerateMessage())
            appendLine(mockWebServerClass)
        }

        return GeneratorOutput.FileData(
            name = "AutomatonMockWebServer.kt",
            data = data,
            )
    }

    private fun generateMockWebServerClass(imports: MutableMap<String, String>, model: SemanticModel): String {
        val classString = StringBuilder()

        classString.appendLine("public class AutomatonMockWebServer {")
        mockWebServerClassBody(imports, model).forEach {
            classString.appendLine(tab() + it)
        }
        classString.appendLine("}")
        return classString.toString()
    }

    private fun mockWebServerClassBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val body = mutableListOf<String>()
        imports["MockRule"] = "${appPackage}.AutomatonMockRule"
        imports["atomicReference"] = "java.util.concurrent.atomic.AtomicReference"
        imports["MockWebServer"] = "mockwebserver3.MockWebServer"
        body.enter()
        body.add("@Volatile")
        body.add("private var activeRules: AtomicReference<ActiveServerMocks> = AtomicReference(ActiveServerMocks(emptySet(), emptySet()))")
        body.add("private val dispatcher = AutomatonDispatcher()")
        body.add("private val server = MockWebServer()")
        body.enter()


        body.add("fun start() {")
        generateStartFunBody(imports).forEach { bodyLine -> body.add(tab() + bodyLine) }
        body.add("}")
        body.enter()

        body.add("fun stop() {")
        generateStopFunBody(imports, model).forEach { bodyLine -> body.add(tab() + bodyLine) }
        body.add("}")
        body.enter()

        body.add("fun setStepRules(newRules: Set<AutomatonMockRule>) {")
        body.add(tab() + "activeRules.updateAndGet { rules ->")
        body.add(tab(2) + "ActiveServerMocks(")
        body.add(tab(3) + "general = rules.general,")
        body.add(tab(3) + "step = newRules,")
        body.add(tab(2) + ")")
        body.add(tab(1) + "}")
        body.add("}")
        body.enter()

        body.add("fun setGeneralRules(newRules: Set<AutomatonMockRule>) {")
        generateSetGeneralRuleFunBody(imports, model).forEach { bodyLine -> body.add(tab() + bodyLine) }
        body.add("}")
        body.enter()

        body.add("data class ActiveServerMocks(")
        body.add(tab() + "val general: Set<AutomatonMockRule>,")
        body.add(tab() + "val step: Set<AutomatonMockRule>,")
        body.add(") {")
        body.add(tab() + "val rules = general + step")
        body.add("}")
        body.enter()

        automatonDispatcherGenerator.generateInner(imports).forEach {
            body.add(it)
        }
        return body
    }

    private fun generateStartFunBody(imports: MutableMap<String, String>): List<String> {
        val body = mutableListOf<String>()

        imports["InetAddress"] = "java.net.InetAddress"

        body.add("server.dispatcher = dispatcher")
        body.add("server.start(")
        body.add(tab() + "inetAddress = InetAddress.getByName(${host.toStringLiteral()}),")
        body.add(tab() + "port = ${port},")
        body.add(")")
        return body
    }

    private fun generateStopFunBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val body = mutableListOf<String>()
        body.add("server.close()")
        return body
    }

    private fun generateSetGeneralRuleFunBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val body = mutableListOf<String>()
        body.add(tab() + "activeRules.updateAndGet { rules ->")
        body.add(tab() + "ActiveServerMocks(")
        body.add(tab(2) + "general = newRules,")
        body.add(tab(2) + "step = rules.step,")
        body.add(tab() + ")")
        body.add("}")

        return body
    }
}