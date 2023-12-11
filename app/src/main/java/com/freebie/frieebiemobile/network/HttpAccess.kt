package com.freebie.frieebiemobile.network

import com.freebie.frieebiemobile.ContinuationCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

interface HttpAccess {

    @Throws(IOException::class)
    suspend fun httpRequest(
        requestUrlSegment: String,
        headers: Map<String, String> = emptyMap(),
        body: ByteArray? = null,
        method: Method
    ): HttpResponse
}

enum class Method(val strValue: String) {
    POST("POST"), GET("GET")
}

class HttpResponse(
    val isSuccess: Boolean,
    val code: Int,
    val bodyAsArray: ByteArray,
    val bodyAsString: String
)

class OkHttpImpl @Inject constructor(
    private val client: OkHttpClient,
) : HttpAccess {

    override suspend fun httpRequest(
        requestUrlSegment: String,
        headers: Map<String, String>,
        body: ByteArray?,
        method: Method
    ): HttpResponse {
        val request = Request.Builder()
            .url(BASE_URL + requestUrlSegment)
            .method(method.strValue, body?.toRequestBody())
            .apply {
                headers.forEach { entry -> header(entry.key, entry.value) }
            }.build()
        try {
            val response = client.newCall(request).await()
            return HttpResponse(
                isSuccess = response.isSuccessful,
                code = response.code,
                bodyAsArray = response.body?.bytes() ?: byteArrayOf(),
                bodyAsString = response.body?.toString() ?: "",
            )
        } catch (e: Exception) {
            val isInternetConnected = InternetConnectionProvider.isInternetConnected()
            if (!isInternetConnected) throw NoInternetException()
            else throw e
        }

    }

    private suspend inline fun Call.await(): Response {
        return suspendCancellableCoroutine { continuation ->
            val callback = ContinuationCallback(this, continuation)
            enqueue(callback)
            continuation.invokeOnCancellation(callback)
        }
    }

}