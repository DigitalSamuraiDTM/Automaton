package com.digitalsamurai.automaton.sample.core.di

import com.digitalsamurai.automaton.sample.data.network.AuthNetwork
import com.digitalsamurai.automaton.sample.data.network.FrontbackNetwork
import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun provideNetworkHttpClient(): NetworkHttpClient {
        return NetworkHttpClient()
    }

    /**
     * Создаёт backend авторизации.
     *
     * @param networkHttpClient HTTP-клиент для выполнения запросов.
     * @return backend, принимающий только запросы авторизации.
     */
    @Provides
    @Singleton
    fun provideAuthNetwork(networkHttpClient: NetworkHttpClient): AuthNetwork {
        return AuthNetwork(
            host = "10.0.2.2",
            port = 8081,
            networkHttpClient = networkHttpClient,
        )
    }

    /**
     * Создаёт backend основных функций приложения.
     *
     * @param networkHttpClient HTTP-клиент для выполнения запросов.
     * @return backend, принимающий только frontback-запросы.
     */
    @Provides
    @Singleton
    fun provideFrontbackNetwork(networkHttpClient: NetworkHttpClient): FrontbackNetwork {
        return FrontbackNetwork(
            host = "10.0.2.2",
            port = 8080,
            networkHttpClient = networkHttpClient,
        )
    }
}