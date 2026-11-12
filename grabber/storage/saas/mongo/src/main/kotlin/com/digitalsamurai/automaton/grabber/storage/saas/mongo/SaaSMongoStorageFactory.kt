package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorage
import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageFactory
import com.digitalsamurai.automaton.grabber.storage.api.GrabberStoragesScope
import com.mongodb.client.MongoClients

public class SaaSMongoStorageFactory internal constructor() :
    AutomatonGrabberStorageFactory<SaaSMongoStorageMeta> {

    public var connectionString: String = "mongodb://localhost:27017"

    public var databaseName: String = "automaton"

    public var collectionName: String = "grabber_sync_stubs"

    /**
     * Builds a MongoDB storage with the configured connection.
     *
     * @return configured storage with hidden MongoDB implementation details.
     */
    override fun build(): AutomatonGrabberStorage<SaaSMongoStorageMeta> {
        val client = MongoClients.create(connectionString)
        val collection = client
            .getDatabase(databaseName)
            .getCollection(collectionName)
        return SaaSMongoStorage(client = client, collection = collection)
    }
}

/**
 * Configures a SaaS MongoDB storage factory.
 *
 * @param factoryScope MongoDB connection configuration.
 * @return configured [SaaSMongoStorageFactory].
 */
public fun GrabberStoragesScope.mongo(
    factoryScope: SaaSMongoStorageFactory.() -> Unit = {},
): SaaSMongoStorageFactory {
    val factory = SaaSMongoStorageFactory()
    factory.factoryScope()
    return factory
}
