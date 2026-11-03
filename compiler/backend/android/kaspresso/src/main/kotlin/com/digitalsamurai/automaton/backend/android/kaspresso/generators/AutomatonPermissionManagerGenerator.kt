package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.AutoGenerator
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.*
import java.io.File

class AutomatonPermissionManagerGenerator(
    private val appPackage: String,
    private val outputDir: File,
) : AutoGenerator() {

    private val testContextType = ClassName("com.kaspersky.kaspresso.testcases.core.testcontext", "BaseTestContext")
    private val buildType = ClassName("android.os", "Build")
    private val packageManagerType = ClassName("android.content.pm", "PackageManager")
    private val instrumentationRegistryType = ClassName("androidx.test.platform.app", "InstrumentationRegistry")

    private val applyPermissionFunction = FunSpec.builder("applyPermission")
        .addKdoc(
            """
            Применяет требуемое состояние разрешения, если оно отличается от текущего.

            @param testContext контекст before-секции, предоставляющий device и adbServer.
            @param permission системное имя разрешения.
            @param isGranted требуемое состояние разрешения.
            @return Unit после применения состояния или без действия, если состояние уже совпадает.
            """.trimIndent()
        )
        .addParameter("testContext", testContextType)
        .addParameter("permission", String::class)
        .addParameter("isGranted", Boolean::class)
        .beginControlFlow("if (isGranted == isPermissionGranted(testContext, permission))")
        .addStatement("return")
        .endControlFlow()
        .beginControlFlow("if (%T.VERSION.SDK_INT >= %T.VERSION_CODES.P)", buildType, buildType)
        .beginControlFlow("if (isGranted)")
        .addStatement(
            "%T.getInstrumentation().uiAutomation.grantRuntimePermission(testContext.device.targetContext.packageName, permission)",
            instrumentationRegistryType,
        )
        .nextControlFlow("else")
        .addStatement(
            "%T.getInstrumentation().uiAutomation.revokeRuntimePermission(testContext.device.targetContext.packageName, permission)",
            instrumentationRegistryType,
        )
        .endControlFlow()
        .nextControlFlow("else")
        .addStatement("val operation = if (isGranted) %S else %S", "grant", "revoke")
        .addStatement(
            "testContext.adbServer.performShell(%S + operation + %S + testContext.device.targetContext.packageName + %S + permission)",
            "pm ",
            " ",
            " ",
        )
        .endControlFlow()
        .build()

    private val isPermissionGrantedFunction = FunSpec.builder("isPermissionGranted")
        .addModifiers(KModifier.PRIVATE)
        .addKdoc(
            """
            Проверяет текущее состояние разрешения приложения.

            @param testContext контекст before-секции с доступом к тестируемому приложению.
            @param permission системное имя разрешения.
            @return true, если разрешение уже выдано приложению.
            """.trimIndent()
        )
        .addParameter("testContext", testContextType)
        .addParameter("permission", String::class)
        .returns(Boolean::class)
        .addStatement(
            "return testContext.device.targetContext.checkSelfPermission(permission) == %T.PERMISSION_GRANTED",
            packageManagerType,
        )
        .build()

    override fun generate(model: SemanticModel) {
        val managerClass = TypeSpec.classBuilder("AutomatonPermissionManager")
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(autoGenerateMessageForKotlinPoet())
            .addFunction(applyPermissionFunction)
            .addFunction(isPermissionGrantedFunction)
            .build()

        FileSpec.builder(appPackage, "AutomatonPermissionManager")
            .addType(managerClass)
            .build()
            .writeTo(outputDir)
    }
}
