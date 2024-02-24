package com.jobik.gameoflife.screens.Onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jobik.gameoflife.R

sealed class OnboardingScreen(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    data object AboutGameOfLife : OnboardingScreen(
        image = R.drawable.ic_launcher_foreground,
        title = R.string.header_small,
        description = R.string.text_small,
    )

    data object AboutGameOfLife2 : OnboardingScreen(
        image = R.drawable.ic_launcher_foreground,
        title = R.string.header_medium,
        description = R.string.text_medium,
    )

    data object AboutGameOfLife3 : OnboardingScreen(
        image = R.drawable.ic_launcher_foreground,
        title = R.string.header_large,
        description = R.string.text_large,
    )

    data object AboutGameOfLife4 : OnboardingScreen(
        image = R.drawable.ic_launcher_foreground,
        title = R.string.header_small,
        description = R.string.text_large,
    )

    object PageList {
        val PageList = listOf(AboutGameOfLife, AboutGameOfLife2, AboutGameOfLife3, AboutGameOfLife4)
        val Count = PageList.size
    }
}