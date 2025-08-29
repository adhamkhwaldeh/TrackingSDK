package com.kerberos.livetrackingsdk.repositories.repositories


import com.kerberos.livetrackingsdk.models.TripModel
import com.kerberos.livetrackingsdk.orm.TripDao
import kotlinx.coroutines.flow.Flow


class TripRepository constructor(
    private var tripDao: TripDao
) {

//    suspend fun createUser(albumModel: TripModel): Flow<BaseState<TripModel>> {
//        return requestBlockingById { apiService.createUser(albumModel) }
//    }
//
//    suspend fun deleteUser(userId: Int): Flow<BaseState<Any?>> {
//        return requestBlockingById<Any?>(flowable = { apiService.deleteUser(userId) })
//    }

}
