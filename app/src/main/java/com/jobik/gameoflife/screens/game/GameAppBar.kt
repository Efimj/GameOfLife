package com.jobik.gameoflife.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jobik.gameoflife.R
import com.jobik.gameoflife.screens.layout.ModalDrawer
import com.jobik.gameoflife.screens.layout.ModalDrawerImplementation
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.isWidth
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameAppBar(modalDrawer: ModalDrawer = ModalDrawerImplementation) {
    val coroutineScope = rememberCoroutineScope()

    val topInsets = if (isWidth(sizeClass = WindowWidthSizeClass.Compact)) {
        Modifier.topWindowInsetsPadding()
    } else {
        Modifier
    }

    TopAppBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .then(topInsets),
        title = {
            Text(text = stringResource(id = R.string.GameOfLife))
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
                    modalDrawer.drawerState.open()
                }
            }) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = stringResource(id = R.string.menu_button)
                )
            }
        },
    )
}