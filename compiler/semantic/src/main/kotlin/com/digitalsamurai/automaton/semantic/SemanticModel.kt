package com.digitalsamurai.automaton.semantic

import com.digitalsamurai.automaton.semantic.SemanticModel.Test.Theme
import kotlin.time.Duration

/**
 * Intermediate representation based on AST semantic analyze.
 */
data class SemanticModel(
    val metadata: Metadata,
    val context: Context,
    val test: Test,
) {

    data class Test(
        val steps: List<Step>,
    ) {
        enum class Theme {
            LIGHT, DARK, SYSTEM,
        }

        data class Step(
            val actions: List<String>,
        )
    }

    data class Context(
        val theme: Theme?,
        val permissions: Map<String, Boolean>,
        val elements: Map<String, Element>
    ) {

    }
    data class Metadata(
        val testName: String,
    )

    data class Element(
        val name: String,
    ) {
        sealed class Property {
            data class Visible(
                val value: Boolean,
            )
        }
    }

    sealed interface Action {

        val element: Element

        data class Tap(
            override val element: Element,
            val type: Type,
        ): Action {
            enum class Type {
                SINGLE, DOUBLE, LONG
            }
        }

        data class Wait(
            override val element: Element,
            val property: Element.Property,
            val timeout: Duration,
        ): Action

        data class Input(
            override val element: Element,
            val text: String,
        ): Action

        data class Assert(
            override val element: Element,
            val property: Element.Property,
        ): Action
    }
}