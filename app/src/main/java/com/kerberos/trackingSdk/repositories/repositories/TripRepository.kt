package com.kerberos.trackingSdk.repositories.repositories

import com.kerberos.trackingSdk.orm.Trip
import com.kerberos.trackingSdk.orm.TripDao


class TripRepository(
    private var tripDao: TripDao
) {

    suspend fun getAllTrips(): List<Trip> {
        return tripDao.loadAllData()
    }

    suspend fun insertTrips(trips: List<Trip>) {
        tripDao.insert(trips)
    }

    suspend fun updateTrip(trip: Trip) {
        tripDao.update(trip)
    }

    suspend fun deleteTrip(trip: Trip) {
        tripDao.delete(trip)
    }

}
