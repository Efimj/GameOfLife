package com.jobik.gameoflife.navigation

import androidx.annotation.Keep

@Keep
enum class Screen {
    Onboarding,
    Game,
}
sealed class NavigationItem(val route: String) {
    object Onboarding : NavigationItem(Screen.Onboarding.name)
    object Game : NavigationItem(Screen.Game.name)
}