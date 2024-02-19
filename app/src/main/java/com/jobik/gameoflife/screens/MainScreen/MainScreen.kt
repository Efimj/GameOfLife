package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.composables.AliveEmojis
import com.jobik.gameoflife.ui.composables.CustomFabButton
import com.jobik.gameoflife.ui.composables.GridForGame

@Composable
fun MainScreen(viewModel: MainScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        GridForGame(
            array = viewModel.states.value.currentStep,
            emojiMode = viewModel.states.value.emojiEnabled,
            content = {
                Content(viewModel)
            },
            onElementClick = viewModel::onElementClick
        )
    }
}

@Composable
private fun Content(viewModel: MainScreenViewModel) {
    Column {
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
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
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Step duration", color = MaterialTheme.colorScheme.onSurface)
            Slider(
                modifier = Modifier
                    .weight(1f),
                value = viewModel.states.value.oneStepDurationMills.toFloat(),
                onValueChange = { newPosition ->
                    viewModel.changeStepDuration(duration = newPosition.toLong())
                },
                valueRange = 100f..1000f,
                steps = 5,
            )
            Text(text = "${viewModel.states.value.oneStepDurationMills}ms", color = MaterialTheme.colorScheme.onSurface)
        }
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomFabButton(viewModel.states.value.isSimulationRunning) {
                if (viewModel.states.value.isSimulationRunning)
                    viewModel.turnOffSimulation()
                else
                    viewModel.turnOnSimulation()
            }
        }
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.dropGame() }) {
                Text(text = "Drop")
            }
        }
        Row(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
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
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
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