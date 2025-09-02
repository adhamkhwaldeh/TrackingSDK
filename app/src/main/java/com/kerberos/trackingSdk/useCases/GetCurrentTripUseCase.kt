package com.kerberos.trackingSdk.useCases

import com.kerberos.trackingSdk.base.BaseFlowUseCase
import com.kerberos.trackingSdk.base.BaseUseCase
import com.kerberos.trackingSdk.models.TripModel
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentTripUseCase(
    private val tripRepository: TripRepository
) : BaseFlowUseCase<TripModel?, Void?>() {

    override fun invoke(params: Void?): Flow<TripModel?> {
        return tripRepository.getActiveTrip()
    }
}