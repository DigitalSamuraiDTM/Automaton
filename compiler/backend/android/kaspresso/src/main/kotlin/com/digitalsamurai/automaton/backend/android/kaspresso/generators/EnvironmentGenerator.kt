package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.AutoGenerator
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.*
import java.io.File

class EnvironmentGenerator(
    val appPackage: String,
    val outputDir: File,
    val mockServerHost: String,
    val mockServerPort: Int,
) : AutoGenerator() {

    private val mockRuleClassGenerator = MockRuleClassGenerator(appPackage, outputDir)
    private val mockWebServerGenerator = MockWebServerGenerator(
        appPackage = appPackage,
        outputDir = outputDir,
        host = mockServerHost,
        port = mockServerPort,
    )
    private val permissionManagerGenerator = AutomatonPermissionManagerGenerator(appPackage, outputDir)
    private val themeManagerGenerator = AutomatonThemeManagerGenerator(appPackage, outputDir)

    private val mockWebServerClassName = ClassName(appPackage, "AutomatonMockWebServer")
    private val permissionManagerClassName = ClassName(appPackage, "AutomatonPermissionManager")
    private val themeManagerClassName = ClassName(appPackage, "AutomatonThemeManager")

    private val mockWebServerProperty = PropertySpec.builder("mockWebServer", mockWebServerClassName)
        .addModifiers(KModifier.PUBLIC)
        .initializer("%T()", mockWebServerClassName)
        .build()
    private val permissionManagerProperty = PropertySpec.builder("permissionManager", permissionManagerClassName)
        .addModifiers(KModifier.PUBLIC)
        .initializer("%T()", permissionManagerClassName)
        .build()
    private val themeManagerProperty = PropertySpec.builder("themeManager", themeManagerClassName)
        .addModifiers(KModifier.PUBLIC)
        .initializer("%T()", themeManagerClassName)
        .build()

    override fun generate(model: SemanticModel) {
        generateEnvironmentHolderClass()
        mockRuleClassGenerator.generate(model)
        mockWebServerGenerator.generate(model)
        permissionManagerGenerator.generate(model)
        themeManagerGenerator.generate(model)
    }

    private fun generateEnvironmentHolderClass() {
        val environmentObject = TypeSpec.objectBuilder("AutomatonEnvironment")
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(autoGenerateMessageForKotlinPoet())
            .addProperty(mockWebServerProperty)
            .addProperty(permissionManagerProperty)
            .addProperty(themeManagerProperty)
            .build()

        FileSpec.builder(appPackage, "AutomatonEnvironment")
            .addType(environmentObject)
            .build()
            .writeTo(outputDir)
    }
}
