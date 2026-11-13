package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.representation.ParsedApiRequest
import com.digitalsamurai.automaton.grabber.representation.ResponseData
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import kotlinx.serialization.json.Json
import org.bson.Document
import org.bson.types.ObjectId
import java.time.Instant
import java.util.Date

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
        val documents = createTreeDocuments(request, meta)
        // TODO insertMany не является атомарной транзакцией: при ошибке часть дерева может быть уже записана.
        // TODO Продумать индексы по nodeType, родительским ссылкам и бизнес-ключам данных.
        collection.insertMany(documents)
        println("MONGO PARSED API TREE SAVED: ${documents.size} documents")
    }

    /**
     * Creates initial metadata shared by all nodes of the new tree.
     *
     * @param meta operation execution metadata.
     * @return metadata for newly created nodes.
     */
    private fun createNodeMetadata(meta: SaaSMongoStorageMeta): SaaSMongoNodeMetadata {
        val timestamp = Instant.now()
        return SaaSMongoNodeMetadata(
            teamId = meta.teamId,
            management = when (val actor = meta.actor) {
                is SaaSActorMeta.User -> SaaSManagementMeta.Manual
                is SaaSActorMeta.Integration -> SaaSManagementMeta.Integration(actor.id)
            },
            createdBy = meta.actor,
            createdAt = timestamp,
            updatedBy = meta.actor,
            updatedAt = timestamp,
            version = INITIAL_VERSION,
            sourceRevision = meta.sourceRevision,
        )
    }

    /**
     * Converts node metadata into a MongoDB document.
     *
     * @param metadata node metadata.
     * @return BSON-compatible metadata document.
     */
    private fun createMetadataDocument(metadata: SaaSMongoNodeMetadata): Document {
        return Document("teamId", metadata.teamId)
            .append(
                "management",
                when (val management = metadata.management) {
                    SaaSManagementMeta.Manual -> Document("type", "manual")
                    is SaaSManagementMeta.Integration -> Document("type", "integration")
                        .append("integrationId", management.integrationId)
                }
            )
            .append("createdBy", createActorDocument(metadata.createdBy))
            .append("createdAt", Date.from(metadata.createdAt))
            .append("updatedBy", createActorDocument(metadata.updatedBy))
            .append("updatedAt", Date.from(metadata.updatedAt))
            .append("version", metadata.version)
            .append("sourceRevision", metadata.sourceRevision)
    }

    /**
     * Converts an actor into a MongoDB document.
     *
     * @param actor operation actor.
     * @return document with actor type and identifier.
     */
    private fun createActorDocument(actor: SaaSActorMeta): Document {
        return when (actor) {
            is SaaSActorMeta.User -> Document("type", "user").append("id", actor.id)
            is SaaSActorMeta.Integration -> Document("type", "integration").append("id", actor.id)
        }
    }

    /**
     * Creates an API root document and all its descendant nodes.
     *
     * @param request parsed API specification.
     * @param meta operation execution metadata.
     * @return documents linked by parent identifiers.
     */
    private fun createTreeDocuments(
        request: AutomatonGrabberParsedApi,
        meta: SaaSMongoStorageMeta,
    ): List<Document> {
        val metadata = createNodeMetadata(meta)
        val apiId = ObjectId()
        val documents = mutableListOf(
            Document("_id", apiId)
                .append("nodeType", API_NODE)
                .append("metadata", createMetadataDocument(metadata))
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
                .append("metadata", createMetadataDocument(metadata))
                .append("name", server.name)
                .append("url", server.url)
            server.requests.forEach { parsedRequest ->
                when (parsedRequest) {
                    is ParsedApiRequest.Http -> documents += createHttpRequestDocuments(
                        request = parsedRequest,
                        serverId = serverId,
                        metadata = metadata,
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
     * @param metadata metadata assigned to the new nodes.
     * @return request document followed by response documents.
     */
    private fun createHttpRequestDocuments(
        request: ParsedApiRequest.Http,
        serverId: ObjectId,
        metadata: SaaSMongoNodeMetadata,
    ): List<Document> {
        val requestId = ObjectId()
        val requestDocument = Document.parse(
            json.encodeToString(ParsedApiRequest.Http.serializer(), request)
        )
            .append("_id", requestId)
            .append("nodeType", HTTP_REQUEST_NODE)
            .append("serverId", serverId)
            .append("metadata", createMetadataDocument(metadata))
        requestDocument.remove("responseData")
        val responseDocuments = request.responseData.map { (statusCode, response) ->
            Document.parse(json.encodeToString(ResponseData.serializer(), response))
                .append("_id", ObjectId())
                .append("nodeType", RESPONSE_NODE)
                .append("requestId", requestId)
                .append("statusCode", statusCode)
                .append("metadata", createMetadataDocument(metadata))
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
        const val INITIAL_VERSION = 1L
    }
}
