package com.jobik.gameoflife.navigation

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Screen(val name: String) {

    @Serializable
    data object Onboarding : Screen(name = "Onboarding")

    @Serializable
    data object Game : Screen(name = "Game")

    @Serializable
    data object Information : Screen(name = "Information")

    @Serializable
    data object Settings : Screen(name = "Settings")
}