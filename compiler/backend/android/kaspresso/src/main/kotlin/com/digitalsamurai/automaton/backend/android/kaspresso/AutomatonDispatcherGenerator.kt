package com.digitalsamurai.automaton.backend.android.kaspresso

import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel

class AutomatonDispatcherGenerator(): AutoGenerator() {


    fun generateInner(imports: MutableMap<String, String>): List<String> {
        val classList = mutableListOf<String>()
        imports["Dispatcher"] = "mockwebserver3.Dispatcher"
        classList.add("inner class AutomatonDispatcher(): Dispatcher() {")
        generateClassBody(imports).forEach { classBody ->
            classList.add(tab()+ classBody)
        }
        classList.add("}")
        return classList
    }


    private fun generateClassBody(imports: MutableMap<String, String>): List<String> {
        val classBody = mutableListOf<String>()
        imports["mockwebserver3.RecordedRequest"] = "mockwebserver3.RecordedRequest"
        imports["mockwebserver3.MockResponse"] = "mockwebserver3.MockResponse"
        classBody.add("override fun dispatch(request: RecordedRequest): MockResponse {")
        generateDispatchFunction(imports).forEach { funBody ->
            classBody.add(tab() + funBody)
        }
        classBody.add("}")
        return classBody
    }

    private fun generateDispatchFunction(imports: MutableMap<String, String>): List<String> {
        val funBody = mutableListOf<String>()
        funBody.add("val mockRules = activeRules.filter { it.method == request.method && it.path == request.url.encodedPath }")
        funBody.add("check(mockRules.isEmpty()) { return MockResponse(code = 404, body = \"No active Automaton mock\") }")
        funBody.add("val mockRule = mockRules.single()")
        funBody.add("return mockRule.toMockResponse()")
        return funBody
    }


    override fun generate(model: SemanticModel) {
        // no-op now
    }
}