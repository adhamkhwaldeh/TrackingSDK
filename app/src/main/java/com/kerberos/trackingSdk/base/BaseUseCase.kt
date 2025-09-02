package com.kerberos.trackingSdk.base

import kotlinx.coroutines.flow.Flow

abstract class BaseUseCase<out Type, in Params> where Type : Any? {

    abstract suspend operator fun invoke(params: Params): Flow<Type>

}