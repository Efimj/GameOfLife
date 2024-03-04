package com.jobik.gameoflife.screens.Information

import androidx.compose.foundation.background
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.SnapConfig
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun InformationScreen(drawerState: DrawerState) {
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        state = collapsingToolbarScaffold,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        enabledWhenBodyUnfilled = false,
        snapConfig = SnapConfig(), // "collapseThreshold = 0.5" by default
        toolbar = {
            InformationAppBar(drawerState)
        },
    ) {
        InformationContent()
    }
}