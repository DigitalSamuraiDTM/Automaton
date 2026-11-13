package com.digitalsamurai.automaton.grabber.frontend.openapi.file

import com.digitalsamurai.automaton.grabber.AutomatonGrabber
import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import java.io.File

public class OpenApiFileFrontend<M : AutomatonGrabberStorageMeta>(
    private val grabber: AutomatonGrabber<M>,
) {

    private val parsedApiMapper = ParsedApiMapper()

    /**
     * Reads an OpenAPI JSON or YAML file and synchronizes its mapped representation.
     *
     * @param input file containing the OpenAPI specification.
     * @param meta metadata required by the storage.
     * @return OpenAPI model parsed by Swagger Parser.
     */
    public fun grab(
        input: OpenApiFileInput,
        meta: M,
    ): OpenAPI {
        val openApi = parse(input.file)
        val request = parsedApiMapper.getParsedApi(openApi)
        grabber.sync(request = request, meta = meta)
        return openApi
    }

    /**
     * Parses an OpenAPI document into the Swagger Parser model.
     *
     * @param file source document.
     * @return parsed OpenAPI model.
     */
    private fun parse(file: File): OpenAPI {
        val result = OpenAPIParser().readLocation(file.absolutePath, null, null)
        result.messages.forEach { message ->
            System.err.println("OpenAPI parser: $message")
        }
        return checkNotNull(result.openAPI) {
            "Unable to parse OpenAPI document: ${file.absolutePath}"
        }
    }
}

public data class OpenApiFileInput(
    val file: File,
)
