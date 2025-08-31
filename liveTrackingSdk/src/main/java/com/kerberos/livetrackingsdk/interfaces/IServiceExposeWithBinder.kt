package com.kerberos.livetrackingsdk.interfaces

import com.kerberos.livetrackingsdk.managers.LocationTrackingManager
import kotlinx.coroutines.DelicateCoroutinesApi

interface IServiceExposeWithBinder {
    fun getLocationTrackingManager(): LocationTrackingManager
}