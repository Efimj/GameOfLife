package com.jobik.gameoflife.screens.game

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.ui.composables.Counter
import com.jobik.gameoflife.ui.composables.GridForGame

@Composable
fun GameContent(modifier: Modifier = Modifier, viewModel: GameScreenViewModel) {
    val contentColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.onSecondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.onError
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }
    val contentColor by animateColorAsState(
        targetValue = contentColorTarget,
        label = "contentColor"
    )

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f, fill = false)
                .fillMaxWidth(),
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
                    viewModel = viewModel,
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