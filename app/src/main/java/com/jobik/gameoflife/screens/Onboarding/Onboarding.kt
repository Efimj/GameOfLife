package com.jobik.gameoflife.screens.Onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jobik.gameoflife.R

sealed class Onboarding(
    @DrawableRes
    val image: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    data object GameOfLife : Onboarding(
        image = R.drawable.john_horton_conway,
        title = R.string.onb_1_headline,
        description = R.string.onb_1_description,
    )

    data object Rule1 : Onboarding(
        image = R.drawable.generative_combination,
        title = R.string.onb_2_headline,
        description = R.string.onb_2_description,
    )

    data object Rule2 : Onboarding(
        image = R.drawable.end_combination,
        title = R.string.onb_3_headline,
        description = R.string.onb_3_description,
    )

    object PageList {
        val PageList = listOf(GameOfLife, Rule1, Rule2)
        val Count = PageList.size
    }
}