package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.ClassGenerator
import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.*
import java.io.File

class AutomatonThemeManagerGenerator(
    private val appPackage: String,
) : ClassGenerator() {

    private val appCompatDelegateType = ClassName("androidx.appcompat.app", "AppCompatDelegate")
    private val themeType = ClassName(appPackage, "AutomatonTheme")
    private val nullableIntType = Int::class.asTypeName().copy(nullable = true)

    private val originalNightModeProperty = PropertySpec.builder("originalNightMode", nullableIntType)
        .addModifiers(KModifier.PRIVATE)
        .mutable()
        .initializer("null")
        .build()

    private val applyThemeFunction = FunSpec.builder("applyTheme")
        .addKdoc(
            """
            Сохраняет исходный night mode и применяет тему конкретного теста к приложению.

            @param theme тема, заданная в семантической модели теста.
            @return Unit после сохранения исходного режима и применения заданной темы приложения.
            """.trimIndent()
        )
        .addParameter("theme", themeType)
        .beginControlFlow("if (originalNightMode == null)")
        .addStatement("originalNightMode = %T.getDefaultNightMode()", appCompatDelegateType)
        .endControlFlow()
        .beginControlFlow("val nightMode = when (theme)")
        .addStatement("%T.LIGHT -> %T.MODE_NIGHT_NO", themeType, appCompatDelegateType)
        .addStatement("%T.DARK -> %T.MODE_NIGHT_YES", themeType, appCompatDelegateType)
        .addStatement("%T.SYSTEM -> %T.MODE_NIGHT_FOLLOW_SYSTEM", themeType, appCompatDelegateType)
        .endControlFlow()
        .addStatement("%T.setDefaultNightMode(nightMode)", appCompatDelegateType)
        .build()

    private val restoreThemeFunction = FunSpec.builder("restoreTheme")
        .addKdoc(
            """
            Восстанавливает исходный night mode приложения и очищает сохранённое значение.

            @return Unit после восстановления режима или без действия, если исходный режим не сохранён.
            """.trimIndent()
        )
        .addStatement("val nightMode = originalNightMode ?: return")
        .addStatement("%T.setDefaultNightMode(nightMode)", appCompatDelegateType)
        .addStatement("originalNightMode = null")
        .build()


    override fun generateClassFile(model: SemanticModel): GeneratorOutput.FileData {
        val managerClass = TypeSpec.classBuilder("AutomatonThemeManager")
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(autoGenerateMessageForKotlinPoet())
            .addProperty(originalNightModeProperty)
            .addFunction(applyThemeFunction)
            .addFunction(restoreThemeFunction)
            .build()

        val data = FileSpec.builder(appPackage, "AutomatonThemeManager")
            .addType(managerClass)
            .build()

        return GeneratorOutput.FileData(
            name = data.name + ".kt",
            data = data.toString()
        )
    }
}
