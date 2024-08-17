package com.jobik.gameoflife.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.isWidth
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.SnapConfig
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun SettingsScreen(navController: NavHostController) {
    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        state = collapsingToolbarScaffold,
        scrollStrategy = ScrollStrategy.EnterAlways,
        enabledWhenBodyUnfilled = false,
        snapConfig = SnapConfig(), // "collapseThreshold = 0.5" by default
        toolbar = {
            if (isWidth(sizeClass = WindowWidthSizeClass.Compact))
                SettingsAppBar()
        },
    ) {
        SettingsContent(navController)
    }
}

