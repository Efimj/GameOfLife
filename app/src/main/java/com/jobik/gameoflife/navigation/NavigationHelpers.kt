package com.jobik.gameoflife.navigation

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import com.jobik.gameoflife.SharedPreferencesKeys

class NavigationHelpers {
    companion object {
        fun NavHostController.canNavigate(): Boolean {
            return this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
        }

        fun findStartDestination(context: Context): String {
            val sharedPreferences =
                context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
            val default = Screen.Onboarding.name
            val finishedData = sharedPreferences.getString(SharedPreferencesKeys.CurrentOnboardingFinishedData, default)
            return if (finishedData == SharedPreferencesKeys.OnboardingFinishedData) {
                Screen.Game.name
            } else {
                Screen.Onboarding.name
            }
        }
    }
}