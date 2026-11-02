package com.digitalsamurai.automaton.sample.data.network.requests.auth

import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpRequest

abstract class AuthNetworkHttpRequest<REQUEST_DATA : Any, RESPONSE_DATA : Any> :
    NetworkHttpRequest<REQUEST_DATA, RESPONSE_DATA>()