package com.kerberos.livetrackingsdk.base

import com.adham.gini.weatherginisdk.base.states.BaseState
import kotlinx.coroutines.*

/**
 * Base sealed use case
 *
 * @param Type
 * @param Params
 * @property ioScope
 * @property main
 * @constructor Create empty Base sealed use case
 */
abstract class BaseSealedUseCase<out Type, in Params>(
    private val ioScope: CoroutineScope,
    private val main: CoroutineDispatcher = Dispatchers.Main,
) where Type : Any? {
    /**
     * Run
     *
     * @param params
     * @return
     */
    abstract suspend fun run(params: Params): BaseState<Type>?

    /**
     * Invoke
     *
     * @param params
     * @param onResult
     * @receiver
     */
    operator fun invoke(
        params: Params,
        onResult: (BaseState<Type>) -> Unit = {},
    ) {
        ioScope.launch {
            val result = async { run(params) }
            withContext(main) {
                result.await()?.let { onResult(it) }
            }
        }
    }

    /**
     * None
     *
     * @constructor Create empty None
     */
    @Suppress("unused")
    class None
}
