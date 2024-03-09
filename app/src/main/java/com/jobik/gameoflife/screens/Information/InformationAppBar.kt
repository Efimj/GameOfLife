package com.jobik.gameoflife.screens.Information

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
import androidx.compose.ui.text.style.TextOverflow
import com.jobik.gameoflife.R
import com.jobik.gameoflife.screens.AppLayout.ModalDrawer
import com.jobik.gameoflife.screens.AppLayout.ModalDrawerImplementation
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InformationAppBar(modalDrawer: ModalDrawer = ModalDrawerImplementation) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .horizontalWindowInsetsPadding()
            .topWindowInsetsPadding(),
        title = {
            Text(
                text = stringResource(id = R.string.information),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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