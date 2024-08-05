package com.jobik.gameoflife.screens.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jobik.gameoflife.R

sealed class Onboarding(
    @DrawableRes
    val images: List<Int>,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {
    data object GameOfLife : Onboarding(
        images = listOf(R.drawable.john_horton_conway),
        title = R.string.onb_1_headline,
        description = R.string.onb_1_description,
    )

    data object Rule1 : Onboarding(
        images = listOf(
            R.drawable.generative_combination_step_1,
            R.drawable.generative_combination_step_2,
            R.drawable.generative_combination_step_3,
            R.drawable.generative_combination_step_4,
            R.drawable.generative_combination_step_5,
            R.drawable.generative_combination_step_6
        ),
        title = R.string.onb_2_headline,
        description = R.string.onb_2_description,
    )

    data object Rule2 : Onboarding(
        images = listOf(
            R.drawable.end_combination_step_1,
            R.drawable.end_combination_step_2,
            R.drawable.end_combination_step_3,
            R.drawable.end_combination_step_4,
            R.drawable.end_combination_step_5,
            R.drawable.end_combination_step_6,
            R.drawable.end_combination_step_7,
            R.drawable.end_combination_step_8,
            R.drawable.end_combination_step_9,
        ),
        title = R.string.onb_3_headline,
        description = R.string.onb_3_description,
    )

    object PageList {
        val PageList = listOf(GameOfLife, Rule1, Rule2)

        val Count = PageList.size
    }
}