package com.jobik.gameoflife.screens.game

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.jobik.gameoflife.R
import com.jobik.gameoflife.screens.layout.ModalDrawer
import com.jobik.gameoflife.screens.layout.ModalDrawerImplementation
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.isWidth
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameAppBar(
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    title: String = "",
    modalDrawer: ModalDrawer = ModalDrawerImplementation
) {
    val coroutineScope = rememberCoroutineScope()

    val topInsets = if (isWidth(sizeClass = WindowWidthSizeClass.Compact)) {
        Modifier.topWindowInsetsPadding()
    } else {
        Modifier
    }

    TopAppBar(
        modifier = Modifier
            .background(backgroundColor)
            .then(topInsets),
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