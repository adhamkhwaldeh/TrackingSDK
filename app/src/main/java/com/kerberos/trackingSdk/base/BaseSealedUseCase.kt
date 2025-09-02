package com.kerberos.trackingSdk.base

import com.kerberos.trackingSdk.base.states.BaseState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

/**
 * Base sealed use case
 *
 * @param Type
 * @param Params
 * @constructor Create empty Base sealed use case
 */
abstract class BaseSealedUseCase<out Type, in Params> :
    BaseUseCase<BaseState<Type>, Params>() where Type : Any?