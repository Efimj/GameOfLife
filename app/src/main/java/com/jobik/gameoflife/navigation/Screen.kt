package com.jobik.gameoflife.navigation

import androidx.annotation.Keep

@Keep
enum class Screen {
    Game,
    Onboarding,
    Settings,
}

sealed class NavigationItem(val route: String) {
    object Game : NavigationItem(Screen.Game.name)
    object Onboarding : NavigationItem(Screen.Onboarding.name)
    object Settings : NavigationItem(Screen.Settings.name)
}