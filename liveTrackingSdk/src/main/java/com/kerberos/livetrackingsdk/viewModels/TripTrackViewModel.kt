package com.kerberos.livetrackingsdk.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerberos.livetrackingsdk.orm.TripTrack
import com.kerberos.livetrackingsdk.repositories.repositories.TripTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

enum class TripStatus {
    STOPPED,
    RUNNING,
    PAUSED
}

class TripTrackViewModel constructor(private val tripTrackRepository: TripTrackRepository) :
    ViewModel() {

    private val _tripTracks = MutableStateFlow<List<TripTrack>>(emptyList())
    val tripTracks: StateFlow<List<TripTrack>> = _tripTracks

    private val _tripStatus = MutableStateFlow(TripStatus.STOPPED)
    val tripStatus: StateFlow<TripStatus> = _tripStatus

    fun getTripTracks(tripId: Int) {
        viewModelScope.launch {
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