package com.kerberos.trackingSdk.useCases

import android.location.Location
import com.kerberos.trackingSdk.base.BaseSealedUseCase
import com.kerberos.trackingSdk.base.states.BaseState
import com.kerberos.trackingSdk.models.TripTrackModel
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddCurrentTripTrackUseCase(
    private val tripRepository: TripRepository
) : BaseSealedUseCase<TripTrackModel, Location>() {
    override suspend fun invoke(params: Location): Flow<BaseState<TripTrackModel>> {
        return flow {
//            val trip = tripRepository.getActiveTrip()
//            trip?.let {
//                val tripTrack = TripTrackModel(
//                    tripId = it.id,
//                    latitude = params.latitude,
//                    longitude = params.longitude,
//                    speed = params.speed.toDouble(),
//                    timestamp = params.time
//                )
//                tripRepository.createTripTrack(tripTrack)
//                emit(tripTrack)
//            }
        }
    }

}