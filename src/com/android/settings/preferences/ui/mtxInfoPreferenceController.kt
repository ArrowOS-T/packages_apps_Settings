/*
 * Copyright (C) 2023 the DroidxOS Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.preferences.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemProperties
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.view.View
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.android.settings.R
import com.android.settingslib.core.AbstractPreferenceController
import com.android.settingslib.DeviceInfoUtils
import com.android.settingslib.widget.LayoutPreference

import com.android.settings.preferences.ui.DeviceInfoUtil

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class mtxInfoPreferenceController(context: Context) : AbstractPreferenceController(context) {

    private val defaultFallback = mContext.getString(R.string.device_info_default)

    private fun getProp(propName: String): String {
        return SystemProperties.get(propName, defaultFallback)
    }
    
    private fun getPropertyOrDefault(propName: String): String {
        return SystemProperties.get(propName, defaultFallback)
    }

    private fun getProp(propName: String, customFallback: String): String {
        val propValue = SystemProperties.get(propName)
        return if (propValue.isNotEmpty()) propValue else SystemProperties.get(customFallback, "Unknown")
    }
    
    private fun getDeviceChipset(): String {
        return getProp(PROP_DEVICE_CHIPSET)
    }
    
    private fun getDeviceResolution(): String {
        return getProp(PROP_DEVICE_DISPLAY)
    }

    private fun getDeviceManufacturer(): String {
        return getProp(PROP_DEVICE_MANUFACTURER)
    }

    private fun getDeviceCodenamePhone(): String {
        return getProp(PROP_DEVICE_CODENAMEPHONE)
    }

    private fun getDeviceColorBodyPhone(): String {
        return getProp(PROP_DEVICE_COLORBODYPHONE)
    }
    
    
    override fun displayPreference(screen: PreferenceScreen) {
        super.displayPreference(screen)

        val hwInfoPreference = screen.findPreference<LayoutPreference>(KEY_HW_INFO)!!
        val phoneInfoPreference = screen.findPreference<LayoutPreference>(KEY_PHONE_INFO)!!

        hwInfoPreference.apply {
            findViewById<TextView>(R.id.device_chipset).text = getDeviceChipset()
            findViewById<TextView>(R.id.device_storage).text = "${DeviceInfoUtil.getStorageTotal(mContext)}"
            findViewById<TextView>(R.id.device_ram).text = "${DeviceInfoUtil.getTotalRam()}"
            findViewById<TextView>(R.id.device_battery_capacity).text = DeviceInfoUtil.getBatteryCapacity(mContext)
            findViewById<TextView>(R.id.device_resolution).text = getDeviceResolution()
        }

        phoneInfoPreference.apply {
            findViewById<TextView>(R.id.manufacturer_summary).text = getDeviceManufacturer()
            findViewById<TextView>(R.id.codename_phone).text = getDeviceCodenamePhone()
            findViewById<TextView>(R.id.colorbody_phone).text = getDeviceColorBodyPhone()
        }
    }

    override fun isAvailable(): Boolean {
        return true
    }

    override fun getPreferenceKey(): String {
        return KEY_DEVICE_INFO
    }

    companion object {
        
        private const val KEY_HW_INFO = "device_hw_info"
        private const val KEY_PHONE_INFO = "phone_info"
        private const val KEY_DEVICE_INFO = "my_device_info_header"
        
        private const val KEY_STORAGE = "device_storage"
        private const val KEY_CHIPSET = "device_chipset"
        private const val KEY_BATTERY = "device_battery_capacity"
        private const val KEY_DISPLAY = "device_resolution"
        private const val KEY_RAM_STORAGE = "device_ram_storage"

        private const val PROP_DEVICE_CHIPSET = "ro.device.chipset"
        private const val PROP_DEVICE_DISPLAY = "ro.device.display_resolution"

        private const val PROP_DEVICE_MANUFACTURER = "ro.product.manufacturer"
        private const val PROP_DEVICE_CODENAMEPHONE = "ro.product.device"

        private const val PROP_DEVICE_COLORBODYPHONE = "ro.device.colorbody_phone"
        
    }
}
