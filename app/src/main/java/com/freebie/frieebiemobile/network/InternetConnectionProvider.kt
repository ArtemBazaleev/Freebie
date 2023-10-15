package com.freebie.frieebiemobile.network

import timber.log.Timber
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

// Google default DNS server
private const val HOSTNAME = "8.8.8.8"
private const val PORT = 53
private const val TIMEOUT = 1500

object InternetConnectionProvider {

    private val socketFactory = SocketFactory.getDefault()

    fun isInternetConnected(): Boolean {
        return try {
            Timber.d("PINGING google.")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress(HOSTNAME, PORT), TIMEOUT)
            socket.close()
            Timber.d("PING success.")
            true
        } catch (e: IOException) {
            Timber.e("No internet connection. $e")
            false
        }
    }
}