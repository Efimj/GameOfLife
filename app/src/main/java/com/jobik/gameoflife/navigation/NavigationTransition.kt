package com.jobik.gameoflife.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry
import com.jobik.gameoflife.navigation.NavigationHelper.Companion.getScreen
import com.jobik.gameoflife.navigation.NavigationHelper.Companion.mainScreenList

class NavigationTransition {
    fun mainScreenEnterTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition? {
        val initial: Screen = scope.initialState.getScreen() ?: return null
        val target: Screen = scope.targetState.getScreen() ?: return null

        if (mainScreenList.any { it.name == initial.name }.not()) {
            return null
        }

        val initialIndex = mainScreenList.indexOfFirst { it.name == initial.name }
        val targetIndex = mainScreenList.indexOfFirst { it.name == target.name }

        if (initialIndex < 0 || targetIndex < 0) return null

        if (initialIndex > targetIndex) {
            return slideInVertically { -it }
        } else if (initialIndex < targetIndex) {
            return slideInVertically { it }
        }

        return null
    }

    fun mainScreenExitTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition? {
        val initial: Screen = scope.initialState.getScreen() ?: return null
        val target: Screen = scope.targetState.getScreen() ?: return null

        if (mainScreenList.any { it.name == target.name }.not()) {
            return null
        }

        val initialIndex = mainScreenList.indexOfFirst { it.name == initial.name }
        val targetIndex = mainScreenList.indexOfFirst { it.name == target.name }

        if (initialIndex < 0 || targetIndex < 0) return null

        if (initialIndex > targetIndex) {
            return slideOutVertically { it }
        } else if (initialIndex < targetIndex) {
            return slideOutVertically { -it }
        }

        return null
    }
}