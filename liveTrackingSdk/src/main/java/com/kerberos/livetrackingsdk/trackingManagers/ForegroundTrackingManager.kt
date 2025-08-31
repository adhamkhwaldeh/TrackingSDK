package com.kerberos.livetrackingsdk.trackingManagers

import android.content.Context
import com.kerberos.livetrackingsdk.enums.LiveTrackingMode
import com.kerberos.livetrackingsdk.interfaces.ITrackingSdkModeStatusListener
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager


class ForegroundTrackingManager(
    context: Context,
    trackingSdkModeStatusListener: ITrackingSdkModeStatusListener
) : BaseTrackingManager(context, trackingSdkModeStatusListener) {

    private val locationTrackingManager: LocationTrackingManager by lazy {
        LocationTrackingManager(context)
    }

    //#region SDK actions
    override fun initializeTrackingManager(): Boolean {
        trackingSdkModeStatusListener.onTrackingSDKModeInitialized(
            locationTrackingManager,
            LiveTrackingMode.FOREGROUND_SERVICE
        )
        return true
    }

    override fun destroyTrackingManager(): Boolean {
        return locationTrackingManager.onStopTracking()
    }
    //#endregion


}