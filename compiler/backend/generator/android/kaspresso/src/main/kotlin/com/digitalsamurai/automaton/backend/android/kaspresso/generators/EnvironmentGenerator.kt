package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.ClassGenerator
import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.*

class EnvironmentGenerator(
    val appPackage: String,
    val mockServerHost: String,
    val mockServerPort: Int,
) : ClassGenerator() {

    private val mockRuleClassGenerator = MockRuleClassGenerator(appPackage)
    private val mockWebServerGenerator = MockWebServerGenerator(
        appPackage = appPackage,
        host = mockServerHost,
        port = mockServerPort,
    )
    private val permissionManagerGenerator = AutomatonPermissionManagerGenerator(appPackage)
    private val themeManagerGenerator = AutomatonThemeManagerGenerator(appPackage)
    private val themeEnumGenerator = AutomatonThemeEnumGenerator(appPackage)

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

    fun generateAllEnvironment(model: SemanticModel): List<GeneratorOutput.FileData> = buildList {
        add(generateClassFile(model))
        add(mockRuleClassGenerator.generateClassFile(model))
        add(mockWebServerGenerator.generateClassFile(model))
        add(permissionManagerGenerator.generateClassFile(model))
        add(themeManagerGenerator.generateClassFile(model))
        add(themeEnumGenerator.generateClassFile(model))
    }

    override fun generateClassFile(model: SemanticModel): GeneratorOutput.FileData {
        val environmentObject = TypeSpec.objectBuilder("AutomatonEnvironment")
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(autoGenerateMessageForKotlinPoet())
            .addProperty(mockWebServerProperty)
            .addProperty(permissionManagerProperty)
            .addProperty(themeManagerProperty)
            .build()

        val data = FileSpec.builder(appPackage, "AutomatonEnvironment")
            .addType(environmentObject)
            .build()

        return GeneratorOutput.FileData(
            name = data.name + ".kt",
            data = data.toString(),
        )
    }
}
