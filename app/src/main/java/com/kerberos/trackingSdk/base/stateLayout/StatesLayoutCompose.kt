package com.kerberos.trackingSdk.base.stateLayout


import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.platform.LocalContext
import com.kerberos.livetrackingsdk.base.states.BaseState
import com.kerberos.trackingSdk.base.stateLayout.defaultStates.InternalServerErrorCompose
import com.kerberos.trackingSdk.base.stateLayout.defaultStates.NoInternetConnectionCompose
import com.kerberos.trackingSdk.base.stateLayout.defaultStates.NotAuthorizedCompose
import com.kerberos.trackingSdk.base.stateLayout.defaultStates.Progress


/**
 * States layout compose
 *
 * @param T
 * @param modifier
 * @param baseState
 * @param customContent
 * @param customAction
 * @param content
 */
@Composable
fun <T> StatesLayoutCompose(
    modifier: Modifier = Modifier,
    baseState: BaseState<T>,
    customContent: StatesLayoutCustomInterface? = null,
    customAction: StatesLayoutCustomActionInterface? = null,
//    content: (@Composable @UiComposable (featureState: BaseState.FeaturedState<T>) -> Unit)? = null,
    content: (@Composable @UiComposable (data: T) -> Unit)? = null,
) {
    Column(modifier = modifier) {
        when (baseState) {
            is BaseState.Initial -> {
                Box {

                }
            }

            is BaseState.Loading -> {
                Progress(value = true)
            }

            is BaseState.LoadingDismiss -> {
                Progress(value = false)
            }

            is BaseState.InternalServerError -> {
                customContent?.internalServerError()
                    ?: InternalServerErrorCompose(baseState.errorMessage)
            }

            is BaseState.InvalidData -> {
                customContent?.invalidData() ?: Box {}
            }

            is BaseState.NoAuthorized -> {
                customContent?.notAuthorized() ?: NotAuthorizedCompose()
            }

            is BaseState.NoInternetError -> {
                customContent?.noInternetError() ?: NoInternetConnectionCompose {
                    customAction?.retry()
                }
            }

            is BaseState.NotDataFound -> {
                customContent?.noContent() ?: Box {}
            }

            is BaseState.ValidationError -> {
                customContent?.validationError() ?: Box {
                    val context = LocalContext.current
                    LaunchedEffect(key1 = true) {
                        Toast.makeText(
                            context,
                            if (baseState.errorId != 0) context.getString(baseState.errorId)
                            else baseState.responseMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }

            is BaseState.BaseStateLoadedSuccessfully -> {
                Box {
                    content?.let { it(baseState.data) }
                }
            }

        }
    }
}