package com.kerberos.livetrackingsdk.factories

import com.google.gson.Gson
import com.kerberos.livetrackingsdk.importer.CsvTripImporterExporter
import com.kerberos.livetrackingsdk.importer.JsonTripImporterExporter
import com.kerberos.livetrackingsdk.repositories.repositories.TripRepository
import com.kerberos.livetrackingsdk.useCases.ExportTripsUseCase
import com.kerberos.livetrackingsdk.useCases.ImportTripsUseCase

class TripUseCaseFactory(private val tripRepository: TripRepository, private val gson: Gson) {

    fun createImportUseCase(type: String): ImportTripsUseCase {
        val importer = when (type) {
            "csv" -> CsvTripImporterExporter()
            "json" -> JsonTripImporterExporter(gson)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
        return ImportTripsUseCase(tripRepository, importer)
    }

    fun createExportUseCase(type: String): ExportTripsUseCase {
        val exporter = when (type) {
            "csv" -> CsvTripImporterExporter()
            "json" -> JsonTripImporterExporter(gson)
            else -> throw IllegalArgumentException("Unknown type: $type")
        }
        return ExportTripsUseCase(tripRepository, exporter)
    }
}
