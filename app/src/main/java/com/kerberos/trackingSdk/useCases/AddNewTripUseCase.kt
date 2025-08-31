package com.kerberos.trackingSdk.useCases

import com.kerberos.trackingSdk.base.BaseSealedUseCase
import com.kerberos.trackingSdk.orm.Trip
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class AddNewTripUseCase
@Inject
constructor(
    private val tripRepository: TripRepository
) : BaseSealedUseCase<Unit, Trip, String>() {

    override suspend fun execute(params: Unit): Flow<Trip> {
        return flow {
            val trip = Trip(
                startTime = Date().time,
                isActive = true
            )
            val id = tripRepository.createTrip(trip)
            emit(tripRepository.getTrip(id)!!)
        }
    }
}