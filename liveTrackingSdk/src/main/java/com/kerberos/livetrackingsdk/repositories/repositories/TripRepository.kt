package com.kerberos.livetrackingsdk.repositories.repositories


import com.kerberos.livetrackingsdk.models.TripModel
import com.kerberos.livetrackingsdk.orm.Trip
import com.kerberos.livetrackingsdk.orm.TripDao


class TripRepository constructor(
    private var tripDao: TripDao
) {

    suspend fun getAllTrips(): List<Trip> {
        return tripDao.loadAllData()
    }

    suspend fun insertTrips(trips: List<Trip>) {
        tripDao.insert(trips)
    }

}
