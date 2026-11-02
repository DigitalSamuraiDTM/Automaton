package com.digitalsamurai.automaton.sample.data.network

import com.digitalsamurai.automaton.sample.data.network.requests.frontback.FrontbackAuthorizedNetworkHttpRequest
import com.digitalsamurai.automaton.sample.data.network.requests.frontback.FrontbackNetworkHttpRequest
import com.digitalsamurai.opentelemetry.example.core.network.BaseNetwork
import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient

class FrontbackNetwork(
    host: String,
    port: Int,
    networkHttpClient: NetworkHttpClient,
) : BaseNetwork<FrontbackNetworkHttpRequest<*,*>>(
    host = host,
    port = port,
    networkHttpClient = networkHttpClient,
) {

    /**
     * Выполняет неавторизованный frontback-запрос.
     *
     * @param REQUEST_DATA тип данных запроса.
     * @param RESPONSE_DATA тип данных ответа.
     * @param networkHttpRequest frontback-запрос.
     * @param data данные запроса.
     * @return результат выполнения с типизированным ответом или ошибкой.
     */
    suspend inline fun <reified REQUEST_DATA : Any, reified RESPONSE_DATA : Any> makeNetworkRequest(
        networkHttpRequest: FrontbackNetworkHttpRequest<REQUEST_DATA, RESPONSE_DATA>,
        data: REQUEST_DATA,
    ): Result<RESPONSE_DATA> {
        return networkHttpClient.makeNetworkRequest(
            networkHttpRequest = networkHttpRequest,
            data = data,
            host = host,
            port = port,
        )
    }

    /**
     * Выполняет авторизованный frontback-запрос.
     *
     * @param REQUEST_DATA тип данных запроса.
     * @param RESPONSE_DATA тип данных ответа.
     * @param networkHttpRequest frontback-запрос с авторизационным токеном.
     * @param data данные запроса.
     * @return результат выполнения с типизированным ответом или ошибкой.
     */
    suspend inline fun <reified REQUEST_DATA : Any, reified RESPONSE_DATA : Any> makeNetworkRequest(
        networkHttpRequest: FrontbackAuthorizedNetworkHttpRequest<REQUEST_DATA, RESPONSE_DATA>,
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
