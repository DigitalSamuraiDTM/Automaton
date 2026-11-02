package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.android.kaspresso.generators.EnvironmentGenerator
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.backend.api.camelCase
import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File
import java.lang.Exception

// TODO implement permission resolver
class AndroidKaspressoBackend(
    private val outputDirectory: File,
    private val appPackage: String,
    private val activity: AndroidAppActivity,
    private val composeApp: Boolean = true,
    mockWebserverHost: String,
    mockWebServerPort: Int,
) : AutoGenerator(), AutomatonBackend {

    private val environmentGenerator = EnvironmentGenerator(
        appPackage = appPackage,
        outputDir = outputDirectory,
        mockServerHost = mockWebserverHost,
        mockServerPort = mockWebServerPort,
    )

    init {
        if (!outputDirectory.exists()) throw IllegalArgumentException("Output directory '${outputDirectory.path}' does not exist")
        if (!outputDirectory.isDirectory) throw IllegalArgumentException("Output directory '${outputDirectory.path}' is not a directory")
    }

    override fun generate(model: SemanticModel) {
        // check environment needed?
        if (model.isMockWebServerNeeded()) {
            // generate mock web server environment
            environmentGenerator.generate(model)
        }

        val outputFile = File(outputDirectory, model.test.metadata.testName.camelCase() + ".kt")
        val imports = mutableMapOf<String, String>()
        val testClass = generateTestClass(
            imports = imports,
            model = model,
        )

        outputFile.outputStream().writer().use { writer ->

            writer.write("package $appPackage\n\n")
            imports.entries.forEach { entry ->
                writer.write("import ${entry.value}\n")
            }
            writer.write("\n")

            writer.write(autoGenerateMessage())
            writer.write(testClass)
        }
    }

    override suspend fun buildOutput(model: SemanticModel): BackendOutput {
        try {
            generate(model)
            return BackendOutput(model.requestId, "TODO")
        } catch (e: Exception) {
            return BackendOutput(model.requestId, e.toString())
        }
    }

    private fun generateClassBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        imports["JunitRule"] = "org.junit.Rule"
        imports["createAndroidComposeRule"] = "androidx.compose.ui.test.junit4.v2.createAndroidComposeRule"
        imports["activityName"] = activity.toString()
        imports["JunitTest"] = "org.junit.Test"
        val bodyOutput = mutableListOf<String>()
        // body begin
        bodyOutput.add("\n")
        bodyOutput.add("@get:Rule")
        bodyOutput.add("val composeTestRule = createAndroidComposeRule<${activity.name}>()")
        bodyOutput.enter()
        bodyOutput.add("companion object {")
        // add http web mock server when auto test needed
        if (model.isMockWebServerNeeded()) {
            imports["automatonEnvironment"] = "${appPackage}.AutomatonEnvironment"
            imports["mockWebServer"] = "${appPackage}.AutomatonMockWebServer"
            bodyOutput.add(tab() + "val mockWebServer = AutomatonEnvironment.mockWebServer")
        }
        bodyOutput.enter()

        // Before class fun
        imports["BeforeClass"] = "org.junit.BeforeClass"
        bodyOutput.add(tab() + "@JvmStatic")
        bodyOutput.add(tab() + "@BeforeClass")
        bodyOutput.add(tab() + "fun beforeClass() {")
        generateBeforeClassFunBody(model).forEach { bodyLine ->
            bodyOutput.add(tab(2) + bodyLine)
        }
        bodyOutput.add(tab() + "}")
        bodyOutput.enter()

        // after class fun
        imports["AfterClass"] = "org.junit.AfterClass"
        bodyOutput.add(tab() + "@JvmStatic")
        bodyOutput.add(tab() + "@AfterClass")
        bodyOutput.add(tab() + "fun afterClass() {")
        generateAfterClassFunBody(model).forEach { bodyLine ->
            bodyOutput.add(tab(2) + bodyLine)
        }
        bodyOutput.add(tab() + "}")
        bodyOutput.add("}")
        bodyOutput.enter()

        bodyOutput.add("@Test")
        // test fun
        bodyOutput.add("fun ${model.test.metadata.testName.camelCase()}() = before {")
        generateBeforeTestFunBody(imports, model).forEach { before ->
            bodyOutput.add(tab() + before)
        }
        bodyOutput.add("}.after {")
        generateAfterTestFunBody(imports, model).forEach { after ->
            bodyOutput.add(tab() + after)
        }
        bodyOutput.add("}.run {")
        // test fun body
        generateTestFunBody(imports, model).forEach { step ->
            bodyOutput.add(tab() + step)
        }
        bodyOutput.add("}")
        return bodyOutput
    }


    /**
     * generate class environment
     */
    private fun generateBeforeClassFunBody(semanticModel: SemanticModel): List<String> {
        val output = mutableListOf<String>()
        if (semanticModel.isMockWebServerNeeded()) {
            output.add("mockWebServer.start()")
        }

        return output.ifEmpty { listOf("// no operations") }
    }

    private fun generateAfterClassFunBody(semanticModel: SemanticModel): List<String> {
        val output = mutableListOf<String>()
        if (semanticModel.isMockWebServerNeeded()) {
            output.add("mockWebServer.stop()")
        }

        return output.ifEmpty { listOf("// no operations") }
    }

    /**
     * Setups environment for the whole test. Every setup section returns empty list when nothing to setup,
     * so emptiness of the block is checked by the result, not by the model
     */
    private fun generateBeforeTestFunBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val output = mutableListOf<String>()
        output.addAll(generateGeneralRequestsSetup(imports, model))
        // new setup sections (theme, permissions, etc.) must be added here
        return output.ifEmpty { listOf("// no before") }
    }

    /**
     * Resets environment after the test. AutomatonEnvironment is singleton, so its state would leak to the next tests
     */
    private fun generateAfterTestFunBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val output = mutableListOf<String>()
        output.addAll(generateGeneralRequestsReset(model))
        output.addAll(generateStepRequestsReset(model))
        // new reset sections (theme, permissions, etc.) must be added here
        return output.ifEmpty { listOf("// no after") }
    }

    private fun generateGeneralRequestsSetup(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val output = mutableListOf<String>()
        // general requests are subset of context requests, so mockWebServer is already declared
        if (model.test.generalRequests.isEmpty()) return output

        imports["mockRule"] = "${appPackage}.AutomatonMockRule"
        output.add("mockWebServer.setGeneralRules(")
        output.add(tab() + "setOf(")
        model.test.generalRequests.forEach { request ->
            request.toAutomatonMockRule().forEach { line ->
                output.add(tab(2) + line)
            }
        }
        output.add(tab() + ")")
        output.add(")")
        return output
    }

    private fun generateGeneralRequestsReset(model: SemanticModel): List<String> {
        if (model.test.generalRequests.isEmpty()) return emptyList()
        return listOf("mockWebServer.setGeneralRules(emptySet())")
    }

    private fun generateStepRequestsReset(model: SemanticModel): List<String> {
        if (model.test.steps.none { it.requests.isNotEmpty() }) return emptyList()
        return listOf("mockWebServer.setStepRules(emptySet())")
    }


    private fun generateTestFunBody(imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val output = mutableListOf<String>()
        model.test.steps.forEach { step ->
            output.add("step(\"${step.description}\") {")
            generateActionsAtStep(step, imports, model).forEach { actionLine ->
                output.add(tab() + actionLine)
            }
            output.add("}")
        }
        return output
    }

    private fun generateActionsAtStep(step: SemanticModel.Test.Step, imports: MutableMap<String, String>, model: SemanticModel): List<String> {
        val actions = mutableListOf<String>()
        imports["onNodeWithTag"] = "androidx.compose.ui.test.onNodeWithTag"
        step.actions.forEach { action ->
            when(action) {
                is SemanticModel.Test.Action.Assert -> {
                    actions.add("composeTestRule.onNodeWithTag(\"${action.element.name}\").${action.property.assertAction(imports)}")
                }
                is SemanticModel.Test.Action.Input -> {
                    imports["performTextInput"] = "androidx.compose.ui.test.performTextInput"
                    actions.add("composeTestRule.onNodeWithTag(\"${action.element.name}\").performTextInput(\"${action.text}\")")
                }
                is SemanticModel.Test.Action.Tap -> {
                    imports["performClick"] = "androidx.compose.ui.test.performClick"
                    actions.add("composeTestRule.onNodeWithTag(\"${action.element.name}\").performClick()")
                }
                is SemanticModel.Test.Action.Wait -> {
                    actions.add("flakySafely(timeoutMs = ${action.timeout.inWholeMilliseconds}) {")
                    actions.add(tab() + "composeTestRule.onNodeWithTag(\"${action.element.name}\").${action.property.assertAction(imports)}")
                    actions.add("}")
                }
            }
        }
        return actions
    }

    private fun generateTestClass(imports: MutableMap<String, String>, model: SemanticModel): String {
        val output = StringBuilder()

        imports["JunitRunWith"] = "org.junit.runner.RunWith"
        imports["Kaspresso"] = "com.kaspersky.kaspresso.kaspresso.Kaspresso"
        imports["AndroidJUnit4"] = "androidx.test.ext.junit.runners.AndroidJUnit4"
        imports["KaspressoTestCase"] = "com.kaspersky.kaspresso.testcases.api.testcase.TestCase"
        imports["withComposeSupport"] = "com.kaspersky.components.composesupport.config.withComposeSupport"
        // Class instant
        output.appendLine("@RunWith(AndroidJUnit4::class)")
        output.appendLine("class ${model.test.metadata.testName.camelCase()} : TestCase(")
        output.appendLine("${tab()}kaspressoBuilder = Kaspresso.Builder.withComposeSupport()")
        output.appendLine(") {")
        // Body instant
        generateClassBody(imports, model).forEach { bodyLine ->
            output.appendLine(tab() + bodyLine)
        }
        // End bracket
        output.appendLine("}")

        return output.toString()
    }

    private fun SemanticModel.Element.Property.assertAction(imports: MutableMap<String, String>): String {
        return when (this) {
            is SemanticModel.Element.Property.Visible -> if (this.value) {
                imports["assertIsDisplayed"] = "androidx.compose.ui.test.assertIsDisplayed"
                "assertIsDisplayed()"
            } else {
                imports["assertIsNotDisplayed"] = "androidx.compose.ui.test.assertIsNotDisplayed"
                "assertIsNotDisplayed()"
            }
        }
    }

}