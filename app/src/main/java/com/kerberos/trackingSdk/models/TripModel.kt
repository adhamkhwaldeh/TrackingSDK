package com.kerberos.trackingSdk.models

data class TripModel(
    val id: Int,

    val startTime: Long,

    val endTime: Long?,

    val tripDuration: Long?,

    val totalDistance: Double?,

    val isActive: Boolean
)
