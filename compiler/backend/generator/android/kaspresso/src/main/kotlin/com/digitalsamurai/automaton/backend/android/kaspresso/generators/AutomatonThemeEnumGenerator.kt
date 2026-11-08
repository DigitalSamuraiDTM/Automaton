package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.ClassGenerator
import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec

class AutomatonThemeEnumGenerator(val appPackage: String): ClassGenerator() {
    override fun generateClassFile(model: SemanticModel): GeneratorOutput.FileData {
        val themeEnum = TypeSpec.enumBuilder("AutomatonTheme")
            .addModifiers(KModifier.PUBLIC)
            .addKdoc(autoGenerateMessageForKotlinPoet())
            .addEnumConstant("LIGHT")
            .addEnumConstant("DARK")
            .addEnumConstant("SYSTEM")
            .build()

        val data = FileSpec.builder(appPackage, "AutomatonTheme")
            .addType(themeEnum)
            .build()

        return GeneratorOutput.FileData(
            name = data.name+".kt",
            data = data.toString(),
        )
    }
}