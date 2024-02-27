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
    data object GameOfLife : OnboardingScreen(
        image = R.drawable.john_horton_conway,
        title = R.string.onb_1_headline,
        description = R.string.onb_1_description,
    )

    data object Rule1 : OnboardingScreen(
        image = R.drawable.icon,
        title = R.string.onb_2_headline,
        description = R.string.onb_2_description,
    )

    data object Rule2 : OnboardingScreen(
        image = R.drawable.icon,
        title = R.string.onb_3_headline,
        description = R.string.onb_3_description,
    )

    object PageList {
        val PageList = listOf(GameOfLife, Rule1, Rule2)
        val Count = PageList.size
    }
}