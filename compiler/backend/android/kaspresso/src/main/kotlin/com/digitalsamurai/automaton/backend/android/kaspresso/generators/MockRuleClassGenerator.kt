package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.AutoGenerator
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

/**
 * Generates AutomatonMockRule: a single mocked response of the mock web server
 */
class MockRuleClassGenerator(val appPackage: String, val outputDir: File) : AutoGenerator() {

    private val headersType = MAP.parameterizedBy(STRING, STRING)

    private val constructor = FunSpec.constructorBuilder()
        .addParameter("path", String::class)
        .addParameter("method", String::class)
        .addParameter("code", Int::class)
        .addParameter("headers", headersType)
        .addParameter("body", String::class)
        .build()

    private val castFunStatement: String =
        """
        return MockResponse(
            code = code,
            headers = headers.toHeaders(),
            body = body,
        )
        """.trimIndent()
    private val castFunction = FunSpec.builder("toMockResponse")
        .addStatement(castFunStatement)
        .returns(ClassName("mockwebserver3", "MockResponse"))
        .build()

    private val clazz = TypeSpec.classBuilder("AutomatonMockRule")
        .addModifiers(KModifier.DATA)
        .addKdoc(autoGenerateMessageForKotlinPoet())
        .primaryConstructor(constructor)
        .addProperty(PropertySpec.builder("path", String::class).initializer("path").build())
        .addProperty(PropertySpec.builder("method", String::class).initializer("method").build())
        .addProperty(PropertySpec.builder("code", Int::class).initializer("code").build())
        .addProperty(PropertySpec.builder("headers", headersType).initializer("headers").build())
        .addProperty(PropertySpec.builder("body", String::class).initializer("body").build())
        .addFunction(castFunction)
        .build()

    override fun generate(model: SemanticModel) {
        FileSpec.builder(appPackage, "AutomatonMockRule")
            .addImport("mockwebserver3", "MockResponse")
            .addImport("okhttp3.Headers.Companion", "toHeaders")
            .addDefaultPackageImport(appPackage)
            .addType(clazz)
            .build().writeTo(outputDir)

    }
}