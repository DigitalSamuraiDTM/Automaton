package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.compiler.compiler
import com.digitalsamurai.automaton.frontend.yaml.YamlInput
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {

    val compiler = Automaton.compiler.factory<YamlInput> {

    }

    val result = compiler.compile(YamlInput())

    println(result)



}