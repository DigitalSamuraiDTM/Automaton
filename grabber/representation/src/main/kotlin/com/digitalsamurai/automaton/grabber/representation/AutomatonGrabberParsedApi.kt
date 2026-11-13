package com.digitalsamurai.automaton.grabber.representation

import kotlinx.serialization.Serializable

@Serializable
public data class AutomatonGrabberParsedApi(
    val info: Info,
    val servers: List<Server>,
) {
    @Serializable
    public data class Info(
        val name: String,
        val description: String?,
    )

    @Serializable
    public data class Server(
        val name: String,
        val url: String,
        val requests: List<ParsedApiRequest>,
    )
}

@Serializable
public sealed interface ParsedApiRequest {

    @Serializable
    public data class Http(
        val path: String,
        val pathParameters: PathSchema = PathSchema(),
        val type: HttpRequestType,
        val description: String?,
        val requestData: RequestData,
        val responseData: Map<Int, ResponseData>,
    ) : ParsedApiRequest
}

@Serializable
public enum class HttpRequestType {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTIONS,
    TRACE,
}

@Serializable
public data class RequestData(
    val headers: List<Header> = emptyList(),
    val queryParameters: QuerySchema = QuerySchema(),
    val body: Body? = null,
)

@Serializable
public data class ResponseData(
    val description: String?,
    val headers: List<Header> = emptyList(),
    val body: Body? = null,
)

@Serializable
public data class Header(
    val name: String,
    val optional: Boolean = false,
)

// TODO: Validate that every path placeholder has a parameter and every parameter is present in the path.
@Serializable
public data class PathSchema(
    val parameters: Map<String, SchemaValue> = emptyMap(),
) : Schema

@Serializable
public data class QuerySchema(
    val parameters: Map<String, SchemaValue> = emptyMap(),
    val required: Set<String> = emptySet(),
) : Schema

@Serializable
public sealed interface Body {

    @Serializable
    public data class Json(
        val schema: JsonSchema,
    ) : Body

    @Serializable
    public data class Xml(
        val schema: XmlSchema,
    ) : Body
}

@Serializable
public sealed interface Schema

@Serializable
public sealed interface SchemaValue : Schema {

    @Serializable
    public data object StringValue : SchemaValue

    @Serializable
    public data object IntegerValue : SchemaValue

    @Serializable
    public data object NumberValue : SchemaValue

    @Serializable
    public data object BooleanValue : SchemaValue
}

@Serializable
public sealed interface JsonSchema : Schema {

    @Serializable
    public data class ArrayValue(
        val items: SchemaValue,
    ) : JsonSchema, SchemaValue

    @Serializable
    public data class ObjectValue(
        val properties: Map<String, SchemaValue> = emptyMap(),
        val required: Set<String> = emptySet(),
    ) : JsonSchema, SchemaValue
}

// TODO will be maked on demand
@Serializable
public sealed interface XmlSchema : Schema
