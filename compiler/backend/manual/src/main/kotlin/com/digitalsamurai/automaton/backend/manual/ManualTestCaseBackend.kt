package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru.RussianLanguageExtractor
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
    override fun buildOutput(tree: AstNode<*>): Result<BackendOutput> {
        val manualTestCase = buildText(tree)
        println(manualTestCase)
        val outputFile = File(outputDirectory,"example_test.md")
        outputFile.writeText(manualTestCase)
        return Result.success(BackendOutput(metaData = outputFile.path))
    }

    private fun buildText(tree: AstNode<*>): String {
        // TODO вернуться сюда после семантического анализа. Скорее всего после семантики будет использоваться другая насыщенная структура
        return tree.toManualTestCase("")
    }
    private fun AstNode<*>.toManualTestCase(prefix: String): String {

        val extractedText = languageExtractor.extractText(this)
        return if (this.childs.isEmpty()) {
            "${extractedText}\n"
        } else {
            var out = ""
            out += extractedText + "\n"
            childs.forEachIndexed {i, child ->
                out += if (i == childs.lastIndex) {
                    "${prefix}└── ${child.toManualTestCase("$prefix    ")}"
                } else {
                    "${prefix}├── ${child.toManualTestCase("$prefix│   ")}"
                }
            }
            out
        }
    }
}