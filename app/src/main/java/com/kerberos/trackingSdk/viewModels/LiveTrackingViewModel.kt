package com.kerberos.trackingSdk.viewModels

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kerberos.livetrackingsdk.LiveTrackingManager
import com.kerberos.livetrackingsdk.enums.TrackingState
import com.kerberos.livetrackingsdk.interfaces.ITrackingLocationListener
import com.kerberos.livetrackingsdk.interfaces.ITrackingStatusListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LiveTrackingViewModel(
    application: Application,
    private val appPrefsStorage: com.kerberos.trackingSdk.dataStore.AppPrefsStorage
) : AndroidViewModel(application),
    ITrackingStatusListener, ITrackingLocationListener {

    private val _trackingState = MutableStateFlow<TrackingState>(TrackingState.STOPPED)
    val trackingState: StateFlow<TrackingState> = _trackingState

    private val _locationState = MutableStateFlow<Location?>(null)
    val locationState: StateFlow<Location?> = _locationState

    private val liveTrackingManager: LiveTrackingManager = LiveTrackingManager.Builder(application)
        .build()

    init {
        liveTrackingManager.addTrackingStatusListener(this)
        liveTrackingManager.addTrackingLocationListener(this)
        liveTrackingManager.currentTrackingManager.initializeTrackingManager()

        viewModelScope.launch {
            appPrefsStorage.trackSDKConfiguration.collect { settings ->
                settings?.let {
                    liveTrackingManager.changeSdkSettings(it)
                }
            }
        }
    }

    fun startTracking() {
        liveTrackingManager.onStartTracking()
    }

    fun stopTracking() {
        liveTrackingManager.onStopTracking()
    }

    fun pauseTracking() {
        liveTrackingManager.onPauseTracking()
    }

    fun resumeTracking() {
        liveTrackingManager.onResumeTracking()
    }

    override fun onTrackingStateChanged(state: TrackingState) {
        viewModelScope.launch {
            _trackingState.value = state
        }
    }

    override fun onLocationUpdated(currentLocation: Location?) {
        viewModelScope.launch {
            _locationState.value = currentLocation
        }
    }

    override fun onLocationUpdateFailed(exception: Exception) {
        // Handle the error, e.g., log it or show a message to the user
    }

    override fun onCleared() {
        super.onCleared()
        liveTrackingManager.removeTrackingStatusListener(this)
        liveTrackingManager.removeTrackingLocationListener(this)
    }
}
