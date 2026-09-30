package com.digitalsamurai.automaton.parser.api

import com.digitalsamurai.automaton.ast.AstData
import com.digitalsamurai.automaton.frontend.api.AutomatonRequest


interface AutomatonParser {

    fun parse(request: AutomatonRequest): AstData
}