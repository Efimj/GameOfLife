package com.jobik.gameoflife.screens.AppLayout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.gameoflife.navigation.AppNavHost
import com.jobik.gameoflife.navigation.NavigationHelpers
import com.jobik.gameoflife.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.gameoflife.ui.helpers.endWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.verticalWindowInsetsPadding
import kotlinx.coroutines.launch

@Composable
fun LayoutWithNavigationRail(navController: NavHostController, modalDrawer: ModalDrawer = ModalDrawerImplementation) {
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            modifier = Modifier.fillMaxHeight(),
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
            val coroutineScope = rememberCoroutineScope()

            for (button in DrawerParams.drawerButtons) {
                NavigationRailItem(
                    selected = button.route.name == currentRoute,
                    onClick = {
                        coroutineScope.launch {
                            modalDrawer.drawerState.open()
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
                .verticalWindowInsetsPadding()
                .endWindowInsetsPadding()
                .padding(vertical = 20.dp)
                .padding(end = 20.dp)
        ) {
            AppNavHost(
                navController = navController,
                startDestination = NavigationHelpers.findStartDestination(context = context)
            )
        }
    }
}