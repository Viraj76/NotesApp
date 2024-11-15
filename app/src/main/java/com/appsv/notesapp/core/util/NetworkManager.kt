package com.appsv.notesapp.core.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * This class monitors the internet connectivity in the user's phone and returns it as a Flow.
 */
class NetworkManager(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder().apply {
        addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
    }.build()

    fun observeNetworkStatus(): Flow<Boolean> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true).isSuccess
            }

            override fun onUnavailable() {
                trySend(false).isSuccess
            }

            override fun onLost(network: Network) {
                trySend(false).isSuccess
            }
        }

        // Check the current active network status and emit initial state
        val isConnected = connectivityManager.activeNetwork != null &&
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true


        trySend(isConnected).isSuccess

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }
}
