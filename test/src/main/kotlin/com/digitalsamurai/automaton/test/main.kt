package com.digitalsamurai.automaton.test

import com.digitalsamurai.automaton.api.Automaton
import com.digitalsamurai.automaton.compiler.compiler
import com.digitalsamurai.automaton.frontend.yaml.YamlFrontend
import com.digitalsamurai.automaton.frontend.yaml.YamlInput
import com.digitalsamurai.automaton.parser.ll1.LL1Parser
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(): Unit = runBlocking {

    val yamlFrontend = YamlFrontend()
//    val compiler = Automaton.compiler.factory(
//        frontend = yamlFrontend
//    ){}
//
//    yamlFrontend.compile(YamlInput(File("/Users/nugaev.andrey/Desktop/Projects/Automaton/examples/tree_example.yaml")))
//
//    delay(100000L)


    LL1Parser.initialize()

    print(LL1Parser.toString())

}