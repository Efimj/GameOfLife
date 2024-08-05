package com.jobik.gameoflife.screens.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.gameoflife.BuildConfig
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.AppNavHost
import com.jobik.gameoflife.navigation.NavigationHelpers
import com.jobik.gameoflife.navigation.NavigationHelpers.Companion.canNavigate
import com.jobik.gameoflife.ui.helpers.*
import kotlinx.coroutines.launch

@Composable
fun LayoutWithNavigationRail(navController: NavHostController, modalDrawer: ModalDrawer = ModalDrawerImplementation) {
    val context = LocalContext.current

    Row(modifier = Modifier.fillMaxSize()) {
        NavigationRail(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            windowInsets = WindowInsets.ime
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .startWindowInsetsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
                val coroutineScope = rememberCoroutineScope()
                TopWindowInsetsSpacer()

                Image(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(top = 10.dp)
                        .size(80.0.dp)
                        .padding(horizontal = 10.dp),
                    painter = painterResource(id = R.drawable.icon_for_tint),
                    contentDescription = "Main app icon",
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)

                )

                Column {
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
                Spacer(modifier = Modifier.weight(1f, fill = true))
                Row(
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "v${BuildConfig.VERSION_NAME}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                BottomWindowInsetsSpacer()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalWindowInsetsPadding()
                .endWindowInsetsPadding()
                .padding(vertical = 20.dp)
                .padding(end = 20.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AppNavHost(
                navController = navController,
                startDestination = NavigationHelpers.findStartDestination(context = context)
            )
        }
    }
}