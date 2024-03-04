package com.jobik.gameoflife.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

class NavigationHelpers {
    companion object{
        fun NavHostController.canNavigate(): Boolean {
            return this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
        }
    }
}