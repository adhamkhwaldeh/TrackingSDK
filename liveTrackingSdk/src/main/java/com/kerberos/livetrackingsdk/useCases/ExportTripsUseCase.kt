package com.kerberos.livetrackingsdk.useCases

import com.kerberos.livetrackingsdk.importer.TripExporter
import com.kerberos.livetrackingsdk.mappers.toTripModel
import com.kerberos.livetrackingsdk.repositories.repositories.TripRepository
import java.io.OutputStream

class ExportTripsUseCase(
    private val tripRepository: TripRepository,
    private val tripExporter: TripExporter
) {
    suspend fun execute(outputStream: OutputStream) {
        val trips = tripRepository.getAllTrips().map { it.toTripModel() }
        tripExporter.export(trips, outputStream)
    }
}
