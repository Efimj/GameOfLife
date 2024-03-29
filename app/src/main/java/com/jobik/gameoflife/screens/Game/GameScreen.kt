package com.jobik.gameoflife.screens.Game

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.screens.Game.actions.GameActions
import com.jobik.gameoflife.ui.helpers.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameScreen(
    viewModel: GameScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val scope = rememberCoroutineScope()
    // Get local density from composable
    val backdropScaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)

    val containerColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.secondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.error
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    val containerColor by animateColorAsState(targetValue = containerColorTarget, label = "containerColor")

    LaunchedEffect(backdropScaffoldState.progress) {
        if (backdropScaffoldState.progress.fraction > 0f)
            viewModel.turnOffSimulation()
    }

    BackHandler(enabled = backdropScaffoldState.currentValue == BackdropValue.Concealed) {
        scope.launch { backdropScaffoldState.reveal() }
    }

    BackHandler(enabled = viewModel.states.value.isSimulationRunning) {
        viewModel.turnOffSimulation()
    }

    when (currentWidthSizeClass()) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
            CompactGameScreen(
                backdropScaffoldState,
                containerColor,
                viewModel
            )
        }

        WindowWidthSizeClass.Expanded -> {
            ExpandedGameScreen(viewModel, containerColor)
        }
    }
}

@Composable
private fun ExpandedGameScreen(
    viewModel: GameScreenViewModel,
    containerColor: Color
) {
    val localDensity = LocalDensity.current
    var wideScreenHeight by remember { mutableStateOf(0.dp) }

    Row(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                // Set screen height using the LayoutCoordinates
                wideScreenHeight = with(localDensity) { coordinates.size.height.toDp() }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(.4f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            GameActions(viewModel = viewModel)
        }
        Column(
            modifier = Modifier
                .weight(.6f)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .background(containerColor),
            verticalArrangement = Arrangement.Center
        ) {
            GameContent(viewModel = viewModel)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun CompactGameScreen(
    backdropScaffoldState: BackdropScaffoldState,
    containerColor: Color,
    viewModel: GameScreenViewModel
) {
    val localDensity = LocalDensity.current

    val frontCornerValue = if (backdropScaffoldState.currentValue == BackdropValue.Concealed) 0.dp else 12.dp
    val frontCorner by animateDpAsState(targetValue = frontCornerValue, label = "frontCorner")

    var screenHeight by remember { mutableStateOf(0.dp) }
    val gameContentPercentageOfHeight = 0.65
    val maxGameHeight by remember(screenHeight) { mutableStateOf((screenHeight.value * gameContentPercentageOfHeight).dp) }

    val isTopInsets = isWidth(sizeClass = WindowWidthSizeClass.Compact)

    val topInsets = if (isTopInsets) {
        Modifier.topWindowInsetsPadding()
    } else {
        Modifier
    }

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
            GameAppBar()
        },
        peekHeight = if (isTopInsets) topWindowInsetsPadding() + 64.dp else 64.dp,
        persistentAppBar = false,
        frontLayerScrimColor = Color.Unspecified,
        frontLayerBackgroundColor = MaterialTheme.colorScheme.background,
        backLayerBackgroundColor = MaterialTheme.colorScheme.surface,
        backLayerContent = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
                    .background(containerColor)
                    .then(topInsets)
                    .heightIn(max = maxGameHeight)
            ) {
                GameContent(viewModel = viewModel)
            }
        },
        frontLayerContent = {
            GameActions(viewModel = viewModel)
        })
}
