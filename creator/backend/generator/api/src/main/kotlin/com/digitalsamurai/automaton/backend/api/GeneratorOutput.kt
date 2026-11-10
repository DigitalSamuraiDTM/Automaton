package com.digitalsamurai.automaton.backend.api

data class GeneratorOutput(
    val requestId: String,
    val metaData: String,
    val outputFiles: List<FileData>,
) {

    data class FileData(
        val name: String,
        val data: String,
    )
}