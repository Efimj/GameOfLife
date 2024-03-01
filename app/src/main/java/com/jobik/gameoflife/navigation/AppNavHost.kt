package com.jobik.gameoflife.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jobik.gameoflife.screens.Game.GameScreen
import com.jobik.gameoflife.screens.Onboarding.OnboardingScreen

@Composable
fun AppNavHost(
    drawerState: DrawerState,
    navController: NavHostController,
    startDestination: String = NavigationItem.Game.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Game.route) {
            GameScreen(drawerState)
        }
        composable(NavigationItem.Onboarding.route) {
            OnboardingScreen(navController)
        }
        composable(NavigationItem.Settings.route) {

        }
    }
}