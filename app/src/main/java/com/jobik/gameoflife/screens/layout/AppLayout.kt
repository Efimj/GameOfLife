package com.jobik.gameoflife.screens.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass
import com.jobik.gameoflife.util.SnackbarHostUtil.snackbarHostState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.helpers.bottomWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding


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

        SnackbarProvider()
    }
}

@Composable
private fun SnackbarProvider() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .bottomWindowInsetsPadding()
            .horizontalWindowInsetsPadding()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
        ) {
            Snackbar(
                snackbarData = it,
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                actionColor = MaterialTheme.colorScheme.secondaryContainer,
                dismissActionContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = .8f)
            )
        }
    }
}

