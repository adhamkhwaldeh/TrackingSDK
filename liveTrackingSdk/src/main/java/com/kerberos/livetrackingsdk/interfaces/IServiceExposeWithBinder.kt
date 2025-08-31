package com.kerberos.livetrackingsdk.interfaces

import com.kerberos.livetrackingsdk.services.BaseTrackingService
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
interface IServiceExposeWithBinder {
    fun getServiceInstance(): BaseTrackingService
}