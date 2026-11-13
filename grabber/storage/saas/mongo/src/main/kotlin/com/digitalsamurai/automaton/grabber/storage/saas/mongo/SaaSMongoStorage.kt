package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import com.digitalsamurai.automaton.grabber.representation.AutomatonGrabberParsedApi
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import kotlinx.serialization.json.Json
import org.bson.Document

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
     * Saves the complete parsed API as one MongoDB document.
     *
     * @param request parsed API specification.
     * @param meta MongoDB storage metadata.
     * @return Unit after MongoDB acknowledges the insert.
     */
    override fun sync(
        request: AutomatonGrabberParsedApi,
        meta: SaaSMongoStorageMeta,
    ) {
        val document = Document.parse(
            json.encodeToString(AutomatonGrabberParsedApi.serializer(), request)
        )
        collection.insertOne(document)
        println("MONGO PARSED API SAVED: ${document.toJson()}")
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
