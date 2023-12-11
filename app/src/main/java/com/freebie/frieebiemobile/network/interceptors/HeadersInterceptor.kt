package com.freebie.frieebiemobile.network.interceptors

import android.content.Context
import com.freebie.frieebiemobile.login.TokenStorage
import com.freebie.frieebiemobile.network.ACCEPT
import com.freebie.frieebiemobile.network.APPLICATION_PROTOBUF
import com.freebie.frieebiemobile.network.AUTHORIZATION
import com.freebie.frieebiemobile.network.BEARER
import com.freebie.frieebiemobile.network.CONTENT_TYPE
import com.freebie.frieebiemobile.network.DEFAULT_LOCALE
import com.freebie.frieebiemobile.network.DEFAULT_PLATFORM
import com.freebie.frieebiemobile.network.LOCALE
import com.freebie.frieebiemobile.network.PLATFORM
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeadersInterceptor @Inject constructor(
    @ApplicationContext val appContext: Context,
    private val tokenStorage: TokenStorage
) : Interceptor {

    private val locale by lazy {
        try {
            appContext.resources.configuration.locales[0].language
        } catch (e: Exception) { DEFAULT_LOCALE }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(ACCEPT, APPLICATION_PROTOBUF)
            .addHeader(CONTENT_TYPE, APPLICATION_PROTOBUF)
            .addHeader(PLATFORM, DEFAULT_PLATFORM)
            .addHeader(LOCALE, locale)

        runBlocking {
            runCatching {
                val token = tokenStorage.getAccessToken()
                token?.let { tokenNonNull ->
                    newRequest.addHeader(AUTHORIZATION, BEARER + tokenNonNull)
                }
            }
        }
        return chain.proceed(newRequest.build())

    }
}
