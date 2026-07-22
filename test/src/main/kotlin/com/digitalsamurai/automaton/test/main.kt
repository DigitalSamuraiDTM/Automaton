package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.compiler.compiler
import com.digitalsamurai.automaton.frontend.yaml.YamlFrontend
import com.digitalsamurai.automaton.frontend.yaml.YamlInput
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(): Unit = runBlocking {

    val yamlFrontend = YamlFrontend()
    val compiler = Automaton.compiler.factory(
        frontend = yamlFrontend
    ){}

    yamlFrontend.compile(YamlInput(File("")))

//    val result = compiler.compile(YamlInput())




}