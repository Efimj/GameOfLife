package com.jobik.gameoflife.screens.AppLayout

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import kotlinx.coroutines.launch

/**
 * base data container for the button creation
 * takes in the resources IDs
 */
data class AppDrawerItemInfo<T>(
    val drawerOption: T,
    @StringRes val title: Int,
    val icon: ImageVector,
    @StringRes val descriptionId: Int
)


/**
 * list of the buttons
 */
object DrawerParams {
    val drawerButtons = listOf(
        AppDrawerItemInfo(
            drawerOption = Screen.Game,
            title = R.string.GameOfLife,
            icon = Icons.Outlined.Casino,
            descriptionId = R.string.drawer_GameOfLife_description
        ),
        AppDrawerItemInfo(
            drawerOption = Screen.Onboarding,
            title = R.string.Onboarding,
            icon = Icons.AutoMirrored.Outlined.HelpOutline,
            descriptionId = R.string.drawer_Onboarding_description
        ),
        AppDrawerItemInfo(
            drawerOption = Screen.Settings,
            title = R.string.Settings,
            icon = Icons.Outlined.Settings,
            descriptionId = R.string.drawer_Settings_description
        ),
    )
}

/**
 * T for generic type to be used for the picking
 */
@Composable
fun <T : Enum<T>> AppDrawerContent(
    drawerState: DrawerState,
    menuItems: List<AppDrawerItemInfo<T>>,
    defaultPick: T,
    onClick: (T) -> Unit
) {
    // default home destination to avoid duplication
    var currentPick by remember { mutableStateOf(defaultPick) }
    val coroutineScope = rememberCoroutineScope()

    ModalDrawerSheet(windowInsets = WindowInsets.ime) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Main app icon"
                )
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // generates on demand the required composables
                    items(menuItems) { item ->
                        // custom UI representation of the button
                        AppDrawerItem(item = item, enabled = currentPick == item.drawerOption) { navOption ->

                            // if it is the same - ignore the click
                            if (currentPick == navOption) {
                                return@AppDrawerItem
                            }

                            currentPick = navOption

                            // close the drawer after clicking the option
                            coroutineScope.launch {
                                drawerState.close()
                            }

                            // navigate to the required screen
                            onClick(navOption)
                        }
                    }
                    item {
                        BottomWindowInsetsSpacer()
                    }
                }
            }
        }
    }
}

@Composable
fun <T> AppDrawerItem(item: AppDrawerItemInfo<T>, enabled: Boolean, onClick: (options: T) -> Unit) {
    val backgroundColorValue = if (enabled) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    val backgroundColor by animateColorAsState(targetValue = backgroundColorValue, label = "backgroundColor")

    val contentColorValue =
        if (enabled) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
    val contentColor by animateColorAsState(targetValue = contentColorValue, label = "backgroundColor")

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.width(280.dp),
        onClick = { onClick(item.drawerOption) },
        shape = CircleShape,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(id = item.descriptionId),
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = item.title),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}