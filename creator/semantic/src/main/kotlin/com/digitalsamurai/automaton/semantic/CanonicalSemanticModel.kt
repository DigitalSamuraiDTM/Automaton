package com.digitalsamurai.automaton.semantic

import com.digitalsamurai.automaton.semantic.SemanticModel.Context
import com.digitalsamurai.automaton.semantic.SemanticModel.Test
import com.digitalsamurai.automaton.utils.Fingerprinted

/**
 * Canonical semantic model for fingerprint SHA-256 calculation with rules:
 * * NO unused context elements
 * * Ordered context elements
 * * Ordered map keys
 * * Ordered all unordered set
 * * UTF-8 encode data
 * * JSON data ordering
 */
@ConsistentCopyVisibility
public data class CanonicalSemanticModel private constructor(
    val context: Context,
    val test: Test,
    val canonicalSchemaVersion: String,
): Fingerprinted {

    companion object {

        private const val SCHEMA_VERSION = "1.0.0"

        /**
         * Extract canonical semantic model from generated semantic model
         */
        fun fromSemanticModel(semanticModel: SemanticModel): CanonicalSemanticModel {

            val testElements = semanticModel.test.steps.flatMap { step -> step.actions.map { action -> action.element } }
            val canonicalElements = semanticModel.context.elements
                .intersect(testElements.toSet())
                .sortedBy { it.name }
            val testRequests = semanticModel.test.generalRequests.union(semanticModel.test.steps.flatMap { it.requests })
            val canonicalRequests = semanticModel.context.requests
                .intersect(testRequests)
                .sortedBy { it.name }
            val canonicalPermissions = semanticModel.context.permissions.sortedBy { it::class.java.canonicalName }

            return CanonicalSemanticModel(
                context = Context(
                    theme = semanticModel.context.theme,
                    permissions = canonicalPermissions,
                    elements = canonicalElements,
                    requests = canonicalRequests,
                ),
                test = semanticModel.test,
                canonicalSchemaVersion = SCHEMA_VERSION,
            )
        }
    }

    override fun toByteArray(): ByteArray {
        return this.canonicalSchemaVersion.toByteArray() +
                this.test.toString().toByteArray() +
                this.context.toString().toByteArray() +
                this.canonicalSchemaVersion.toByteArray()
    }
}

public fun SemanticModel.canonical(): CanonicalSemanticModel {
    return CanonicalSemanticModel.fromSemanticModel(this)
}