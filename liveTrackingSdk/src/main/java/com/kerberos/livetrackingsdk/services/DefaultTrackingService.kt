package com.kerberos.livetrackingsdk.services

import android.location.Location
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
class DefaultTrackingService : BaseTrackingService() {

    override val serviceClassForRestart
        get(): Class<out DefaultTrackingService> {
            return DefaultTrackingService::class.java
        }

    override fun onLocationUpdated(currentLocation: Location?) {

    }

    override fun onLocationUpdateFailed(exception: Exception) {

    }

}