package com.jobik.gameoflife.screens.AppLayout

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun LayoutWithPermanentNavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState = DrawerState(DrawerValue.Closed),
) {
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val coroutineScope = rememberCoroutineScope()

    PermanentNavigationDrawer(
        drawerContent = {
            Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .startWindowInsetsPadding()
                        .padding(horizontal = 20.dp)
                        .widthIn(max = 240.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TopWindowInsetsSpacer()
                    Image(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .size(120.dp),
                        painter = painterResource(id = R.drawable.icon_for_tint),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        contentDescription = "Main app icon"
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        text = stringResource(id = R.string.GameOfLife),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Column {
                        DrawerParams.drawerButtons.forEach { button ->
                            NavigationDrawerItem(
                                icon = { Icon(button.icon, contentDescription = null) },
                                label = { Text(text = stringResource(id = button.title)) },
                                selected = button.route.name == currentRoute,
                                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = MaterialTheme.colorScheme.surfaceContainer),
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                    if (button.route.name == currentRoute) return@NavigationDrawerItem
                                    if (navController.canNavigate().not()) return@NavigationDrawerItem
                                    navController.navigate(button.route.name) {
                                        // pops the route to root and places new screen
                                        popUpTo(button.route.name)
                                    }
                                },
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
                            text = stringResource(id = R.string.app_version_text) + BuildConfig.VERSION_NAME,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    BottomWindowInsetsSpacer()
                }
            }
        },
        content = {
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
    )
}