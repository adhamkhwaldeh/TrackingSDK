package com.kerberos.trackingSdk.useCases

import com.kerberos.trackingSdk.base.BaseSealedUseCase
import com.kerberos.trackingSdk.base.states.BaseState
import com.kerberos.trackingSdk.orm.Trip
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class AddNewTripUseCase(
    private val tripRepository: TripRepository
) : BaseSealedUseCase<Trip, Unit>() {

    override suspend fun invoke(params: Unit): Flow<BaseState<Trip>> {
        return flow {
            val trip = Trip(
                startTime = Date().time,
                isActive = true
            )
//            val id = tripRepository.createTrip(trip)
//            emit(tripRepository.getTrip(id)!!)
        }
    }
}