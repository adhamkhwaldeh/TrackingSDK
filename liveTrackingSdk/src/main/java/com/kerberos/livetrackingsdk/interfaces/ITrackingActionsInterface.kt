package com.kerberos.livetrackingsdk.interfaces

interface ITrackingActionsInterface {
    fun onStartTracking(): Boolean

    fun onResumeTracking(): Boolean

    fun onPauseTracking(): Boolean

    fun onStopTracking(): Boolean
}