package com.kerberos.livetrackingsdk

import android.content.Context
import com.kerberos.livetrackingsdk.dataStore.SdkPreferencesManager
import com.kerberos.livetrackingsdk.enums.LiveTrackingMode
import com.kerberos.livetrackingsdk.interfaces.ITrackingActionsInterface
import com.kerberos.livetrackingsdk.interfaces.ITrackingLocationInterface
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager
import com.kerberos.livetrackingsdk.models.SdkSettings
import com.kerberos.livetrackingsdk.services.BaseTrackingService
import com.kerberos.livetrackingsdk.services.DefaultTrackingService
import com.kerberos.livetrackingsdk.trackingManagers.BackgroundTrackingManager
import com.kerberos.livetrackingsdk.trackingManagers.BaseTrackingManager
import com.kerberos.livetrackingsdk.trackingManagers.ForegroundTrackingManager


class LiveTrackingManager(
    val context: Context,
    backgroundService: Class<out BaseTrackingService> = DefaultTrackingService::class.java,
    val liveTrackingMode: LiveTrackingMode = LiveTrackingMode.FOREGROUND_SERVICE,
    sdkSettings: SdkSettings = SdkSettings(
        minDistanceMeters = 1000f,
        locationUpdateInterval = 1000L,
        backgroundTrackingToggle = false
    ),
) {

    private val sdkPreferencesManager: SdkPreferencesManager by lazy {
        SdkPreferencesManager(context)
    }

    init {
        sdkPreferencesManager.updateAllSettings(sdkSettings)
    }

    val trackingManagers: MutableList<BaseTrackingManager> = mutableListOf(
        ForegroundTrackingManager(context),
        BackgroundTrackingManager(context, backgroundService)
    )

    val currentTrackingManager: BaseTrackingManager
        get() {
            return when (liveTrackingMode) {
                LiveTrackingMode.FOREGROUND_SERVICE -> trackingManagers.first { it is ForegroundTrackingManager }
                LiveTrackingMode.BACKGROUND_SERVICE -> trackingManagers.first { it is BackgroundTrackingManager }
            }
        }

    val currentLocationTrackingManager: LocationTrackingManager?
        get() {
            return currentTrackingManager.locationManager
        }

    fun changeTrackingMode(newMode: LiveTrackingMode): Boolean {
        if (newMode == liveTrackingMode) return true
        val stopResult = currentTrackingManager.onStopTracking()
        if (!stopResult) return false
        val initResult = currentTrackingManager.initializeTrackingManager()
        if (!initResult) return false
        return true
    }

    fun changeSdkSettings(sdkSettings: SdkSettings): Boolean {
        sdkPreferencesManager.updateAllSettings(sdkSettings)
        return currentLocationTrackingManager?.invalidateConfiguration() ?: false
    }

    fun addTrackingLocationListener(listener: ITrackingLocationInterface) {
        currentLocationTrackingManager?.addTrackingLocationListener(listener)
    }

    /**
     * Removes a tracking location interface from receiving location updates.
     *
     * @param listener The interface to remove.
     */
    fun removeTrackingLocationListener(listener: ITrackingLocationInterface) {
        currentLocationTrackingManager?.removeTrackingLocationListener(listener)
    }

    /**
     * Clears all registered tracking location interfaces.
     */
    fun clearAllTrackingLocationListeners() {
        currentLocationTrackingManager?.clearAllTrackingLocationListeners()
    }


}