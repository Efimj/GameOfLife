package com.jobik.gameoflife.screens.AppLayout

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jobik.gameoflife.navigation.AppNavHost
import com.jobik.gameoflife.navigation.NavigationHelpers.Companion.findStartDestination
import com.jobik.gameoflife.navigation.NavigationItem
import com.jobik.gameoflife.navigation.Screen

@Composable
fun AppLayout(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
) {
    val context = LocalContext.current

    Surface {
        // the main drawer composable, which creates the actual drawer
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
}