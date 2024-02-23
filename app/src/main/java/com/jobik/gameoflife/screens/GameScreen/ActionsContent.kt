package com.jobik.gameoflife.screens.GameScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.composables.*


@Composable
fun ActionsContent(viewModel: GameScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MainActions(viewModel)
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(modifier = Modifier.weight(1f), onClick = viewModel::setFullEmpty) {
                    Text(text = "Clear", overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
                FilledIconButton(modifier = Modifier, onClick = viewModel::setFullAlive) {
                    Text(text = AliveEmojis.random(), overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
                FilledIconButton(modifier = Modifier, onClick = viewModel::setFullDeath) {
                    Text(text = DeadEmojis.random(), overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTab(
                    tabWidth = 80.dp,
                    items = listOf("100ms", "250ms", "500ms", "1s"),
                    selectedItemIndex =
                    when (viewModel.states.value.oneStepDurationMills) {
                        100L -> 0
                        250L -> 1
                        500L -> 2
                        1000L -> 3
                        else -> 0
                    },
                    onClick = {
                        viewModel.changeStepDuration(
                            duration = when (it) {
                                0 -> 100L
                                1 -> 250L
                                2 -> 500L
                                3 -> 1000L
                                else -> 100L
                            }
                        )
                    },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Free soul mode", color = MaterialTheme.colorScheme.onSurface)
                Switch(
                    enabled = viewModel.states.value.emojiEnabled,
                    checked = viewModel.states.value.freeSoulMode,
                    onCheckedChange = { viewModel.switchFreeSoulMode() },
                    thumbContent = if (!viewModel.states.value.freeSoulMode) {
                        {
                            Text(text = "\uD83D\uDC80")
                        }
                    } else {
                        null
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Emoji mode", color = MaterialTheme.colorScheme.onSurface)
                Switch(
                    checked = viewModel.states.value.emojiEnabled,
                    onCheckedChange = { viewModel.switchEmojiMode() },
                    thumbContent = if (viewModel.states.value.emojiEnabled) {
                        {
                            Text(text = AliveEmojis.random())
                        }
                    } else {
                        null
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
        }
    }
}

@Composable
private fun MainActions(viewModel: GameScreenViewModel) {
    Column(modifier = Modifier.padding(vertical = 20.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                IconButton(
                    colors = IconButtonDefaults.filledIconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    onClick = { viewModel.dropGame() }) {
                    Icon(Icons.Filled.RestartAlt, contentDescription = "Favorite")
                }
            }
            CustomFabButton(viewModel.states.value.isSimulationRunning) {
                if (viewModel.states.value.isSimulationRunning)
                    viewModel.turnOffSimulation()
                else
                    viewModel.turnOnSimulation()
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                Counter(
                    viewModel.states.value.stepNumber.toInt(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}