package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.representation.ParsedApiRequest
import com.digitalsamurai.automaton.grabber.representation.ResponseData
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import kotlinx.serialization.json.Json
import org.bson.Document
import org.bson.types.ObjectId

public data object SaaSMongoStorageMeta : AutomatonGrabberStorageMeta

internal class SaaSMongoStorage(
    private val client: MongoClient,
    private val collection: MongoCollection<Document>,
) : AutomatonGrabberStorage<SaaSMongoStorageMeta>, AutoCloseable {

    private val json = Json {
        classDiscriminator = "_kind"
        encodeDefaults = true
    }

    /**
     * Saves the parsed API as a tree of API, server, request and response documents.
     *
     * @param request parsed API specification.
     * @param meta MongoDB storage metadata.
     * @return Unit after MongoDB acknowledges the insert.
     */
    override fun sync(
        request: AutomatonGrabberParsedApi,
        meta: SaaSMongoStorageMeta,
    ) {
        val documents = createTreeDocuments(request)
        // TODO insertMany не является атомарной транзакцией: при ошибке часть дерева может быть уже записана.
        // TODO Продумать индексы по nodeType, родительским ссылкам и бизнес-ключам данных.
        collection.insertMany(documents)
        println("MONGO PARSED API TREE SAVED: ${documents.size} documents")
    }

    /**
     * Creates an API root document and all its descendant nodes.
     *
     * @param request parsed API specification.
     * @return documents linked by parent identifiers.
     */
    private fun createTreeDocuments(request: AutomatonGrabberParsedApi): List<Document> {
        val apiId = ObjectId()
        val documents = mutableListOf(
            Document("_id", apiId)
                .append("nodeType", API_NODE)
                .append(
                    "info",
                    Document("name", request.info.name)
                        .append("description", request.info.description)
                )
        )
        request.servers.forEach { server ->
            val serverId = ObjectId()
            documents += Document("_id", serverId)
                .append("nodeType", SERVER_NODE)
                .append("apiId", apiId)
                .append("name", server.name)
                .append("url", server.url)
            server.requests.forEach { parsedRequest ->
                when (parsedRequest) {
                    is ParsedApiRequest.Http -> documents += createHttpRequestDocuments(
                        request = parsedRequest,
                        serverId = serverId,
                    )
                }
            }
        }
        return documents
    }

    /**
     * Creates an HTTP request node and its response child nodes.
     *
     * @param request HTTP request representation.
     * @param serverId identifier of the parent server document.
     * @return request document followed by response documents.
     */
    private fun createHttpRequestDocuments(
        request: ParsedApiRequest.Http,
        serverId: ObjectId,
    ): List<Document> {
        val requestId = ObjectId()
        val requestDocument = Document.parse(
            json.encodeToString(ParsedApiRequest.Http.serializer(), request)
        )
            .append("_id", requestId)
            .append("nodeType", HTTP_REQUEST_NODE)
            .append("serverId", serverId)
        requestDocument.remove("responseData")
        val responseDocuments = request.responseData.map { (statusCode, response) ->
            Document.parse(json.encodeToString(ResponseData.serializer(), response))
                .append("_id", ObjectId())
                .append("nodeType", RESPONSE_NODE)
                .append("requestId", requestId)
                .append("statusCode", statusCode)
        }
        return listOf(requestDocument) + responseDocuments
    }

    /**
     * Closes the MongoDB client.
     *
     * @return Unit after the client is closed.
     */
    override fun close() {
        client.close()
    }

    private companion object {
        const val API_NODE = "api"
        const val SERVER_NODE = "server"
        const val HTTP_REQUEST_NODE = "http_request"
        const val RESPONSE_NODE = "http_response"
    }
}
