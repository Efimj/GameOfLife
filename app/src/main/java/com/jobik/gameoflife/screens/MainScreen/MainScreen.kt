package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
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
                scrollBehavior = scrollBehavior,
                title = {
                    Text("Top app bar")
                }
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