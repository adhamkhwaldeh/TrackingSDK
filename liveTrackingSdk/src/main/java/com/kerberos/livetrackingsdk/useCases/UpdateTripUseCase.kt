package com.kerberos.livetrackingsdk.useCases

import com.kerberos.livetrackingsdk.orm.Trip
import com.kerberos.livetrackingsdk.repositories.repositories.TripRepository

class UpdateTripUseCase(
    private val tripRepository: TripRepository
) {
    suspend fun execute(trip: Trip) {
        tripRepository.updateTrip(trip)
    }
}
