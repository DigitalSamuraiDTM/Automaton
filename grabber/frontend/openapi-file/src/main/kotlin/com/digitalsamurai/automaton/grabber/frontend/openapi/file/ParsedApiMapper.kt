package com.digitalsamurai.automaton.grabber.frontend.openapi.file

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.representation.Body
import com.digitalsamurai.automaton.grabber.representation.Header
import com.digitalsamurai.automaton.grabber.representation.HttpRequestType
import com.digitalsamurai.automaton.grabber.representation.JsonSchema
import com.digitalsamurai.automaton.grabber.representation.ParsedApiRequest
import com.digitalsamurai.automaton.grabber.representation.PathSchema
import com.digitalsamurai.automaton.grabber.representation.QuerySchema
import com.digitalsamurai.automaton.grabber.representation.RequestData
import com.digitalsamurai.automaton.grabber.representation.ResponseData
import com.digitalsamurai.automaton.grabber.representation.SchemaValue
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.responses.ApiResponse

internal class ParsedApiMapper {

    /**
     * Transforms a Swagger OpenAPI model into the grabber representation.
     *
     * @param openApi parsed OpenAPI specification.
     * @return grabber API representation.
     */
    public fun getParsedApi(openApi: OpenAPI): AutomatonGrabberParsedApi {
        val requests = mapRequests(openApi)
        return AutomatonGrabberParsedApi(
            info = AutomatonGrabberParsedApi.Info(
                name = openApi.info?.title.orEmpty(),
                description = openApi.info?.description,
            ),
            servers = openApi.servers.orEmpty().map { server ->
                AutomatonGrabberParsedApi.Server(
                    name = server.description ?: server.url.orEmpty(),
                    url = server.url.orEmpty(),
                    requests = requests,
                )
            },
        )
    }

    /**
     * Maps all operations from all OpenAPI paths.
     *
     * @param openApi parsed OpenAPI specification.
     * @return HTTP requests from the specification.
     */
    private fun mapRequests(openApi: OpenAPI): List<ParsedApiRequest.Http> {
        return openApi.paths.orEmpty().flatMap { (path, pathItem) ->
            pathItem.readOperationsMap().map { (method, operation) ->
                ParsedApiRequest.Http(
                    path = path,
                    pathParameters = mapPathParameters(pathItem, operation, openApi.components),
                    type = HttpRequestType.valueOf(method.name),
                    description = operation.description ?: operation.summary,
                    requestData = mapRequestData(pathItem, operation, openApi.components),
                    responseData = mapResponses(operation, openApi.components),
                )
            }
        }
    }

    /**
     * Maps path-level and operation-level parameters with operation overrides.
     *
     * @param pathItem path that owns the operation.
     * @param operation current OpenAPI operation.
     * @return combined parameters.
     */
    private fun parameters(pathItem: PathItem, operation: Operation): List<Parameter> {
        return (pathItem.parameters.orEmpty() + operation.parameters.orEmpty())
            .associateBy { parameter -> parameter.`in` to parameter.name }
            .values
            .toList()
    }

    /**
     * Maps path parameters into the representation path schema.
     *
     * @param pathItem path that owns the operation.
     * @param operation current OpenAPI operation.
     * @param components reusable OpenAPI components.
     * @return mapped path schema.
     */
    private fun mapPathParameters(
        pathItem: PathItem,
        operation: Operation,
        components: Components?,
    ): PathSchema {
        val parameters = parameters(pathItem, operation)
            .filter { parameter -> parameter.`in` == PATH_PARAMETER }
            .associate { parameter ->
                parameter.name to mapSchemaValue(parameter.schema, components)
            }
        return PathSchema(parameters = parameters)
    }

    /**
     * Maps request headers, query parameters and body.
     *
     * @param pathItem path that owns the operation.
     * @param operation current OpenAPI operation.
     * @param components reusable OpenAPI components.
     * @return mapped request data.
     */
    private fun mapRequestData(
        pathItem: PathItem,
        operation: Operation,
        components: Components?,
    ): RequestData {
        val parameters = parameters(pathItem, operation)
        val queryParameters = parameters.filter { parameter -> parameter.`in` == QUERY_PARAMETER }
        return RequestData(
            headers = parameters
                .filter { parameter -> parameter.`in` == HEADER_PARAMETER }
                .map { parameter ->
                    Header(
                        name = parameter.name,
                        optional = parameter.required != true,
                    )
                },
            queryParameters = QuerySchema(
                parameters = queryParameters.associate { parameter ->
                    parameter.name to mapSchemaValue(parameter.schema, components)
                },
                required = queryParameters
                    .filter { parameter -> parameter.required == true }
                    .mapTo(mutableSetOf()) { parameter -> parameter.name },
            ),
            body = mapBody(operation.requestBody?.content, components),
        )
    }

