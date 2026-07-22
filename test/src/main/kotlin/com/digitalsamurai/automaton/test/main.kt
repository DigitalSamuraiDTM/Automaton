package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.compiler.compiler
import com.digitalsamurai.automaton.frontend.yaml.YamlFrontend
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    val yamlFrontend = YamlFrontend()
    val compiler = Automaton.compiler.factory(
        frontend = yamlFrontend
    ){}

    yamlFrontend.compile()

//    val result = compiler.compile(YamlInput())

    println(result)



}