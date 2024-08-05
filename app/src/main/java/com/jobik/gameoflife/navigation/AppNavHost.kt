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
    startDestination: String = NavigationItem.Game.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(NavigationItem.Game.route) {
            GameScreen()
        }
        composable(NavigationItem.Information.route) {
            InformationScreen()
        }
        composable(NavigationItem.Settings.route) {
            SettingsScreen(navController)
        }
    }
}