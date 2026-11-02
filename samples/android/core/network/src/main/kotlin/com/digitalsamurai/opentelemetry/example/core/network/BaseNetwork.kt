package com.digitalsamurai.opentelemetry.example.core.network

/**
 * Представляет backend, который принимает только запросы семейства [REQUEST].
 *
 * @param REQUEST маркер семейства запросов, поддерживаемых backend.
 * @property host хост backend.
 * @property port порт backend.
 * @property networkHttpClient HTTP-клиент, выполняющий запросы.
 */
public abstract class BaseNetwork<REQUEST: NetworkHttpRequest<*,*>>(
    public val host: String,
    public val port: Int,
    public val networkHttpClient: NetworkHttpClient,
) {}
