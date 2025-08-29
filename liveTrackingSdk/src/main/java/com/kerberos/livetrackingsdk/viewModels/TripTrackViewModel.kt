package com.kerberos.livetrackingsdk.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerberos.livetrackingsdk.orm.TripTrack
import com.kerberos.livetrackingsdk.repositories.repositories.TripTrackRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TripTrackViewModel constructor(private val tripTrackRepository: TripTrackRepository) :
    ViewModel() {

    private val _tripTracks = MutableStateFlow<List<TripTrack>>(emptyList())
    val tripTracks: StateFlow<List<TripTrack>> = _tripTracks

    fun getTripTracks(tripId: Int) {
        viewModelScope.launch {
            tripTrackRepository.getTripTracks(tripId).collect {
                _tripTracks.value = it
            }
        }
    }
}