package com.kerberos.livetrackingsdk.models

import android.content.Context
import com.kerberos.livetrackingsdk.enums.LiveTrackingMode
import com.kerberos.livetrackingsdk.services.BaseTrackingService

data class TrackSDKConfigurationModel(
//    val context: Context,
    val locationUpdateInterval: Long,
    val backgroundTrackingToggle: Boolean,
    val serviceClass: Class<out BaseTrackingService>? = null,
    val liveTrackingMode: LiveTrackingMode = LiveTrackingMode.FOREGROUND_SERVICE,
)