package com.kerberos.trackingSdk.useCases


import com.kerberos.trackingSdk.base.BaseSealedUseCase
import com.kerberos.trackingSdk.base.states.BaseState
import com.kerberos.trackingSdk.base.states.asBasState
import com.kerberos.trackingSdk.models.TripModel
import com.kerberos.trackingSdk.orm.Trip
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateTripUseCase(
    private val tripRepository: TripRepository
) : BaseSealedUseCase<Unit, TripModel>() {

    override suspend fun invoke(params: TripModel): Flow<BaseState<Unit>> {
        return flow {
            emit(tripRepository.updateTrip(params).asBasState())
        }
    }
}
