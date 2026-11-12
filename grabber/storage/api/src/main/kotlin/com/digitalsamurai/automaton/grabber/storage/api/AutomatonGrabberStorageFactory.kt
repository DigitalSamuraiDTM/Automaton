package com.digitalsamurai.automaton.grabber.storage.api

public interface AutomatonGrabberStorageFactory<M : AutomatonGrabberStorageMeta> {

    /**
     * Builds a storage with the configured implementation details.
     *
     * @return configured storage.
     */
    public fun build(): AutomatonGrabberStorage<M>
}
