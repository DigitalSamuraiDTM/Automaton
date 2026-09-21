package com.digitalsamurai.automaton.semantic

class SemanticModelFactory {


    //
    private var metadata: SemanticModel.Metadata? = null


    fun setMetadata(metadata: SemanticModel.Metadata) {
        this.metadata = metadata
    }


    fun contextScope(contextScope: ContextFactory.() -> Unit) {

    }

    class ContextFactory private constructor() {
        private var elements: MutableList<SemanticModel.Element> = mutableListOf()
        var theme: SemanticModel.Test.Theme? = null
        private var permissions: MutableMap<String, Boolean> = mutableMapOf()

        fun addElement(element: SemanticModel.Element) {
            // TODO check if element exist and warn about it
            elements.add(element)
        }

        fun addPermission(permission: String, isGranted: Boolean) {
            // TODO check if permission exist and warn about it
            permissions[permission] = isGranted
        }
    }
}