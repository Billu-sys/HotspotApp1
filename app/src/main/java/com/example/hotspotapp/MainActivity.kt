package com.example.hotspotapp

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var hotspotManager: HotspotManager
    private lateinit var permissionHelper: PermissionHelper
    private lateinit var hotspotPreferences: HotspotPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hotspotManager = HotspotManager(this)
        permissionHelper = PermissionHelper(this)
        hotspotPreferences = HotspotPreferences(this)

        val startHotspotButton: Button = findViewById(R.id.startHotspotButton)
        val stopHotspotButton: Button = findViewById(R.id.stopHotspotButton)
        val frequencyRadioGroup: RadioGroup = findViewById(R.id.frequencyRadioGroup)
        val maxConnectionsEditText: EditText = findViewById(R.id.maxConnectionsEditText)
        val ssidEditText: EditText = findViewById(R.id.ssidEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)

        // Load saved preferences
        ssidEditText.setText(hotspotPreferences.getSsid())
        passwordEditText.setText(hotspotPreferences.getPassword())
        maxConnectionsEditText.setText(hotspotPreferences.getMaxConnections().toString())

        startHotspotButton.setOnClickListener {
            if (permissionHelper.checkAndRequestPermissions()) {
                val frequency = when (frequencyRadioGroup.checkedRadioButtonId) {
                    R.id.radio24GHz -> HotspotManager.FREQUENCY_2_4_GHZ
                    R.id.radio5GHz -> HotspotManager.FREQUENCY_5_GHZ
                    else -> HotspotManager.FREQUENCY_2_4_GHZ
                }
                val maxConnections = maxConnectionsEditText.text.toString().toIntOrNull() ?: 1
                val ssid = ssidEditText.text.toString()
                val password = passwordEditText.text.toString()

                // Save preferences
                hotspotPreferences.setSsid(ssid)
                hotspotPreferences.setPassword(password)
                hotspotPreferences.setMaxConnections(maxConnections)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    hotspotManager.startHotspot(frequency, maxConnections, ssid, password) { success ->
                        runOnUiThread {
                            if (success) {
                                Toast.makeText(this, "Hotspot started successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to start hotspot", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Hotspot feature not supported on this device", Toast.LENGTH_SHORT).show()
                }
            }
        }

        stopHotspotButton.setOnClickListener {
            hotspotManager.stopHotspot()
            Toast.makeText(this, "Hotspot will stop when the app is closed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }
}