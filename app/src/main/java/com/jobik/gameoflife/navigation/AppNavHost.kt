package com.jobik.gameoflife.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jobik.gameoflife.screens.game.GameScreen
import com.jobik.gameoflife.screens.lnformation.InformationScreen
import com.jobik.gameoflife.screens.onboarding.OnboardingScreen
import com.jobik.gameoflife.screens.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Screen = Screen.Game,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screen.Onboarding>() {
            OnboardingScreen(navController)
        }
        composable<Screen.Game>() {
            GameScreen()
        }
        composable<Screen.Information>() {
            InformationScreen()
        }
        composable<Screen.Settings>() {
            SettingsScreen(navController)
        }
    }
}