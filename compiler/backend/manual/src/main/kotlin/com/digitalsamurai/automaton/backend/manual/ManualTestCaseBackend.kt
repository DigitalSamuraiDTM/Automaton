package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual

import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru.RussianLanguageExtractor
import com.digitalsamurai.automaton.semantic.SemanticModel
import java.io.File

public class ManualTestCaseBackend(
    private val language: Language,
    private val outputDirectory: File,
): AutomatonBackend {

    init {
        if (!outputDirectory.exists()) throw IllegalArgumentException("Output directory '${outputDirectory.path}' does not exist")
        if (!outputDirectory.isDirectory) throw IllegalArgumentException("Output directory '${outputDirectory.path}' is not a directory")
    }

    private val languageExtractor: LanguageExtractor = when(language) {
        Language.RU -> RussianLanguageExtractor()
    }
    override fun buildOutput(tree: SemanticModel): Result<BackendOutput> {
        val manualTestCase = buildText(tree)
        println(manualTestCase)
        val outputFile = File(outputDirectory,"example_test.md")
        outputFile.writeText(manualTestCase)
        return Result.success(BackendOutput(metaData = outputFile.path))
    }

    private fun buildText(tree: SemanticModel): String {
        // TODO вернуться сюда после семантического анализа. Скорее всего после семантики будет использоваться другая насыщенная структура
        return ""
    }
}