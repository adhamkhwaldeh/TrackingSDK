package com.kerberos.livetrackingsdk.importer

import com.kerberos.livetrackingsdk.models.TripModel
import java.io.InputStream

interface TripImporter {
    suspend fun import(inputStream: InputStream): List<TripModel>
}
