package com.digitalsamurai.automaton.utils

import java.security.MessageDigest

private object Fingerprinter {
    // TODO not thread safety?
    var digest: MessageDigest = MessageDigest.getInstance("SHA-256")

    fun sha256(originalData: String): String {
        return digest.digest(originalData.encodeToByteArray()).toHexString()
    }
}
fun Any.fingerprint(): String {
    return Fingerprinter.sha256(this.toString())
}