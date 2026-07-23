package com.digitalsamurai.automaton.frontend.yaml

import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontends
import com.digitalsamurai.automaton.grammar.Token
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import org.yaml.snakeyaml.Yaml
import java.io.File


public fun AutomatonFrontends.yaml(): YamlFrontend {
    return YamlFrontend()
}

public class YamlFrontend() : AutomatonFrontend {

    private val yaml = Yaml()

    override val lexicalAnalyzer: YamlLexicalAnalyzer = YamlLexicalAnalyzer()

    override val tokensFlow: MutableSharedFlow<Sequence<Token<*>>> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.SUSPEND)

    suspend fun compile(input: YamlInput) {
        val inputStream = input.file.inputStream()
        val decodedFile = yaml.load<Map<String, Any>>(inputStream)
        val tokens = lexicalAnalyzer.analyze(decodedFile)
        tokensFlow.emit(tokens)
    }
}

data class YamlInput(val file: File)