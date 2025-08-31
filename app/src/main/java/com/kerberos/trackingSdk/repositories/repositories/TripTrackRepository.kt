package com.kerberos.trackingSdk.repositories.repositories


import com.kerberos.trackingSdk.orm.TripTrack
import com.kerberos.trackingSdk.orm.TripTrackDoa
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TripTrackRepository(private val tripTrackDao: TripTrackDoa) {

    fun getTripTracks(tripId: Int): Flow<List<TripTrack>> = flow {
        emit(tripTrackDao.loadAllData().filter { it.tripId == tripId })
    }
}
