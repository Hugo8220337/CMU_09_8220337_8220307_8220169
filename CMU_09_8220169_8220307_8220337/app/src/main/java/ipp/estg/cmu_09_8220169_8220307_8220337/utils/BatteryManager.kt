package ipp.estg.cmu_09_8220169_8220307_8220337.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.BatteryManager

@SuppressLint("ServiceCast")
class BatteryManager(context: Context) {
    private val batteryManager: BatteryManager by lazy {
        context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }

    fun getBatteryLevel(): Int {
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}