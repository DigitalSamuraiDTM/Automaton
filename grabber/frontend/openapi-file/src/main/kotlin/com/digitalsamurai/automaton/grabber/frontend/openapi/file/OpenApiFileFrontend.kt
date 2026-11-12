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

    /**
     * Reads an OpenAPI JSON or YAML file and synchronizes its test representation.
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
        // TODO Заменить тестовое представление на полный конвертер OpenAPI в AutomatonGrabberParsedApi.
        val request = AutomatonGrabberParsedApi(
            info = AutomatonGrabberParsedApi.Info(
                name = openApi.info.title,
                description = openApi.info.description,
            ),
            servers = emptyList(),
        )
        sync(request = request, meta = meta)
        return openApi
    }

    /**
     * Runs grabber synchronization through the frontend API.
     *
     * @param request parsed API specification.
     * @param meta metadata required by the storage.
     * @return Unit after the grabber handles the request.
     */
    public fun sync(
        request: AutomatonGrabberParsedApi,
        meta: M,
    ) {
        grabber.sync(request = request, meta = meta)
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
