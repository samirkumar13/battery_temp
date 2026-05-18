package com.samir.batterytemp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.samir.batterytemp.ui.theme.BatteryTempTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    private val historyList = mutableStateListOf<String>()
    private var temperature by mutableFloatStateOf(0f)
    private var status by mutableStateOf("Unknown")

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            processBatteryIntent(intent)
        }
    }

    private fun processBatteryIntent(intent: Intent) {
        val tempRaw = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        temperature = tempRaw / 10.0f
        status = when (intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Charging"
            BatteryManager.BATTERY_STATUS_FULL -> "Full"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Discharging"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Not Charging"
            else -> "Unknown"
        }

        val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        historyList.add(0, "$time  →  $temperature °C  [$status]")
        if (historyList.size > 20) historyList.removeAt(historyList.size - 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val stickyIntent = ContextCompat.registerReceiver(
            this,
            batteryReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        // Process initial battery state if available
        stickyIntent?.let { processBatteryIntent(it) }

        setContent {
            BatteryTempTheme {
                BatteryScreen(
                    temperature = temperature,
                    status = status,
                    history = historyList
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }
}