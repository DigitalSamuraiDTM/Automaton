package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.grammar.*

class RussianLanguageExtractor : LanguageExtractor {
    override fun extractText(node: AstNode<*>): String {
        (node.symbol as? NonTerminal)?.let {
            return it.text()
        }
        (node.symbol as? Terminal<*>)?.let {
            return it.text(node.value)
        }
        throw IllegalArgumentException("Unknown GrammarSymbol type: ${node.symbol}")
//        return ""
    }

    private fun NonTerminal.text(): String {
        return when (this) {
            Action -> ""
            Actions -> "Действия"
            Assert -> "Ожидаемый результат"
            CompilationUnit -> "Юнит"
            Condition -> "Условие"
            Context -> "Контекст"
            Delay -> "Задержка"
            Input -> "Введи текст"
            Metadata -> "Информация о тесте"
            Step -> "Шаг"
            Steps -> "Шаги"
            Tap -> "Тапни по"
            Test -> "Тест-кейс"
            Wait -> "Подожди"
            Elements -> "Элементы"
            Permission -> "Разрешение"
            Permissions -> "Разрешения"
            Theme -> "Тема"
            Element -> "Элемент экрана"
        }
    }

    private fun Terminal<*>.text(value: Any?): String {
        return when(this) {
            Duration -> "Длительность '${value}'"
            Element -> "Элемент экрана '${value}'"
            ElementProperty -> "Свойство '${value}'"
            Id -> "Идентификатор '${value}'"
            InputData -> "текст '${value}'"
            PropertyValue -> "Значение свойства '${value}'"
            TapType -> "Тип тапа '${value}'"
            TestName -> "Имя '${value}'"
            Timeout -> "таймаут '${value}'"
            Camera -> "Камера '${value}'"
            Microphone -> "Микрофон '${value}'"
            ThemeMode -> "Мод '${value}'"
            Element.Name -> "Название '${value}'"
            Element.Layout -> "Слой"
        }
    }

}