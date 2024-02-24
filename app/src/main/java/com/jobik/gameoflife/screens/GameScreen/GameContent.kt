package com.jobik.gameoflife.screens.GameScreen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.ui.composables.Counter
import com.jobik.gameoflife.ui.composables.GridForGame
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding

@Composable
fun GameContent(viewModel: GameScreenViewModel) {
    val containerColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.secondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.errorContainer
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val containerColor by animateColorAsState(targetValue = containerColorTarget, label = "containerColor")

    val contentColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.onSecondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.onErrorContainer
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }
    val contentColor by animateColorAsState(targetValue = contentColorTarget, label = "contentColor")

    Column(
        modifier = Modifier
            .topWindowInsetsPadding()
            .padding(top = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(containerColor)
    ) {
        AnimatedVisibility(
            visible = viewModel.states.value.gameResult != null,
            enter = slideInVertically() + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when (viewModel.states.value.gameResult) {
                        GameOfLife.Companion.GameOfLifeResult.StableCombination -> "Stable combination \uD83C\uDF89"
                        GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> "No one survived \uD83D\uDC80"
                        else -> ""
                    },
                    color = contentColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(4.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            GridForGame(
                array = viewModel.states.value.currentStep,
                emojiMode = viewModel.states.value.emojiEnabled,
                onElementClick = viewModel::onElementClick
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Alive", color = contentColor, overflow = TextOverflow.Ellipsis)
                Counter(viewModel.states.value.alive, style = MaterialTheme.typography.bodyMedium, color = contentColor)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Deaths", color = contentColor, overflow = TextOverflow.Ellipsis)
                Counter(viewModel.states.value.deaths, style = MaterialTheme.typography.bodyMedium, color = contentColor)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Revivals", color = contentColor, overflow = TextOverflow.Ellipsis)
                Counter(viewModel.states.value.revivals, style = MaterialTheme.typography.bodyMedium, color = contentColor)
            }
        }
    }
}