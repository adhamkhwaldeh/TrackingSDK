package com.kerberos.trackingSdk.useCases

import android.location.Location
import com.kerberos.trackingSdk.base.BaseSealedUseCase
import com.kerberos.trackingSdk.models.TripTrackModel
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AddCurrentTripTrackUseCase
@Inject
constructor(
    private val tripRepository: TripRepository
) : BaseSealedUseCase<Location, TripTrackModel, String>() {

    override suspend fun execute(params: Location): Flow<TripTrackModel> {
        return flow {
            val trip = tripRepository.getActiveTrip()
            trip?.let {
                val tripTrack = TripTrackModel(
                    tripId = it.id,
                    latitude = params.latitude,
                    longitude = params.longitude,
                    speed = params.speed.toDouble(),
                    timestamp = params.time
                )
                tripRepository.createTripTrack(tripTrack)
                emit(tripTrack)
            }
        }
    }
}