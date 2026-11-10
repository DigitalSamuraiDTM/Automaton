package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru

import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.semantic.SemanticModel

class RussianLanguageExtractor : LanguageExtractor {

    override fun extractText(model: SemanticModel): String {
        val title =
            "# Тест-кейс № ${model.test.metadata.id}\n" +
                    "Название теста: '${model.test.metadata.testName}'\n" +
                    "## Описание\n" +
                    "### Используемые разрешения:\n" +
                    model.context.permissions.joinToString(separator = ""){ "* " + it.extractPermissionName() + "\n" } +
                    "### Тема приложения:\n" +
                    "* ${model.context.theme.extractTheme()}\n"



        var steps = "## Шаги действий\n"
        model.test.steps.forEachIndexed { index, step ->
            steps += ("#### Шаг ${index}. ${step.description}\n" + step.actions.joinToString(separator = "") { "* "+it.extractAction()+"\n" })
        }

        return title + steps
    }

    private fun SemanticModel.Permission.extractPermissionName(): String {
        val name = when(this) {
            is SemanticModel.Permission.Camera -> "Доступ к камере"
            is SemanticModel.Permission.Microphone -> "Доступ к микрофону"
        }
        return name + ": "+ if (this.isGranted) "**ДА**" else "НЕТ"
    }

    private fun SemanticModel.Test.Action.extractAction(): String {
        return when (this) {
            is SemanticModel.Test.Action.Assert -> "Проверь элемент ${element.testCaseText()}. ${property.extractAssertProperty()}"
            is SemanticModel.Test.Action.Input -> "Введи текст '${text}' в ${element.testCaseText()}"
            is SemanticModel.Test.Action.Tap -> "${type.extractTapType()}${element.testCaseText()}"
            is SemanticModel.Test.Action.Wait -> "Подожди пока элемент ${element.testCaseText()} ${property.extractWaitProperty()}"
        }
    }

    private fun SemanticModel.Test.Action.Tap.Type.extractTapType(): String {
        return when (this) {
            SemanticModel.Test.Action.Tap.Type.SINGLE -> "Кликни по "
            SemanticModel.Test.Action.Tap.Type.DOUBLE -> "Сделай двойной клик по "
            SemanticModel.Test.Action.Tap.Type.LONG -> "Удерживай "
        }
    }

    private fun SemanticModel.Element.Property.extractAssertProperty(): String {
        return when(this) {
            is SemanticModel.Element.Property.Visible -> if (this.value) "Он должен быть видимым" else "Его не должно быть видно"
        }
    }

    private fun SemanticModel.Element.Property.extractWaitProperty(): String {
        return when(this) {
            is SemanticModel.Element.Property.Visible -> if (value) "появится" else "не исчезнет"
        }
    }

    private fun SemanticModel.Context.Theme?.extractTheme(): String {
        return when(this) {
            SemanticModel.Context.Theme.LIGHT -> "**Светлая**"
            SemanticModel.Context.Theme.DARK -> "**Темная**"
            SemanticModel.Context.Theme.SYSTEM -> "**Системная**"
            null -> "Не задана"
        }
    }

    private fun SemanticModel.Element.testCaseText(): String {
        return "[${name}](${layout})"
    }

}