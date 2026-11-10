package com.digitalsamurai.automaton.utils

import java.security.MessageDigest
import kotlin.reflect.full.memberProperties

private object Fingerprinter {
    // TODO not thread safety?
    var digest: MessageDigest = MessageDigest.getInstance("SHA-1")

    fun sha256(originalData: ByteArray): String {
        return digest.digest(originalData).toHexString()
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
}
fun Fingerprinted.fingerprint(): String {
    return Fingerprinter.sha256(originalData = toByteArray())
}