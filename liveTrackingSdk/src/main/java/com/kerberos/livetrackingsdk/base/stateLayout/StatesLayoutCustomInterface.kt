package com.kerberos.livetrackingsdk.base.stateLayout

import androidx.compose.runtime.Composable
import androidx.compose.ui.UiComposable

/**
 * States layout custom interface
 *
 * @constructor Create empty States layout custom interface
 */
interface StatesLayoutCustomInterface {
    /**
     * No content
     *
     * @return
     */
    fun noContent(): (@Composable @UiComposable () -> Unit)? = null

    /**
     * Internal server error
     *
     * @return
     */
    fun internalServerError(): (@Composable @UiComposable () -> Unit)? = null

    /**
     * Invalid data
     *
     * @return
     */
    fun invalidData(): (@Composable @UiComposable () -> Unit)? = null

    /**
     * Not authorized
     *
     * @return
     */
    fun notAuthorized(): (@Composable @UiComposable () -> Unit)? = null

    /**
     * No internet error
     *
     * @return
     */
    fun noInternetError(): (@Composable @UiComposable () -> Unit)? = null

    /**
     * Validation error
     *
     * @return
     */
    fun validationError(): (@Composable @UiComposable () -> Unit)? = null

}