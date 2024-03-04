package com.jobik.gameoflife.navigation

import androidx.annotation.Keep

@Keep
enum class Screen {
    Onboarding,
    Game,
    Information,
    Settings,
}

sealed class NavigationItem(val route: String) {
    object Onboarding : NavigationItem(Screen.Onboarding.name)
    object Game : NavigationItem(Screen.Game.name)
    object Information : NavigationItem(Screen.Information.name)
    object Settings : NavigationItem(Screen.Settings.name)

    companion object {
        val List = listOf(Onboarding, Game, Information, Settings)
    }
}