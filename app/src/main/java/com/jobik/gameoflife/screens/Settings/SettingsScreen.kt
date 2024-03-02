package com.jobik.gameoflife.screens.Settings

import androidx.compose.foundation.background
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.SnapConfig
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun SettingsScreen(drawerState: DrawerState, navController: NavHostController) {
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .horizontalWindowInsetsPadding(),
        state = collapsingToolbarScaffold,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        enabledWhenBodyUnfilled = false,
        snapConfig = SnapConfig(), // "collapseThreshold = 0.5" by default
        toolbar = {
            SettingsAppBar(drawerState)
        },
    ) {
        SettingsContent(navController)
    }
}

