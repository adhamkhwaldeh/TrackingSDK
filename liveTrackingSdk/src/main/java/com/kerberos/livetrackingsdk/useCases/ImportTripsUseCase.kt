package com.kerberos.livetrackingsdk.useCases

import com.kerberos.livetrackingsdk.importer.TripImporter
import com.kerberos.livetrackingsdk.mappers.toTrip
import com.kerberos.livetrackingsdk.repositories.repositories.TripRepository
import java.io.InputStream

class ImportTripsUseCase(
    private val tripRepository: TripRepository,
    private val tripImporter: TripImporter
) {
    suspend fun execute(inputStream: InputStream) {
        val trips = tripImporter.import(inputStream).map { it.toTrip() }
        tripRepository.insertTrips(trips)
    }
}
