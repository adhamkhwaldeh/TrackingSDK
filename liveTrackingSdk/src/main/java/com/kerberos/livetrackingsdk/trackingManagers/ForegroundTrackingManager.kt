package com.kerberos.livetrackingsdk.trackingManagers

import android.content.Context
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager


class ForegroundTrackingManager(
    context: Context
) : BaseTrackingManager(context) {

    private val locationTrackingManager: LocationTrackingManager by lazy {
        LocationTrackingManager(context)
    }

    override val locationManager: LocationTrackingManager?
        get() {
            return locationTrackingManager
        }

    //#region SDK actions
    override fun initializeTrackingManager(): Boolean {
        return true
    }

    override fun destroyTrackingManager(): Boolean {
        return locationTrackingManager.onStopTracking()
    }
    //#endregion

    //#region actions
    override fun onStartTracking(): Boolean {
        return locationTrackingManager.onStartTracking()
    }

    override fun onResumeTracking(): Boolean {
        return locationTrackingManager.onResumeTracking()
    }

    override fun onPauseTracking(): Boolean {
        return locationTrackingManager.onPauseTracking()
    }

    override fun onStopTracking(): Boolean {
        return locationTrackingManager.onStopTracking()
    }
    //#endregion

}