    /**
     * Maps exact numeric OpenAPI response statuses.
     *
     * @param operation current OpenAPI operation.
     * @param components reusable OpenAPI components.
     * @return responses grouped by HTTP status code.
     */
    private fun mapResponses(
        operation: Operation,
        components: Components?,
    ): Map<Int, ResponseData> {
        return operation.responses.orEmpty().mapNotNull { (status, response) ->
            val statusCode = status.toIntOrNull() ?: return@mapNotNull null
            statusCode to mapResponse(response, components)
        }.toMap()
    }

    /**
     * Maps an OpenAPI response.
     *
     * @param response OpenAPI response model.
     * @param components reusable OpenAPI components.
     * @return mapped response data.
     */
    private fun mapResponse(response: ApiResponse, components: Components?): ResponseData {
        return ResponseData(
            description = response.description,
            headers = response.headers.orEmpty().map { (name, header) ->
                Header(
                    name = name,
                    optional = header.required != true,
                )
            },
            body = mapBody(response.content, components),
        )
    }

    /**
     * Maps JSON content into a request or response body.
     *
     * @param content OpenAPI content grouped by media type.
     * @param components reusable OpenAPI components.
     * @return mapped body or null when content is absent.
     */
    private fun mapBody(content: Content?, components: Components?): Body? {
        if (content.isNullOrEmpty()) return null
        val mediaType = content.entries.firstOrNull { (mediaType, _) ->
            val normalizedType = mediaType.substringBefore(';').trim()
            normalizedType.equals(JSON_MEDIA_TYPE, ignoreCase = true) ||
                normalizedType.endsWith(JSON_MEDIA_TYPE_SUFFIX, ignoreCase = true)
        }?.value ?: throw IllegalArgumentException("Only JSON request and response bodies are supported")
        val schema = mediaType.schema ?: return null
        val jsonSchema = mapSchemaValue(schema, components) as? JsonSchema
            ?: throw IllegalArgumentException("A JSON body must have an object or array schema")
        return Body.Json(schema = jsonSchema)
    }

    /**
     * Maps a schema and resolves local component references.
     *
     * @param schema source OpenAPI schema.
     * @param components reusable OpenAPI components.
     * @param resolving references already present in the current branch.
     * @return mapped schema value.
     */
    private fun mapSchemaValue(
        schema: Schema<*>?,
        components: Components?,
        resolving: Set<String> = emptySet(),
    ): SchemaValue {
        requireNotNull(schema) { "A schema is required for every parameter and schema property" }
        val reference = schema.`$ref`
        if (reference != null) {
            require(reference !in resolving) { "Recursive schema reference is not supported: $reference" }
            val resolvedSchema = components?.schemas?.get(reference.substringAfterLast('/'))
                ?: throw IllegalArgumentException("Unable to resolve schema reference: $reference")
            return mapSchemaValue(resolvedSchema, components, resolving + reference)
        }
        return when {
            schema.type == OBJECT_TYPE || schema.properties != null -> JsonSchema.ObjectValue(
                properties = schema.properties.orEmpty().mapValues { (_, property) ->
                    mapSchemaValue(property, components, resolving)
                },
                required = schema.required.orEmpty().toSet(),
            )
            schema.type == ARRAY_TYPE -> JsonSchema.ArrayValue(
                items = mapSchemaValue(schema.items, components, resolving),
            )
            schema.type == STRING_TYPE -> SchemaValue.StringValue
            schema.type == INTEGER_TYPE -> SchemaValue.IntegerValue
            schema.type == NUMBER_TYPE -> SchemaValue.NumberValue
            schema.type == BOOLEAN_TYPE -> SchemaValue.BooleanValue
            else -> throw IllegalArgumentException("Unsupported schema type: ${schema.type}")
        }
    }

    private companion object {
        const val PATH_PARAMETER = "path"
        const val QUERY_PARAMETER = "query"
        const val HEADER_PARAMETER = "header"
        const val JSON_MEDIA_TYPE = "application/json"
        const val JSON_MEDIA_TYPE_SUFFIX = "+json"
        const val OBJECT_TYPE = "object"
        const val ARRAY_TYPE = "array"
        const val STRING_TYPE = "string"
        const val INTEGER_TYPE = "integer"
        const val NUMBER_TYPE = "number"
        const val BOOLEAN_TYPE = "boolean"
    }
}
