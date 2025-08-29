package com.kerberos.livetrackingsdk.backgroundTracking

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener

object LocationTrackingHelper {

    @SuppressLint("MissingPermission")
    fun updateLocation(
        context: Context, locationCallback: (location: Location?) -> Unit,
        failCallback: (message: String?) -> Unit
    ) {
        try {
//            val activity = context.unwrap()
            Log.v("LocationWorker.TrackingCore.updateLocation", "Call the update")

            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(true)
                .setDurationMillis(10000)
                .build()
//            val locationRequest = LocationRequest.create()
//                .setWaitForAccurateLocation(true)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setExpirationDuration(10000)

            LocationServices.getSettingsClient(context)
                .checkLocationSettings(
                    LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                        .setNeedBle(true)
                        .setAlwaysShow(true)
                        .build()
                )
                .addOnSuccessListener {

                    LocationServices.getFusedLocationProviderClient(context).getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
//                        LocationRequest.PRIORITY_HIGH_ACCURACY,//LocationRequest.PRIORITY_NO_POWER,
                        object : CancellationToken() {
                            override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                                return this
                            }

                            override fun isCancellationRequested(): Boolean {
                                return false
                            }

                        },
                    ).addOnSuccessListener { location: Location? ->
                        locationCallback(location)
                        Log.v("LocationWorker.TrackingCore.updateLocation", "Call the update")
                    }.addOnFailureListener {
                        failCallback(it.message)
                        Log.v("LocationWorker.TrackingCore.updateLocation", "Call the update")
                    }
                    Log.v("LocationWorker.TrackingCore.updateLocation", "Call the update")
//                    enableLocation.value = Response.success(true)
                }
                .addOnFailureListener {
                    failCallback(it.message)
                    Log.v("LocationWorker.TrackingCore.updateLocation", "Call the update")
//                    Timber.e(it, "Gps not enabled")
//                    enableLocation.value = Response.error(it)
                }
        } catch (ex: Exception) {
            Log.v("Location update", ex.message ?: ex.localizedMessage ?: ex.stackTraceToString())
        }
    }


}