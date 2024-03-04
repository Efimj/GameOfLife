package com.jobik.gameoflife.screens.AppLayout

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jobik.gameoflife.BuildConfig
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.navigation.canNavigate
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.TopWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.startWindowInsetsPadding
import kotlinx.coroutines.launch

/**
 * base data container for the button creation
 * takes in the resources IDs
 */
data class AppDrawerItemInfo(
    val route: Screen,
    @StringRes val title: Int,
    val icon: ImageVector,
    @StringRes val description: Int,
    val enabled: Boolean = false,
)


/**
 * list of the buttons
 */
object DrawerParams {
    val drawerButtons = listOf(
        AppDrawerItemInfo(
            route = Screen.Game,
            title = R.string.GameOfLife,
            icon = Icons.Outlined.Casino,
            description = R.string.drawer_GameOfLife_description,
        ),
        AppDrawerItemInfo(
            route = Screen.Information,
            title = R.string.information,
            icon = Icons.AutoMirrored.Outlined.HelpOutline,
            description = R.string.information,
        ),
        AppDrawerItemInfo(
            route = Screen.Settings,
            title = R.string.Settings,
            icon = Icons.Outlined.Settings,
            description = R.string.drawer_Settings_description,
        ),
    )
}

/**
 * T for generic type to be used for the picking
 */
@Composable
fun AppDrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: ""
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet(windowInsets = WindowInsets.ime) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .startWindowInsetsPadding()
                    .widthIn(max = 280.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TopWindowInsetsSpacer()
                Image(
                    modifier = Modifier
                        .size(150.dp)
                        .padding(vertical = 20.dp),
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Main app icon"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(bottom = 5.dp),
                    text = stringResource(id = R.string.GameOfLife),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {
                    for (button in DrawerParams.drawerButtons) {
                        AppDrawerItem(
                            title = button.title,
                            contentDescription = button.description,
                            icon = button.icon,
                            isActive = button.route.name == currentRoute,
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.close()
                                }
                                if (button.route.name == currentRoute) return@AppDrawerItem
                                if (navController.canNavigate().not()) return@AppDrawerItem
                                navController.navigate(button.route.name) {
                                    // pops the route to root and places new screen
                                    popUpTo(button.route.name)
                                }
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f, fill = true))
                Row(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .padding(horizontal = 10.dp),
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
    }
}

@Composable
fun AppDrawerItem(
    icon: ImageVector,
    @StringRes title: Int,
    @StringRes contentDescription: Int,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val backgroundColorValue = if (isActive) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    val backgroundColor by animateColorAsState(targetValue = backgroundColorValue, label = "backgroundColor")

    val contentColorValue =
        if (isActive) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
    val contentColor by animateColorAsState(targetValue = contentColorValue, label = "backgroundColor")

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = CircleShape,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = contentDescription),
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}