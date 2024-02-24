package com.jobik.gameoflife.screens.GameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameAppBar(
    drawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer),
        title = {
            Text(text = "Game of Life")
        },
        windowInsets = WindowInsets.ime,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "MenuButton"
                )
            }
        },
    )
}