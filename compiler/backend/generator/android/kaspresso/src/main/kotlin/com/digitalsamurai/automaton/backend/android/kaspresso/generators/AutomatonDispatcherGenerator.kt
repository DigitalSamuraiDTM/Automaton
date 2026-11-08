package com.digitalsamurai.automaton.backend.android.kaspresso.generators

import com.digitalsamurai.automaton.backend.android.kaspresso.ClassGenerator
import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.backend.api.tab
import com.digitalsamurai.automaton.semantic.SemanticModel

class AutomatonDispatcherGenerator(): ClassGenerator() {


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
        // general and step rules never intersect, it is guaranteed by semantic analyzer
        funBody.add("val mockRule = activeRules.get().rules.firstOrNull { it.method == request.method && it.path == request.url.encodedPath }")
        funBody.add(tab() + "?: return MockResponse(code = 404, body = \"No active Automaton mock\")")
        funBody.add("return mockRule.toMockResponse()")
        return funBody
    }


    override fun generateClassFile(model: SemanticModel): GeneratorOutput.FileData {
        TODO("no operation now")
    }
}