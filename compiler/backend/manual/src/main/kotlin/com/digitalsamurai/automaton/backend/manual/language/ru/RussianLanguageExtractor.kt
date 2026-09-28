package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.ru

import com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language.LanguageExtractor
import com.digitalsamurai.automaton.semantic.SemanticModel

class RussianLanguageExtractor : LanguageExtractor {

    override fun extractText(model: SemanticModel): String {
        val title =
            "# Тест-кейс № ${model.test.metadata.id}\n" +
                    "Название теста: '${model.test.metadata.testName}'\n" +
                    "## Контекст\n" +
                    "### Используемые разрешения:\n" +
                    model.context.permissions.joinToString(separator = ""){ "* " + it.extractPermissionName() + "\n" } +
                    "## Шаги действий\n"

        var steps = ""
        model.test.steps.forEachIndexed { index, step ->
            steps += ("#### Шаг ${index}.\n" + step.actions.joinToString(separator = "") { "* "+it.extractAction()+"\n" })
        }

        return title + steps
    }

    private fun SemanticModel.Permission.extractPermissionName(): String {
        return this.name + ": "+ if (this.isGranted) "ДА" else "НЕТ"
    }

    private fun SemanticModel.Test.Action.extractAction(): String {
        return when (this) {
            is SemanticModel.Test.Action.Assert -> "Проверь элемент '${element.name}'. ${property.extractAssertProperty()}"
            is SemanticModel.Test.Action.Input -> "Введи текст '${this.text}' в ${element.name}"
            is SemanticModel.Test.Action.Tap -> "${type.extractTapType()}${element.name}"
            is SemanticModel.Test.Action.Wait -> "Подожди пока элемент '${element.name}' ${property.extractWaitProperty()}"
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

}