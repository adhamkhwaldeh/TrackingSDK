package com.kerberos.trackingSdk.useCases


import com.kerberos.trackingSdk.orm.Trip
import com.kerberos.trackingSdk.repositories.repositories.TripRepository

class DeleteTripUseCase(
    private val tripRepository: TripRepository
) {
    suspend fun execute(trip: Trip) {
        tripRepository.deleteTrip(trip)
    }
}
