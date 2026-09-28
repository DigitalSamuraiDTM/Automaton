package com.digitalsamurai.automaton.semantic

import kotlin.time.Duration

/**
 * Intermediate representation based on AST semantic analyze.
 */
data class SemanticModel(
    val context: Context,
    val test: Test,
) {

    data class Test(
        val metadata: Metadata,
        val steps: List<Step>,
    ) {
        data class Metadata(
            val testName: String,
            val id: String,
        )

        data class Step(
            val description: String,
            val actions: List<Action>,
        )
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

    data class Context(
        val theme: Theme?,
        val permissions: List<Permission>,
        val elements: List<Element>,
    ) {
        enum class Theme {
            LIGHT, DARK, SYSTEM,
        }
    }

    sealed interface Permission {
        val isGranted: Boolean

        data class Camera(override val isGranted: Boolean): Permission
        data class Microphone(override val isGranted: Boolean): Permission
    }

    data class Element(
        val name: String,
        val layout: String,
    ) {
        sealed class Property {
            data class Visible(
                val value: Boolean,
            ): Property()
        }
    }
}