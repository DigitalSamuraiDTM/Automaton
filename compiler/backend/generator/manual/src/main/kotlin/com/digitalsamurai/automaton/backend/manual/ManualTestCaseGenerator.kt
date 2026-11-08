package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual

import com.digitalsamurai.automaton.backend.api.AutomatonGenerator
import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.backend.api.camelCase
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru.RussianLanguageExtractor
import com.digitalsamurai.automaton.semantic.SemanticModel
import kotlinx.coroutines.coroutineScope

public class ManualTestCaseGenerator(
    private val language: Language,
) : AutomatonGenerator {

    private val languageExtractor: LanguageExtractor = when (language) {
        Language.RU -> RussianLanguageExtractor()
    }

    override suspend fun generate(model: SemanticModel): GeneratorOutput = coroutineScope {
        val manualTestCase = languageExtractor.extractText(model)
        return@coroutineScope GeneratorOutput(
            requestId = model.requestId,
            metaData = model.test.metadata.id,
            outputFiles = listOf(
                GeneratorOutput.FileData(
                    name = model.test.metadata.testName.camelCase()+".md",
                    data = manualTestCase,
                )
            ),
        )
    }

}