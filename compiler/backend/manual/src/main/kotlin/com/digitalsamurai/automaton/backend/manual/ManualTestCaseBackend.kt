package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.backend.api.AutomatonBackend
import com.digitalsamurai.automaton.backend.api.BackendOutput
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.Language
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru.RussianLanguageExtractor

public class ManualTestCaseBackend(
    private val language: Language,
): AutomatonBackend {

    private val languageExtractor: LanguageExtractor = when(language) {
        Language.RU -> RussianLanguageExtractor()
    }
    override fun buildOutput(tree: AstNode<*>): Result<BackendOutput> {

        val finalText = buildText(tree)
        println(finalText)

        return Result.success(BackendOutput("OBAMA"))
    }

    private fun buildText(tree: AstNode<*>): String {
        // TODO вернуться сюда после семантического анализа. Скорее всего после семантики будет использоваться другая насыщенная структура
//        var stepIterator = 0
//        var out = languageExtractor.extractText(tree)
//        println(out)
//        out = String.format(out,tree.childs.map { buildText(it) })
//        return out
//        return when(tree) {
//            is NonTerminalNode -> {
//                var outText = ""
//                outText += languageExtractor.extractText(tree)
//                tree.children.forEach { child ->
//                    outText += buildText(child)
//                }
//                outText
//            }
//            is TerminalNode<*> -> languageExtractor.extractText(tree)
//        }
        return "OBAMA"
    }
}