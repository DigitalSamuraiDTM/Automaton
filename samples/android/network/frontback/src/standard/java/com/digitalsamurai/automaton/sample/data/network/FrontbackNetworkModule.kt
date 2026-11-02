package com.digitalsamurai.automaton.sample.data.network

import com.digitalsamurai.opentelemetry.example.core.network.NetworkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FrontbackNetworkModule {

    /**
     * Подключает frontback-backend к DI-графу приложения.
     *
     * @return singleton frontback-backend с отдельным HTTP-клиентом.
     */
    @Provides
    @Singleton
    fun provideFrontbackNetwork(): FrontbackNetwork {
        return FrontbackNetwork(
            host = "10.0.2.2",
            port = 8080,
            networkHttpClient = NetworkHttpClient(),
        )
    }
}
