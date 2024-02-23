package com.jobik.gameoflife.screens.GameScreen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun GameScreen(
    drawerState: DrawerState,
    viewModel: GameScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

    LaunchedEffect(backdropScaffoldState.currentValue) {
        if (backdropScaffoldState.currentValue == BackdropValue.Concealed)
            viewModel.turnOffSimulation()
    }

    BackdropScaffold(
        modifier = Modifier,
        scaffoldState = backdropScaffoldState,
        frontLayerShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        appBar = {
            TopAppBar(
                title = {},
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
        },
        persistentAppBar = false,
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = MaterialTheme.colorScheme.surfaceContainer,
        backLayerBackgroundColor = MaterialTheme.colorScheme.surface,
        backLayerContent = {
            GameContent(viewModel = viewModel)
        },
        frontLayerContent = {
            ActionsContent(viewModel = viewModel)
        }
    )
}