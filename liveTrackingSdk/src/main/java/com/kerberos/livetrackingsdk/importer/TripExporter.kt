package com.kerberos.livetrackingsdk.importer

import com.kerberos.livetrackingsdk.models.TripModel
import java.io.OutputStream

interface TripExporter {
    suspend fun export(trips: List<TripModel>, outputStream: OutputStream)
}
