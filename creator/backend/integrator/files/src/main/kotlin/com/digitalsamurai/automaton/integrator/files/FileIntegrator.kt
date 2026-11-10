package com.digitalsamurai.automaton.integrator.files

import com.digitalsamurai.automaton.backend.api.GeneratorOutput
import com.digitalsamurai.automaton.integrator.api.AutomatonIntegrator
import java.io.File

class FileIntegrator(
    private val outputDirectory: File,
): AutomatonIntegrator {

    init {
        if (!outputDirectory.exists()) throw IllegalArgumentException("Output directory '${outputDirectory.path}' does not exist")
        if (!outputDirectory.isDirectory) throw IllegalArgumentException("Output directory '${outputDirectory.path}' is not a directory")
    }

    override suspend fun integrate(generatorOutput: GeneratorOutput) {
        generatorOutput.outputFiles.forEach { fileData ->
            val file = File(outputDirectory, fileData.name)
            file.outputStream().write(fileData.data.toByteArray())
        }

    }
}