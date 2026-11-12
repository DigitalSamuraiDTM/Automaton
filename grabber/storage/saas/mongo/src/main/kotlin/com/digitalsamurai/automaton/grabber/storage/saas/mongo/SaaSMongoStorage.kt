package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document
import java.time.Instant
import java.util.Date

public data object SaaSMongoStorageMeta : AutomatonGrabberStorageMeta

internal class SaaSMongoStorage(
    private val client: MongoClient,
    private val collection: MongoCollection<Document>,
) : AutomatonGrabberStorage<SaaSMongoStorageMeta>, AutoCloseable {

    /**
     * Saves a stub document for the synchronization request.
     *
     * @param request parsed API specification.
     * @param meta MongoDB storage metadata.
     * @return Unit after MongoDB acknowledges the insert.
     */
    override fun sync(
        request: AutomatonGrabberParsedApi,
        meta: SaaSMongoStorageMeta,
    ) {
        val document = Document("type", "automaton_grabber_sync_stub")
            .append("apiName", request.info.name)
            .append("apiDescription", request.info.description)
            .append("serversCount", request.servers.size)
            .append("meta", meta.toString())
            .append("createdAt", Date.from(Instant.now()))
        collection.insertOne(document)
        println("MONGO SYNC STUB SAVED: ${document.toJson()}")
    }

    /**
     * Closes the MongoDB client.
     *
     * @return Unit after the client is closed.
     */
    override fun close() {
        client.close()
    }
}
