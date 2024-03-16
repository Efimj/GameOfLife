package com.jobik.gameoflife.services.rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.jobik.gameoflife.services.app.AppCounter

private const val numberOfStart = 7

@Composable
fun RateDialogProvider() {
    val context = LocalContext.current
    val isOpen = rememberSaveable { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        val rateService = RateService(context)
        val numberOnCreate = AppCounter(context).getOnCreateNumber()
        if (rateService.getCanAskRate() && numberOnCreate % numberOfStart == 0)
            isOpen.value = true
    }

    val onRate = { dontAskAgain: Boolean ->
        val rateService = RateService(context)
        rateService.openAppRating()
        isOpen.value = false
        if (dontAskAgain)
            rateService.updateCanAskRate(false)
    }

    val onCancel = { dontAskAgain: Boolean ->
        val rateService = RateService(context)
        isOpen.value = false
        if (dontAskAgain)
            rateService.updateCanAskRate(false)
    }

    RateDialog(
        isOpen = isOpen,
        onCancel = onCancel,
        onRate = onRate
    )
}