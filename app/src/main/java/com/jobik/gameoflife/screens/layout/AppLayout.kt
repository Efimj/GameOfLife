package com.jobik.gameoflife.screens.layout

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass

/**
 * Different type of navigation supported by app depending on device size and state.
 */
enum class AppNavigationType {
    DRAWER_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

@Composable
fun AppLayout(
    navController: NavHostController = rememberNavController(),
) {
    val navigationType: AppNavigationType = when (currentWidthSizeClass()) {
        WindowWidthSizeClass.Compact -> {
            AppNavigationType.DRAWER_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            AppNavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            AppNavigationType.PERMANENT_NAVIGATION_DRAWER
        }
    }

    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        when (navigationType) {
            AppNavigationType.DRAWER_NAVIGATION -> {
                LayoutWithModalDrawerSheet(navController = navController)
            }

            AppNavigationType.NAVIGATION_RAIL -> {
                LayoutWithNavigationRail(navController)
            }

            AppNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
                LayoutWithPermanentNavigationDrawer(navController = navController)
            }
        }
    }
}

