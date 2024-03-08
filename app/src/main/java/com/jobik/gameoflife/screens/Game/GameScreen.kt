package com.jobik.gameoflife.screens.Game

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameScreen(
    drawerState: DrawerState,
    viewModel: GameScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Get local density from composable
    val localDensity = LocalDensity.current
    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

    LaunchedEffect(backdropScaffoldState.currentValue) {
        if (backdropScaffoldState.currentValue == BackdropValue.Concealed)
            viewModel.turnOffSimulation()
    }

    val frontCornerValue = if (backdropScaffoldState.currentValue == BackdropValue.Concealed) 0.dp else 12.dp
    val frontCorner by animateDpAsState(targetValue = frontCornerValue, label = "frontCorner")

    var screenHeight by remember { mutableStateOf(0.dp) }
    val gameContentPercentageOfHeight = 0.65
    val maxGameHeight by remember(screenHeight) { mutableStateOf((screenHeight.value * gameContentPercentageOfHeight).dp) }

    BackdropScaffold(
        modifier = Modifier
            .horizontalWindowInsetsPadding()
            .onGloballyPositioned { coordinates ->
                // Set screen height using the LayoutCoordinates
                screenHeight = with(localDensity) { coordinates.size.height.toDp() }
            },
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
            Box(modifier = Modifier.heightIn(max = maxGameHeight)) {
                GameContent(viewModel = viewModel)
            }
        },
        frontLayerContent = {
            ActionsContent(viewModel = viewModel)
        }
    )
}