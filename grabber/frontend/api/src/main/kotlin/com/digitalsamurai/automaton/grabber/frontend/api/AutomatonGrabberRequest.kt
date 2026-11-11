package com.digitalsamurai.automaton.grabber.frontend.api

import org.json.JSONObject

public data class AutomatonGrabberRequest(
    val requestId: String,
    val data: JSONObject,
)
