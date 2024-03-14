package com.jobik.gameoflife.services.rate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

const val numberOfStart = 3

@Composable
fun RateDialogProvider() {
    val isOpen = remember { mutableStateOf(false) }

    val counter = rememberSaveable { mutableIntStateOf(0) }

    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        counter.intValue++
        if (counter.intValue % numberOfStart == 0) {
            isOpen.value = true
        }
    }

    RateDialog(isOpen = isOpen)
}