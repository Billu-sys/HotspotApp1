package com.example.hotspotapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHelper(private val activity: Activity) {

    private val PERMISSIONS_REQUEST_CODE = 100

    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.NEARBY_WIFI_DEVICES,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
        )
    }

    fun checkAndRequestPermissions(): Boolean {
        val permissionsToRequest = mutableListOf<String>()

        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsToRequest.toTypedArray(), PERMISSIONS_REQUEST_CODE)
            return false
        }

        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            return grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        }
        return false
    }
}