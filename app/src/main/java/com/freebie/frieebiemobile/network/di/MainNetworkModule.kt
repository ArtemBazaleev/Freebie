package com.freebie.frieebiemobile.network.di

import com.freebie.frieebiemobile.network.DEFAULT_REQUEST_TIMEOUT
import com.freebie.frieebiemobile.network.HttpAccess
import com.freebie.frieebiemobile.network.OkHttpImpl
import com.freebie.frieebiemobile.network.interceptors.HeadersInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainNetworkOkHttpModule {

    @Provides
    fun provideOkHttpClient(
        headersInterceptor: HeadersInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(HttpLoggingInterceptor())
            .readTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MainNetworkModule {

    @Singleton
    @Binds
    abstract fun provideHttpAccess(impl: OkHttpImpl): HttpAccess
}