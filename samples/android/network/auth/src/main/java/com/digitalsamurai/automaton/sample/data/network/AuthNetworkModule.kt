package com.digitalsamurai.automaton.sample.data.network

import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthNetworkModule {

    /**
     * Подключает backend авторизации к DI-графу приложения.
     *
     * @return singleton backend авторизации с отдельным HTTP-клиентом.
     */
    @Provides
    @Singleton
    fun provideAuthNetwork(): AuthNetwork {
        return AuthNetwork(
            host = "10.0.2.2",
            port = 8081,
            networkHttpClient = NetworkHttpClient(),
        )
    }
}
