package com.kerberos.livetrackingsdk.trackingManagers

import android.content.Context
import com.kerberos.livetrackingsdk.interfaces.ITrackingActionsInterface
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager

abstract class BaseTrackingManager(var context: Context) : ITrackingActionsInterface {
    abstract val locationManager: LocationTrackingManager?
        get

    abstract fun initializeTrackingManager(): Boolean

    abstract fun destroyTrackingManager(): Boolean
}