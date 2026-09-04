package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.grammar.*

class RussianLanguageExtractor : LanguageExtractor {
    override fun extractText(node: AstNode<*>): String {
        (node.symbol as? NonTerminal)?.let {
            return it.text()
        }

        return "терминал"
//        return ""
    }

    private fun NonTerminal.text(): String {
        return when (this) {
            Action -> "Действие"
            Actions -> $$"Последовательность действий \n %1$s"
            Assert -> $$"Ожидаемый результат: %1$s"
            CompilationUnit -> "Юнит %1\$s"
            Condition -> ""
            Context -> "Контекст"
            Delay -> "Задержка"
            Input -> $$"Введи текст '%2$s' в  элемент %1$s \n"
            Metadata -> "Информация о тесте\n"
            Step -> "Шаг"
            Steps -> "Шаги:"
            Tap -> $$"Тапни по элементу %1$s"
            Test -> "Тест-кейс"
            Wait -> $$"Подожди %2$s %1$s"
        }
    }

    private fun Terminal<*>.text(value: Any?): String {
        return "терминал"
    }

}