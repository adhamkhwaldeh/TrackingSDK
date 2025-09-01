package com.kerberos.livetrackingsdk.trackingManagers

import android.content.Context
import com.kerberos.livetrackingsdk.interfaces.ITrackingActionsListener
import com.kerberos.livetrackingsdk.interfaces.ITrackingSdkModeStatusListener
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager

abstract class BaseTrackingManager(
    var context: Context,
    val trackingSdkModeStatusListener: ITrackingSdkModeStatusListener
) : ITrackingActionsListener {
//    abstract val locationManager: LocationTrackingManager?
//        get

    abstract fun initializeTrackingManager(): Boolean

    abstract fun destroyTrackingManager(): Boolean
}