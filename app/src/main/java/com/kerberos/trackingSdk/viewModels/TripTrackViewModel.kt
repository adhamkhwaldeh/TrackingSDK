package com.kerberos.trackingSdk.viewModels

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.kerberos.livetrackingsdk.interfaces.ITrackingLocationListener
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager
import com.kerberos.trackingSdk.orm.TripTrack
import com.kerberos.trackingSdk.repositories.repositories.TripTrackRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TripStatus {
    STOPPED,
    RUNNING,
    PAUSED
}

class TripTrackViewModel(
    private val tripTrackRepository: TripTrackRepository,
//    private val locationTrackingManager: LocationTrackingManager
) :
    ViewModel(), ITrackingLocationListener {

    private val _tripTracks = MutableStateFlow<List<TripTrack>>(emptyList())
    val tripTracks: StateFlow<List<TripTrack>> = _tripTracks

    private val _currentLocation = MutableStateFlow<LatLng?>(null)
    val currentLocation: StateFlow<LatLng?> = _currentLocation

    private val _tripStatus = MutableStateFlow(TripStatus.STOPPED)
    val tripStatus: StateFlow<TripStatus> = _tripStatus

    fun getTripTracks(tripId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            tripTrackRepository.getTripTracks(tripId).collect {
                _tripTracks.value = it
            }
        }
    }


    fun startTrip() {
        _tripStatus.value = TripStatus.RUNNING
    }

    fun pauseTrip() {
        _tripStatus.value = TripStatus.PAUSED
    }

    fun resumeTrip() {
        _tripStatus.value = TripStatus.RUNNING
    }

    fun stopTrip() {
        _tripStatus.value = TripStatus.STOPPED
    }

    fun startLocationUpdates() {
//        locationTrackingManager.addTrackingLocationListener(this)
//        locationTrackingManager.onStartTracking()
    }

    fun stopLocationUpdates() {
//        locationTrackingManager.onStopTracking()
    }

    override fun onLocationUpdated(location: Location?) {
        location?.let {
            _currentLocation.value = LatLng(it.latitude, it.longitude)
        }
    }

    override fun onLocationUpdateFailed(error: Exception) {
        // Handle location update failure
    }
}