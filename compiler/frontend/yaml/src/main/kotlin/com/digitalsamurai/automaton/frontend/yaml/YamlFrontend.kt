package com.digitalsamurai.automaton.frontend.yaml

import com.digitalsamurai.automaton.frontend.api.AutomatonFrontend
import com.digitalsamurai.automaton.frontend.api.AutomatonFrontends
import com.digitalsamurai.automaton.lexis.Token
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.File


public fun AutomatonFrontends.yaml(): YamlFrontend {
    return YamlFrontend()
}

public class YamlFrontend(): AutomatonFrontend {


    override val terminals: Flow<Token<*>> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.SUSPEND)

    suspend fun compile(file: YamlInput) {

    }
}

data class YamlInput(val file: File)