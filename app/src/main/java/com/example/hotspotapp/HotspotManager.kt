package com.example.hotspotapp

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class HotspotManager(private val context: Context) {

    private val wifiManager: WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @RequiresApi(Build.VERSION_CODES.O)
    fun startHotspot(frequency: Int, maxConnections: Int, ssid: String, password: String, callback: (Boolean) -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                wifiManager.startLocalOnlyHotspot(object : WifiManager.LocalOnlyHotspotCallback() {
                    override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation?) {
                        super.onStarted(reservation)
                        Log.d(TAG, "Hotspot started")
                        callback(true)
                    }

                    override fun onStopped() {
                        super.onStopped()
                        Log.d(TAG, "Hotspot stopped")
                        callback(false)
                    }

                    override fun onFailed(reason: Int) {
                        super.onFailed(reason)
                        Log.e(TAG, "Hotspot failed to start. Reason: $reason")
                        callback(false)
                    }
                }, null)
            } catch (e: SecurityException) {
                Log.e(TAG, "Security exception: ${e.message}")
                callback(false)
            }
        } else {
            Log.e(TAG, "Hotspot creation not supported on this Android version")
            callback(false)
        }
    }

    fun stopHotspot() {
        // Note: There's no direct way to stop the LocalOnlyHotspot API.
        // The system will stop it when the app is closed or crashes.
        Log.d(TAG, "Hotspot stop requested. It will stop when the app is closed.")
    }

    companion object {
        private const val TAG = "HotspotManager"
        const val FREQUENCY_2_4_GHZ = 2400
        const val FREQUENCY_5_GHZ = 5000
    }
}