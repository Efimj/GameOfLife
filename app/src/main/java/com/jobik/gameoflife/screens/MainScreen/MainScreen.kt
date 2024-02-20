package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.composables.AliveEmojis
import com.jobik.gameoflife.ui.composables.CustomFabButton
import com.jobik.gameoflife.ui.composables.CustomTab
import com.jobik.gameoflife.ui.composables.GridForGame

@Composable
fun MainScreen(viewModel: MainScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        GridForGame(
            array = viewModel.states.value.currentStep,
            emojiMode = viewModel.states.value.emojiEnabled,
            onElementClick = viewModel::onElementClick
        )
        Spacer(modifier = Modifier.height(10.dp))
        Content(viewModel = viewModel)
    }
}

@Composable
private fun Content(viewModel: MainScreenViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Alive", color = MaterialTheme.colorScheme.onSurface)
            LinearProgressIndicator(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .weight(1f),
                progress = viewModel.states.value.aliveCount / 100f
            )
            Text(text = "${viewModel.states.value.aliveCount}", color = MaterialTheme.colorScheme.onSurface)
        }

        Row(
            modifier = Modifier.padding(vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                FilledIconButton(onClick = { viewModel.dropGame() }) {
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
                Text(
                    text = viewModel.states.value.stepNumber.toString(),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Card {
            Column(modifier = Modifier.padding(vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomTab(
                        tabWidth = 80.dp,
                        items = listOf("100ms", "200ms", "500ms", "1s"),
                        selectedItemIndex =
                        when (viewModel.states.value.oneStepDurationMills) {
                            100L -> 0
                            200L -> 1
                            500L -> 2
                            1000L -> 3
                            else -> 0
                        },
                        onClick = {
                            viewModel.changeStepDuration(
                                duration = when (it) {
                                    0 -> 100L
                                    1 -> 200L
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
            }
        }
    }
}