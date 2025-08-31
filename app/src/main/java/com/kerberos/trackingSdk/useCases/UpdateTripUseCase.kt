package com.kerberos.trackingSdk.useCases


import com.kerberos.trackingSdk.orm.Trip
import com.kerberos.trackingSdk.repositories.repositories.TripRepository

class UpdateTripUseCase(
    private val tripRepository: TripRepository
) {
    suspend fun execute(trip: Trip) {
        tripRepository.updateTrip(trip)
    }
}
