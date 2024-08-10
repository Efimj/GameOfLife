package com.jobik.gameoflife.screens.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.jobik.gameoflife.R
import com.jobik.gameoflife.screens.layout.ModalDrawer
import com.jobik.gameoflife.screens.layout.ModalDrawerImplementation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameAppBar(
    modifier: Modifier,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    title: String = "",
    modalDrawer: ModalDrawer = ModalDrawerImplementation,
    isPinned: Boolean = false,
    onPin: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        modifier = modifier,
        title = {
            AnimatedContent(targetState = title) {
                Text(text = it)
            }
        },
        windowInsets = WindowInsets.ime,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor,
            navigationIconContentColor = color,
            titleContentColor = color,
            actionIconContentColor = color
        ),
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch {
                    modalDrawer.drawerState.open()
                }
            }) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = stringResource(id = R.string.menu_button)
                )
            }
        },
        actions = {
            AnimatedContent(targetState = isPinned) {
                if (it) {
                    IconButton(onClick = {
                        onPin()
                    }) {
                        Icon(
                            Icons.Rounded.Lock,
                            contentDescription = stringResource(id = R.string.menu_button)
                        )
                    }
                } else {
                    IconButton(onClick = {
                        onPin()
                    }) {
                        Icon(
                            Icons.Rounded.LockOpen,
                            contentDescription = stringResource(id = R.string.menu_button)
                        )
                    }
                }
            }
        }
    )
}