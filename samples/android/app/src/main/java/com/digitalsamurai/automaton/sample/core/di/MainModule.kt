package com.digitalsamurai.automaton.sample.core.di

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
}