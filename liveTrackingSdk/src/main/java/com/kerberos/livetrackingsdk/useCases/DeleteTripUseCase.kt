package com.kerberos.livetrackingsdk.useCases

import com.kerberos.livetrackingsdk.orm.Trip
import com.kerberos.livetrackingsdk.repositories.repositories.TripRepository

class DeleteTripUseCase(
    private val tripRepository: TripRepository
) {
    suspend fun execute(trip: Trip) {
        tripRepository.deleteTrip(trip)
    }
}
