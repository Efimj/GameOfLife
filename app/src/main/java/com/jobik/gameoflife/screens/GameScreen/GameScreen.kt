package com.jobik.gameoflife.screens.GameScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameScreen(
    drawerState: DrawerState,
    viewModel: GameScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

    LaunchedEffect(backdropScaffoldState.currentValue) {
        if (backdropScaffoldState.currentValue == BackdropValue.Concealed)
            viewModel.turnOffSimulation()
    }

    val frontCornerValue = if (backdropScaffoldState.currentValue == BackdropValue.Concealed) 0.dp else 12.dp
    val frontCorner by animateDpAsState(targetValue = frontCornerValue, label = "frontCorner")

    BackdropScaffold(
        scaffoldState = backdropScaffoldState,
        frontLayerShape = RoundedCornerShape(
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
            topStart = frontCorner,
            topEnd = frontCorner
        ),
        appBar = {
            GameAppBar(drawerState)
        },
        peekHeight = topWindowInsetsPadding() + 64.dp,
        persistentAppBar = false,
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = MaterialTheme.colorScheme.background,
        backLayerBackgroundColor = MaterialTheme.colorScheme.surface,
        backLayerContent = {
            GameContent(viewModel = viewModel)
        },
        frontLayerContent = {
            ActionsContent(viewModel = viewModel)
        }
    )
}