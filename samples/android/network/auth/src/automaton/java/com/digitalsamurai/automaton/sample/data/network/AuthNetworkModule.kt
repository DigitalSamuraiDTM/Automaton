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
     * Подключает backend авторизации к локальному серверу Automaton.
     *
     * @return singleton backend авторизации с отдельным HTTP-клиентом.
     */
    @Provides
    @Singleton
    fun provideAuthNetwork(): AuthNetwork {
        return AuthNetwork(
            host = "127.0.0.1",
            port = 8080,
            networkHttpClient = NetworkHttpClient(),
        )
    }
}
