package com.kerberos.trackingSdk.base

import kotlinx.coroutines.flow.Flow


abstract class BaseFlowUseCase<out Type, in Params> where Type : Any? {

    abstract operator fun invoke(params: Params): Flow<Type>

}