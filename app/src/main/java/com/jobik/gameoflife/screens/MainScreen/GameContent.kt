package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.ui.composables.Counter
import com.jobik.gameoflife.ui.composables.GridForGame

@Composable
fun GameContent(viewModel: MainScreenViewModel) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.secondaryContainer)
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
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(2.dp)
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
                Text(text = "Alive", color = MaterialTheme.colorScheme.onSurface, overflow = TextOverflow.Ellipsis)
                Counter(viewModel.states.value.alive, style = MaterialTheme.typography.bodyMedium)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Deaths", color = MaterialTheme.colorScheme.onSurface, overflow = TextOverflow.Ellipsis)
                Counter(viewModel.states.value.deaths, style = MaterialTheme.typography.bodyMedium)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Revivals", color = MaterialTheme.colorScheme.onSurface, overflow = TextOverflow.Ellipsis)
                Counter(viewModel.states.value.revivals, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}