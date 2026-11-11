package com.digitalsamurai.automaton.grabber.frontend.openapi.file

import com.digitalsamurai.automaton.grabber.frontend.api.AutomatonGrabberRequest
import com.digitalsamurai.automaton.grabber.frontend.api.GrabberFrontend
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

public class OpenApiFileFrontend : GrabberFrontend {

    override val requestsFlow: MutableSharedFlow<AutomatonGrabberRequest> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND,
    )

    /**
     * Reads an OpenAPI JSON or YAML file and sends a placeholder request to the grabber.
     *
     * @param input file containing the OpenAPI specification.
     * @return OpenAPI model parsed by Swagger Parser.
     */
    public suspend fun grab(input: OpenApiFileInput): OpenAPI {
        val openApi = withContext(Dispatchers.IO) {
            parse(input.file)
        }
        val request = AutomatonGrabberRequest(
            requestId = UUID.randomUUID().toString(),
        )
        requestsFlow.emit(request)
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
