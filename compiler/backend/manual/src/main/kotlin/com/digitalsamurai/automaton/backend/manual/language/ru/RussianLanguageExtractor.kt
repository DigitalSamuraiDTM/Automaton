package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru

import com.digitalsamurai.automaton.ast.AstNode
import com.digitalsamurai.automaton.ast.NonTerminalNode
import com.digitalsamurai.automaton.ast.TerminalNode
import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.grammar.Action
import com.digitalsamurai.automaton.grammar.Actions
import com.digitalsamurai.automaton.grammar.Assert
import com.digitalsamurai.automaton.grammar.CompilationUnit
import com.digitalsamurai.automaton.grammar.Condition
import com.digitalsamurai.automaton.grammar.Context
import com.digitalsamurai.automaton.grammar.Delay
import com.digitalsamurai.automaton.grammar.Duration
import com.digitalsamurai.automaton.grammar.Element
import com.digitalsamurai.automaton.grammar.ElementProperty
import com.digitalsamurai.automaton.grammar.Id
import com.digitalsamurai.automaton.grammar.Input
import com.digitalsamurai.automaton.grammar.InputData
import com.digitalsamurai.automaton.grammar.Metadata
import com.digitalsamurai.automaton.grammar.TestName
import com.digitalsamurai.automaton.grammar.NonTerminal
import com.digitalsamurai.automaton.grammar.PropertyValue
import com.digitalsamurai.automaton.grammar.Step
import com.digitalsamurai.automaton.grammar.Steps
import com.digitalsamurai.automaton.grammar.Tap
import com.digitalsamurai.automaton.grammar.TapType
import com.digitalsamurai.automaton.grammar.Terminal
import com.digitalsamurai.automaton.grammar.Test
import com.digitalsamurai.automaton.grammar.Timeout
import com.digitalsamurai.automaton.grammar.Wait

class RussianLanguageExtractor : LanguageExtractor {
    override fun extractText(node: AstNode): String {
        return when(node) {
            is NonTerminalNode -> node.text()
            is TerminalNode<*> -> node.text()
        }
    }

    private fun NonTerminalNode.text(): String {
        return when(this.symbol) {
            Action -> "Действие"
            Actions -> "Действия"
            Assert -> "Проверь"
            CompilationUnit -> "Юнит"
            Condition -> "Условие"
            Context -> "Контекст"
            Delay -> "Задержка"
            Input -> "Введи"
            Metadata -> "Информация"
            Step -> "Шаг"
            Steps -> "Шаги:"
            Tap -> "Тапни"
            Test -> "Тест-кейс"
            Wait -> "Подожди"
        }
    }

    private fun TerminalNode<*>.text(): String {
        return when(this.symbol) {
            Duration -> "пук пук пук"
            Element -> "элемент экрана '${this.value}'"
            ElementProperty -> "пук пук пук"
            Id -> "пук пук пук"
            InputData -> "пук пук пук"
            TestName -> "пук пук пук"
            PropertyValue -> "пук пук пук"
            TapType -> "пук пук пук"
            Timeout -> "пук пук пук"
        }
    }

}