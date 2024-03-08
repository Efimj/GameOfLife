package com.jobik.gameoflife.screens.Game

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.ui.composables.Counter
import com.jobik.gameoflife.ui.composables.GridForGame
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding

@Composable
fun GameContent(viewModel: GameScreenViewModel) {
    val containerColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.secondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.error
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    val containerColor by animateColorAsState(targetValue = containerColorTarget, label = "containerColor")

    val contentColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.onSecondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.onError
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }
    val contentColor by animateColorAsState(targetValue = contentColorTarget, label = "contentColor")

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomEnd = 12.dp, bottomStart = 12.dp))
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
                    .padding(top = 10.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when (viewModel.states.value.gameResult) {
                        GameOfLife.Companion.GameOfLifeResult.StableCombination -> stringResource(id = R.string.stable_combination)
                        GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> stringResource(id = R.string.no_one_survived)
                        else -> ""
                    },
                    color = contentColor,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
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
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.alive),
                    color = contentColor,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
                Counter(
                    viewModel.states.value.alive,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.deaths),
                    color = contentColor,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
                Counter(
                    viewModel.states.value.deaths,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.revivals),
                    color = contentColor,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
                Counter(
                    viewModel.states.value.revivals,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }
        }
    }
}