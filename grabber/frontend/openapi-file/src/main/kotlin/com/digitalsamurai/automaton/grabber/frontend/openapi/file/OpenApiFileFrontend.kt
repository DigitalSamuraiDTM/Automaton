package com.digitalsamurai.automaton.grabber.frontend.openapi.file

import com.digitalsamurai.automaton.grabber.frontend.api.AutomatonGrabberRequest
import com.digitalsamurai.automaton.grabber.frontend.api.GrabberFrontend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.UUID

public class OpenApiFileFrontend : GrabberFrontend {

    override val requestsFlow: MutableSharedFlow<AutomatonGrabberRequest> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.SUSPEND,
    )

    /**
     * Reads an OpenAPI JSON or YAML file and sends its JSON representation to the grabber.
     *
     * @param input file containing the OpenAPI specification.
     * @return parsed JSON representation of the specification.
     */
    public suspend fun grab(input: OpenApiFileInput): JSONObject {
        val data = withContext(Dispatchers.IO) {
            readJson(input.file)
        }
        val request = AutomatonGrabberRequest(
            requestId = UUID.randomUUID().toString(),
            data = data,
        )
        requestsFlow.emit(request)
        return data
    }

    /**
     * Converts a JSON or YAML document with an object root into a JSON object.
     *
     * @param file source document.
     * @return parsed JSON object.
     */
    private fun readJson(file: File): JSONObject {
        file.inputStream().use { inputStream ->
            val document = Yaml().load<Any?>(inputStream)
            require(document is Map<*, *>) {
                "OpenAPI document root must be an object: ${file.absolutePath}"
            }
            return JSONObject(document)
        }
    }
}

public data class OpenApiFileInput(
    val file: File,
)
