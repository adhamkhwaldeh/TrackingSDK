package com.kerberos.livetrackingsdk.repositories.repositories

import com.kerberos.livetrackingsdk.orm.TripTrack
import com.kerberos.livetrackingsdk.orm.TripTrackDoa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TripTrackRepository constructor(private val tripTrackDao: TripTrackDoa) {

    fun getTripTracks(tripId: Int): Flow<List<TripTrack>> = flow {
        emit(tripTrackDao.loadAllData().filter { it.tripId == tripId })
    }
}
