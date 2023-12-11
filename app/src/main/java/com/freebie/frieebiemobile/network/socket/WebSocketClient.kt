package com.freebie.frieebiemobile.network.socket

import android.util.Log
import com.freebie.frieebiemobile.login.TokenStorage
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.SocketOptionBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

interface WebSocketClient {
    suspend fun initWebSocket(): Result<Boolean>
}

@Singleton
class SocketIOClient @Inject constructor(
    private val tokenStorage: TokenStorage
): WebSocketClient {

    private lateinit var socket: Socket

    private fun createSocket(token: String): Socket {
        val options = SocketOptionBuilder.builder()
            //.setPort(8086)
            .setReconnectionAttempts(10)
            .setTimeout(10000)
            .setExtraHeaders(mutableMapOf(
                "Authorization" to listOf(token)
            ))
            .build()
        return IO.socket("http://freebie-app.com:8086", options)
    }

    override suspend fun initWebSocket(): Result<Boolean> = withContext(Dispatchers.IO) {
        var token = tokenStorage.getAccessToken()
        token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIxIiwiaXNzIjoiZnJlZWJpZS5hdXRoIiwiaWF0IjoxNzAyMzMyMDg0LCJleHAiOjE3MDI5MzY4ODR9.vhBNesFe1kW8ITBv87zJ1H236Ng-G43RLasvEOgmSydDNBfOjeFQzuVwaVTaSC8e2WX-zjZoJIvrYioaDIjbNs_0b7qZ8ucpIQVPeYgyXVvJABp_U052nCbAPs5avQidDlBGKePxoyp1VodXFkQ2zHp6FZwbQqamixGHLh4zEmkvnS6zNuYv3usDqvJLpJfXdYLDBFghouOcmfJLp2sQ2uAd3WPPrzEqSJucckpXyYT6wxHdhDxC_QmM6_fHNbbtLzAFeT9iVkYtxpZNnfAkVrekyISHSA0aa7e3Sdz8iefu3R-MAU5moIgJxs8bhVn_kLKyrvLCbtLdurgNgnwVoA"
        if (token != null) {
            Log.d("SocketIOClient", "createSocket token = $token")
            socket = createSocket(token)
            socket.connect()
            subscribeOnEvents()
            return@withContext Result.success(true)
        } else {
            Log.d("SocketIOClient", "no token")
            return@withContext Result.failure(IllegalStateException("no token"))
        }
    }

    private fun subscribeOnEvents() {
        Log.d("SocketIOClient", "subscribeOnEvents")
        socket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketIOClient", "Connection status = ${socket.connected()}")
            val jsonObject = JSONObject()
            jsonObject.put("subscribeTo", "coupon_purchase")
//            val params = JSONObject()  // то что в комменте в случае когда реально нужно отправлять параметры а так без них работает
//            params.put("1", "1")
//            params.put("2", "2")
//            jsonObject.put("params", params)
            socket.emit("subscribe", jsonObject)
        }


        socket.on("coupon_purchase") {
            Log.d("SocketIOClient", "coupon_purchase = ${it}")
        }
    }

}

class SubscribeEvent (
    val subscribeTo: String,
    val params: Map<String, Any>
)