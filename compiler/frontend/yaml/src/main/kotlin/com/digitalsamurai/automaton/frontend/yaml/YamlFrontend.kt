package com.digitalsamurai.automaton.frontend.yaml

import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontends
import com.digitalsamurai.automaton.frontend.api.AutomatonRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID


public fun AutomatonFrontends.yaml(): YamlFrontend {
    return YamlFrontend()
}

public class YamlFrontend() : AutomatonFrontend {

    override val lexicalAnalyzer: YamlLexicalAnalyzer = YamlLexicalAnalyzer()

    override val tokensFlow: MutableSharedFlow<AutomatonRequest> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.SUSPEND)

    suspend fun compile(input: YamlInput) {
        val decodedData = withContext(Dispatchers.IO) {
            val inputStream = input.file.inputStream()
            lexicalAnalyzer.analyze(inputStream)
        }
        val request = AutomatonRequest(
            requestId = UUID.randomUUID().toString(),
            tokens = decodedData,
        )
        tokensFlow.emit(request)
    }
}

data class YamlInput(val file: File)