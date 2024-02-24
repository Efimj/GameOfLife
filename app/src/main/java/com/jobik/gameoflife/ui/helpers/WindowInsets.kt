package com.jobik.gameoflife.ui.helpers

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.Dp

fun Modifier.topWindowInsetsPadding() = composed {
    this.padding(top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding())
}

fun Modifier.bottomWindowInsetsPadding() = composed {
    this.padding(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding())
}

@Composable
fun topWindowInsetsPadding(): Dp {
    return WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()
}

@Composable
fun bottomWindowInsetsPadding(): Dp {
    return WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
}

@Composable
fun TopWindowInsetsSpacer() =
    Spacer(
        Modifier.windowInsetsTopHeight(
            WindowInsets.safeDrawing
        )
    )

@Composable
fun BottomWindowInsetsSpacer() =
    Spacer(
        Modifier.windowInsetsBottomHeight(
            WindowInsets.safeDrawing
        )
    )
