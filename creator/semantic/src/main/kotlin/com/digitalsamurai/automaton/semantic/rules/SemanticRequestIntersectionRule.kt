package com.digitalsamurai.automaton.semantic.rules

import com.digitalsamurai.automaton.semantic.AutomatonSemanticException
import com.digitalsamurai.automaton.semantic.SemanticModel

/**
 * Правило проверяет, что моки запросов в каждом шаге не пересекаются с общими моками
 * Пересечения общих моков и моков конкретного шага запрещены
 */
class SemanticRequestIntersectionRule {


    fun resolve(
        generalRequests: Set<SemanticModel.Request>,
        steps: List<SemanticModel.Test.Step>,
    ) {
        // TODO add method to the key when it will be supported by grammar
        val generalPaths = generalRequests.associateBy { it.url.path }
        steps.forEach { step ->
            step.requests.forEach { stepRequest ->
                val generalRequest = generalPaths[stepRequest.url.path] ?: return@forEach
                throw AutomatonSemanticException(
                    "Request '${stepRequest.name}' at step '${step.description}' mocks the same request " +
                            "'${stepRequest.url.path}' as general test request '${generalRequest.name}'"
                )
            }
        }
    }
}