package com.jobik.gameoflife.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Screen {

    @Serializable
    data object Onboarding : Screen()

    @Serializable
    data object Game : Screen()

    @Serializable
    data object Information : Screen()

    @Serializable
    data object Settings : Screen()

    val values = listOf(Onboarding, Game, Information, Settings)
}