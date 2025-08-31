package com.kerberos.livetrackingsdk.interfaces

import android.location.Location

interface ITrackingLocationInterface {
    fun onLocationUpdated(currentLocation: Location?)

    fun onLocationUpdateFailed(exception: Exception)
}