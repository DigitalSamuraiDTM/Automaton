package com.digitalsamurai.opentelemetry.example.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendAll
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

// TODO вообще запросы надо делать умнее. Надо чтобы мы умели на каждый HTTP response code обрабатывать разные кейсы и, возможно, делать, разную десериализацию
public class NetworkHttpClient() {

    public val client: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 50_000
            socketTimeoutMillis = 10_000
        }
    }

    public suspend inline fun <reified REQUEST_DATA : Any, reified RESPONSE_DATA : Any> makeNetworkRequest(
        networkHttpRequest: NetworkHttpRequest<REQUEST_DATA, RESPONSE_DATA>,
        data: REQUEST_DATA,
    ): Result<RESPONSE_DATA> = withContext(Dispatchers.IO) {
        runCatching {
            val httpResponse = client.request {
                method = networkHttpRequest.method.toKtorMethod()
//                headers.append("traceparent", "00-${span.spanContext.traceId}-${span.spanContext.spanId}-01")
                if (networkHttpRequest is AuthorizedNetworkHttpRequest) {
                    headers.append("Authorization", networkHttpRequest.jwt.value)
                }
                url {
                    host = networkHttpRequest.host
                    port = networkHttpRequest.port
                    path(networkHttpRequest.path)
                }
                // у гет метода нет тела же по идее, закодируем по кайфу в параметры
                if (method == HttpMethod.Get) {
                    Json.encodeToJsonElement(data).jsonObject.entries.forEach { jsonEntry ->
                        url.parameters.append(jsonEntry.key, jsonEntry.value.toString().trim('"'))
                    }
                } else {
                    contentType(ContentType.Application.Json)
                    setBody(data, typeInfo<REQUEST_DATA>())
                }
            }
            val responseBody = httpResponse.body<RESPONSE_DATA>()
            responseBody
        }
    }

    public fun NetworkHttpRequest.Method.toKtorMethod(): HttpMethod {
        return when (this) {
            NetworkHttpRequest.Method.POST -> HttpMethod.Post
            NetworkHttpRequest.Method.GET -> HttpMethod.Get
            NetworkHttpRequest.Method.PUT -> HttpMethod.Put
            NetworkHttpRequest.Method.DELETE -> HttpMethod.Delete
        }
    }
}