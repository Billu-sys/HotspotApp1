package com.example.hotspotapp

import android.content.Context
import android.content.SharedPreferences

class HotspotPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("HotspotPrefs", Context.MODE_PRIVATE)

    fun getSsid(): String = sharedPreferences.getString("ssid", "MyHotspot") ?: "MyHotspot"
    fun setSsid(ssid: String) = sharedPreferences.edit().putString("ssid", ssid).apply()

    fun getPassword(): String = sharedPreferences.getString("password", "password123") ?: "password123"
    fun setPassword(password: String) = sharedPreferences.edit().putString("password", password).apply()

    fun getMaxConnections(): Int = sharedPreferences.getInt("maxConnections", 5)
    fun setMaxConnections(maxConnections: Int) = sharedPreferences.edit().putInt("maxConnections", maxConnections).apply()
}