package com.kerberos.trackingSdk.useCases

import com.kerberos.trackingSdk.importer.TripExporter
import com.kerberos.trackingSdk.mappers.toTripModel
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import java.io.OutputStream

class ExportTripsUseCase(
    private val tripRepository: com.kerberos.trackingSdk.repositories.repositories.TripRepository,
    private val tripExporter: com.kerberos.trackingSdk.importer.TripExporter
) {
    suspend fun execute(outputStream: OutputStream) {
        val trips = tripRepository.getAllTrips().map { it.toTripModel() }
        tripExporter.export(trips, outputStream)
    }
}
