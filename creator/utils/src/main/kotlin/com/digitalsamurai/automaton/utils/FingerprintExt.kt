package com.digitalsamurai.automaton.utils

import java.nio.ByteBuffer
import java.security.MessageDigest

private object Fingerprinter {

    /**
     * Computes a SHA-256 hash using a digest instance local to this invocation.
     *
     * @param originalData source bytes.
     * @return hexadecimal SHA-256 hash.
     */
    fun sha256(originalData: ByteArray): String {
        return MessageDigest.getInstance("SHA-256").digest(originalData).toHexString()
    }
}

/**
 * Marks the class from which a fingerprint can be obtained
 */
public interface Fingerprinted {
    /**
     * For generation fingerprint from class it
     */
    fun toByteArray(): ByteArray

    /**
     * Encodes fingerprint fields with a four-byte length prefix before each field.
     *
     * @param fields fingerprint fields in a stable order.
     * @return byte representation with unambiguous field boundaries.
     */
    fun encodeFingerprintFields(vararg fields: ByteArray): ByteArray {
        val buffer = ByteBuffer.allocate(fields.sumOf { Int.SIZE_BYTES + it.size })
        fields.forEach { field ->
            buffer.putInt(field.size)
            buffer.put(field)
        }
        return buffer.array()
    }
}
fun Fingerprinted.fingerprint(): String {
    return Fingerprinter.sha256(originalData = toByteArray())
}