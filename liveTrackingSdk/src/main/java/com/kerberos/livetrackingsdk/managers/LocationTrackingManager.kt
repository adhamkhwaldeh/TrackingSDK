package com.kerberos.livetrackingsdk.managers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kerberos.livetrackingsdk.interfaces.ITrackingLocationInterface
import com.kerberos.livetrackingsdk.interfaces.ITrackingActionsInterface
import com.kerberos.livetrackingsdk.enums.TrackingState
import com.kerberos.livetrackingsdk.exceptions.GpsNotEnabledException
import com.kerberos.livetrackingsdk.exceptions.PermissionNotGrantedException
import com.kerberos.livetrackingsdk.helpers.PermissionsHelper

class LocationTrackingManager private constructor(
    val context: Context,
    private val minTimeMillis: Long = 0L,
    private val minDistanceMeters: Float = 0f,
    initialListeners: List<ITrackingLocationInterface> = emptyList(),
) : ITrackingActionsInterface {

    private val fusedClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    private val locationManager: LocationManager by lazy {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private val request: LocationRequest
        get() {
            // Use the values set by the builder
            return LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                minTimeMillis.coerceAtLeast(1000L)
            ) // Ensure interval isn't too small
                // .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // Already set in constructor
                .setMinUpdateIntervalMillis(minTimeMillis) // Smallest interval if updates are more frequent from other sources
                .setMinUpdateDistanceMeters(minDistanceMeters)
                // .setMaxUpdates(1) // Remove if you want continuous updates
                .build()
//            return LocationRequest.Builder(1000L)
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//                .setMinUpdateIntervalMillis(minTimeMillis)
//                .setIntervalMillis(1000L)
////                .setMinUpdateDistanceMeters(minDistanceMeters)
////                .setMaxUpdates(1)
//                .build()
        }

    private val trackingLocationInterfaces: MutableList<ITrackingLocationInterface> =
        initialListeners.toMutableList()

    private val callback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            val location = result.lastLocation
            trackingLocationInterfaces.forEach { trackingLocationInterface ->
                trackingLocationInterface.onLocationUpdated(location)
            }
        }

        override fun onLocationAvailability(availability: LocationAvailability) {
            if (!availability.isLocationAvailable) {
                trackingLocationInterfaces.toList().forEach { trackingLocationInterface ->
                    val gpsValue = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if (!gpsValue) {
                        trackingLocationInterface.onLocationUpdateFailed(GpsNotEnabledException())
                        return@forEach
                    } else if (!PermissionsHelper.isPermissionGranted(context)) {
                        trackingLocationInterface.onLocationUpdateFailed(
                            PermissionNotGrantedException()
                        )

                    } else {
                        // You might want a specific callback for unavailability or use onLocationUpdateFailed
                        trackingLocationInterface.onLocationUpdateFailed(Exception("Location became unavailable"))
                    }
                }
            }
        }
    }

    private var currentTrackingState: TrackingState = TrackingState.IDLE

    val trackingState: TrackingState
        get() {
            return currentTrackingState
        }

    // Builder class
    class Builder(private val context: Context) {
        private var minTimeMillis: Long = 5000L // Default value for minTimeMillis (e.g., 5 seconds)
        private var minDistanceMeters: Float = 10f // Default value for minDistanceMeters (e.g., 10 meters)
        private val listeners: MutableList<ITrackingLocationInterface> = mutableListOf()

        fun setMinTimeMillis(minTimeMillis: Long): Builder = apply {
            this.minTimeMillis = minTimeMillis
        }

        fun setMinDistanceMeters(minDistanceMeters: Float): Builder = apply {
            this.minDistanceMeters = minDistanceMeters
        }

        fun addTrackingLocationListener(listener: ITrackingLocationInterface): Builder = apply {
            if (!this.listeners.contains(listener)) {
                this.listeners.add(listener)
            }
        }

        fun build(): LocationTrackingManager {
            return LocationTrackingManager(context, minTimeMillis, minDistanceMeters, listeners)
        }
    }

    /**
     * Adds a tracking location interface to receive location updates.
     *
     * @param listener The interface to add.
     */
    fun addTrackingLocationListener(listener: ITrackingLocationInterface) {
        if (!trackingLocationInterfaces.contains(listener)) {
            trackingLocationInterfaces.add(listener)
            // If tracking is already active and we add a new listener,
            // consider providing it the last known location immediately if available.
            // Or, it will just start receiving new updates.
        }
    }

    /**
     * Removes a tracking location interface from receiving location updates.
     *
     * @param listener The interface to remove.
     */
    fun removeTrackingLocationListener(listener: ITrackingLocationInterface) {
        trackingLocationInterfaces.remove(listener)
    }

    /**
     * Clears all registered tracking location interfaces.
     */
    fun clearAllTrackingLocationListeners() {
        trackingLocationInterfaces.clear()
    }

    override fun onStartTracking(): Boolean {
        if (!(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            fusedClient.requestLocationUpdates(request, callback, Looper.getMainLooper())
            currentTrackingState = TrackingState.STARTED
        } else {
            trackingLocationInterfaces.forEach { trackingLocationInterface ->
                trackingLocationInterface.onLocationUpdateFailed(PermissionNotGrantedException())
            }
            return false
        }
        return true
    }

    override fun onResumeTracking(): Boolean {
        return onStartTracking()
    }

    override fun onPauseTracking(): Boolean {
        fusedClient.removeLocationUpdates(callback)
        currentTrackingState = TrackingState.PAUSED
        return true
    }

    override fun onStopTracking(): Boolean {
        currentTrackingState = TrackingState.IDLE
        fusedClient.removeLocationUpdates(callback)
        return true
    }

}