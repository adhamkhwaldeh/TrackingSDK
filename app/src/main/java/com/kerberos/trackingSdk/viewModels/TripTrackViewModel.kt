package com.kerberos.trackingSdk.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerberos.trackingSdk.orm.TripTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class TripStatus {
    STOPPED,
    RUNNING,
    PAUSED
}

class TripTrackViewModel(private val tripTrackRepository: com.kerberos.trackingSdk.repositories.repositories.TripTrackRepository) :
    ViewModel() {

    private val _tripTracks = MutableStateFlow<List<TripTrack>>(emptyList())
    val tripTracks: StateFlow<List<TripTrack>> = _tripTracks

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
}