package com.kerberos.trackingSdk.base.states


/**
 * As bas state
 *
 * @param T
 * @return
 */
fun <T> Result<T>.asBasState(): BaseState<T> {
    return if (isSuccess && getOrNull() != null) {
        BaseState.BaseStateLoadedSuccessfully(getOrNull()!!)
    } else {
        BaseState.getStateByThrowable(exceptionOrNull()!!)
    }
}