package com.jobik.gameoflife.navigation

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.jobik.gameoflife.SharedPreferencesKeys
import com.jobik.gameoflife.navigation.Screen.Game
import com.jobik.gameoflife.navigation.Screen.Information
import com.jobik.gameoflife.navigation.Screen.Onboarding
import com.jobik.gameoflife.navigation.Screen.Settings

class NavigationHelper {
    companion object {
        val screenList = listOf(Onboarding, Game, Information, Settings)
        val mainScreenList = listOf(Game, Information, Settings)

        fun NavHostController.canNavigate(): Boolean {
            return this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
        }

        fun findStartDestination(context: Context): Screen {
            val sharedPreferences =
                context.getSharedPreferences(
                    SharedPreferencesKeys.AppSettings,
                    Context.MODE_PRIVATE
                )
            val default = Screen.Onboarding
            val finishedData = sharedPreferences.getString(
                SharedPreferencesKeys.CurrentOnboardingFinishedData,
                null
            ) ?: default
            return if (finishedData == SharedPreferencesKeys.OnboardingFinishedData) {
                Screen.Game
            } else {
                Screen.Onboarding
            }
        }

        /**
         * To get route
         */
        fun NavHostController.getScreen(): Screen? {
            val currentDestination = this.currentBackStackEntry?.destination ?: return null
            screenList.forEach { screen ->
                currentDestination.hierarchy.any {
                    it.hasRoute(screen::class)
                } == true && return screen
            }
            return null
        }

        fun NavBackStackEntry.getScreen(): Screen? {
            val currentDestination = this.destination
            screenList.forEach { screen ->
                currentDestination.hierarchy.any {
                    it.hasRoute(screen::class)
                } == true && return screen
            }
            return null
        }
    }
}