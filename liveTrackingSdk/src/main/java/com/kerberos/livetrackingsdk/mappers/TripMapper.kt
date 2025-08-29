package com.kerberos.livetrackingsdk.mappers

import com.kerberos.livetrackingsdk.models.TripModel
import com.kerberos.livetrackingsdk.orm.Trip

fun Trip.toTripModel(): TripModel {
    return TripModel(
        id = this.id,
        startTime = this.startTime,
        endTime = this.endTime,
        tripDuration = this.tripDuration,
        totalDistance = this.totalDistance,
        isActive = this.isActive
    )
}

fun TripModel.toTrip(): Trip {
    return Trip(
        id = this.id,
        startTime = this.startTime,
        endTime = this.endTime,
        tripDuration = this.tripDuration,
        totalDistance = this.totalDistance,
        isActive = this.isActive
    )
}
