package com.digitalsamurai.automaton.grabber.representation

public data class AutomatonGrabberParsedApi(
    val info: Info,
    val servers: List<Server>,
) {
    public data class Info(
        val name: String,
        val description: String?,
    )
    public data class Server(
        val name: String,
        val url: String,
        val requests: List<ParsedApiRequest>,
    )
}

public sealed interface ParsedApiRequest {

    public data class Http(
        val path: String,
        val pathParameters: PathSchema = PathSchema(),
        val type: HttpRequestType,
        val description: String?,
        val requestData: RequestData,
        val responseData: Map<Int, ResponseData>,
    ) : ParsedApiRequest
}

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

public data class RequestData(
    val headers: List<Header> = emptyList(),
    val queryParameters: QuerySchema = QuerySchema(),
    val body: Body? = null,
)

public data class ResponseData(
    val description: String?,
    val headers: List<Header> = emptyList(),
    val body: Body? = null,
)

public data class Header(
    val name: String,
    val optional: Boolean = false,
)

// TODO: Validate that every path placeholder has a parameter and every parameter is present in the path.
public data class PathSchema(
    val parameters: Map<String, SchemaValue> = emptyMap(),
) : Schema

public data class QuerySchema(
    val parameters: Map<String, SchemaValue> = emptyMap(),
    val required: Set<String> = emptySet(),
) : Schema

public sealed interface Body {

    public data class Json(
        val schema: JsonSchema,
    ) : Body

    public data class Xml(
        val schema: XmlSchema,
    ) : Body
}

public sealed interface Schema

public sealed interface SchemaValue : Schema {

    public data object StringValue : SchemaValue

    public data object IntegerValue : SchemaValue

    public data object NumberValue : SchemaValue

    public data object BooleanValue : SchemaValue
}

public sealed interface JsonSchema : Schema {

    public data class ArrayValue(
        val items: SchemaValue,
    ) : JsonSchema, SchemaValue

    public data class ObjectValue(
        val properties: Map<String, SchemaValue> = emptyMap(),
        val required: Set<String> = emptySet(),
    ) : JsonSchema, SchemaValue
}

// TODO will be maked on demand
public sealed interface XmlSchema : Schema
