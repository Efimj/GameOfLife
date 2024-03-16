package com.jobik.gameoflife.services.rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.jobik.gameoflife.services.app.AppCounter

const val numberOfStart = 3

@Composable
fun RateDialogProvider() {
    val context = LocalContext.current
    val isOpen = rememberSaveable { mutableStateOf(false) }

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        val numberOnCreate = AppCounter(context).getOnCreateNumber()
        if (numberOnCreate % numberOfStart == 0)
            isOpen.value = true
    }

    RateDialog(isOpen = isOpen, onCancel = { isOpen.value = false }, onRate = { isOpen.value = false })
}