package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.AutoGenerator
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.*
import java.io.File

class EnvironmentGenerator(
    val appPackage: String,
    val outputDir: File,
): AutoGenerator() {

    private val mockRuleClassGenerator = MockRuleClassGenerator(appPackage, outputDir)
    private val mockWebServerGenerator = MockWebServerGenerator(appPackage, outputDir)

    private val mockWebServerClassName = ClassName(appPackage, "AutomatonMockWebServer")

    private val mockWebServerProperty = PropertySpec.builder("mockWebServer", mockWebServerClassName)
        .addModifiers(KModifier.PUBLIC)
        .initializer("%T()", mockWebServerClassName)
        .build()

    override fun generate(model: SemanticModel) {
        generateEnvironmentHolderClass()
        mockRuleClassGenerator.generate(model)
        mockWebServerGenerator.generate(model)
    }

    private fun generateEnvironmentHolderClass() {
        val environmentObject = TypeSpec.objectBuilder("AutomatonEnvironment")
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(autoGenerateMessageForKotlinPoet())
            .addProperty(mockWebServerProperty)
            .build()

        FileSpec.builder(appPackage, "AutomatonEnvironment")
            .addType(environmentObject)
            .build()
            .writeTo(outputDir)
    }
}
