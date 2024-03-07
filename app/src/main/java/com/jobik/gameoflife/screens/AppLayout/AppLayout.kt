package com.jobik.gameoflife.screens.AppLayout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jobik.gameoflife.navigation.AppNavHost
import com.jobik.gameoflife.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.gameoflife.navigation.NavigationHelpers.Companion.findStartDestination
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass
import kotlinx.coroutines.launch

/**
 * Different type of navigation supported by app depending on device size and state.
 */
enum class AppNavigationType {
    DRAWER_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

@Composable
fun AppLayout(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    val context = LocalContext.current

    val navigationType: AppNavigationType

    when (currentWidthSizeClass()) {
        WindowWidthSizeClass.Compact -> {
            navigationType = AppNavigationType.DRAWER_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = AppNavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = AppNavigationType.PERMANENT_NAVIGATION_DRAWER
        }

        else -> {
            navigationType = AppNavigationType.DRAWER_NAVIGATION
        }
    }

    Surface {
        when (navigationType) {
            AppNavigationType.DRAWER_NAVIGATION -> {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        AppDrawerContent(
                            navController = navController,
                            drawerState = drawerState,
                        )
                    }
                ) {
                    AppNavHost(
                        drawerState = drawerState,
                        navController = navController,
                        startDestination = findStartDestination(context = context)
                    )
                }
            }

            AppNavigationType.NAVIGATION_RAIL -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    NavigationRail(modifier = Modifier.fillMaxHeight()) {
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
                        val coroutineScope = rememberCoroutineScope()

                        for (button in DrawerParams.drawerButtons) {
                            NavigationRailItem(
                                selected = button.route.name == currentRoute,
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                    if (button.route.name == currentRoute) return@NavigationRailItem
                                    if (navController.canNavigate().not()) return@NavigationRailItem
                                    navController.navigate(button.route.name) {
                                        // pops the route to root and places new screen
                                        popUpTo(button.route.name)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = button.icon,
                                        contentDescription = stringResource(
                                            id = button.description
                                        )
                                    )
                                }
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        AppNavHost(
                            drawerState = drawerState,
                            navController = navController,
                            startDestination = findStartDestination(context = context)
                        )
                    }
                }
            }

            AppNavigationType.PERMANENT_NAVIGATION_DRAWER -> {

            }
        }

    }
}