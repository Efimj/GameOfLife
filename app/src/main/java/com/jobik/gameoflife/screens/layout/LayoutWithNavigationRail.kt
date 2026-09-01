package com.jobik.gameoflife.screens.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.gameoflife.BuildConfig
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.AppNavHost
import com.jobik.gameoflife.navigation.NavigationHelper
import com.jobik.gameoflife.navigation.NavigationHelper.Companion.navigateToTopLevel
import com.jobik.gameoflife.ui.helpers.*
import kotlinx.coroutines.launch

@Composable
fun LayoutWithNavigationRail(
    navController: NavHostController,
    modalDrawer: ModalDrawer = ModalDrawerImplementation
) {
    val context = LocalContext.current
    val currentDestination =
        navController.currentBackStackEntryAsState().value?.destination
    val hasAnyPurchase = flavorHasAnyPurchase()

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
                val coroutineScope = rememberCoroutineScope()
                TopWindowInsetsSpacer()

                Image(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(top = 10.dp)
                        .size(80.0.dp)
                        .padding(horizontal = 10.dp),
                    painter = painterResource(id = R.drawable.ic_app),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)

                )

                Column {
                    for (button in DrawerParams.drawerButtons) {
                        val itemColors = if (button.isSupportItem) {
                            NavigationRailItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.tertiary,
                            )
                        } else {
                            NavigationRailItemDefaults.colors()
                        }
                        NavigationRailItem(
                            selected = currentDestination?.hierarchy?.any {
                                it.hasRoute(button.route::class)
                            } == true,
                            onClick = {
                                coroutineScope.launch {
                                    modalDrawer.drawerState.open()
                                }
                                navController.navigateToTopLevel(button.route)
                            },
                            colors = itemColors,
                            icon = {
                                if (button.isSupportItem && hasAnyPurchase) {
                                    Box(modifier = Modifier.size(40.dp)) {
                                        Icon(
                                            modifier = Modifier.align(Alignment.Center),
                                            imageVector = button.icon,
                                            contentDescription = stringResource(button.description),
                                        )
                                        Icon(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .size(16.dp),
                                            imageVector = Icons.Filled.Verified,
                                            contentDescription = stringResource(button.title),
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = button.icon,
                                        contentDescription = stringResource(
                                            id = button.description
                                        )
                                    )
                                }
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
                startDestination = NavigationHelper.findStartDestination(context = context)
            )
        }
    }
}
