package com.digitalsamurai.automaton.sample.data.network

import com.digitalsamurai.automaton.sample.data.network.requests.auth.AuthNetworkHttpRequest
import com.digitalsamurai.opentelemetry.example.core.network.BaseNetwork
import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient

class AuthNetwork(
    host: String,
    port: Int,
    networkHttpClient: NetworkHttpClient,
) : BaseNetwork<AuthNetworkHttpRequest<*, *>>(
    host = host,
    port = port,
    networkHttpClient = networkHttpClient,
) {

    /**
     * Выполняет запрос авторизации.
     *
     * @param REQUEST_DATA тип данных запроса.
     * @param RESPONSE_DATA тип данных ответа.
     * @param networkHttpRequest запрос семейства авторизации.
     * @param data данные запроса.
     * @return результат выполнения с типизированным ответом или ошибкой.
     */
    suspend inline fun <reified REQUEST_DATA : Any, reified RESPONSE_DATA : Any> makeNetworkRequest(
        networkHttpRequest: AuthNetworkHttpRequest<REQUEST_DATA, RESPONSE_DATA>,
        data: REQUEST_DATA,
    ): Result<RESPONSE_DATA> {
        return networkHttpClient.makeNetworkRequest(
            networkHttpRequest = networkHttpRequest,
            data = data,
            host = host,
            port = port,
        )
    }
}
