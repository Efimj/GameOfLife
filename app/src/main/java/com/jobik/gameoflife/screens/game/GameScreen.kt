package com.jobik.gameoflife.screens.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.screens.game.actions.GameActions
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding

@Composable
fun GameScreen(
    viewModel: GameScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val containerColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.secondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.error
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerHigh
    }
    val containerColor by animateColorAsState(
        targetValue = containerColorTarget,
        label = "containerColor"
    )

    BackHandler(enabled = viewModel.states.value.isSimulationRunning) {
        viewModel.turnOffSimulation()
    }

    when (currentWidthSizeClass()) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
            CompactGameScreen(
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
private fun CompactGameScreen(
    containerColor: Color,
    viewModel: GameScreenViewModel
) {
    Column(
        modifier = Modifier.horizontalWindowInsetsPadding()
    ) {
        GameAppBar()
        GameContent(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
                .background(containerColor),
            viewModel = viewModel
        )
        GameActions(viewModel = viewModel)
    }
}
