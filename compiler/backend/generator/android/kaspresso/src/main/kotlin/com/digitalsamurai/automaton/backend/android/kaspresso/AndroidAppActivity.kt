package com.digitalsamurai.automaton.backend.android.kaspresso

data class AndroidAppActivity(
    val activityPackage: String,
    val name: String,
) {
    override fun toString(): String {
        return "$activityPackage.$name"
    }
}