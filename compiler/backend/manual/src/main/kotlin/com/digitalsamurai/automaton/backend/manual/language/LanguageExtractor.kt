package com.digitalsamurai.automaton.com.digitalsamurai.automaton.backend.manual.language

import com.digitalsamurai.automaton.semantic.SemanticModel

interface LanguageExtractor {

    fun extractText(model: SemanticModel): String
}
