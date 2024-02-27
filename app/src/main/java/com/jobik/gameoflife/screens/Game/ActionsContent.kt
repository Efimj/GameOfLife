package com.jobik.gameoflife.screens.Game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.ui.composables.*
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer

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
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = .2f))
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
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = viewModel::setFullEmpty,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(text = stringResource(id = R.string.clear), overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
                FilledIconButton(
                    modifier = Modifier,
                    onClick = viewModel::setFullAlive,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(text = AliveEmojis.random(), overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
                FilledIconButton(
                    modifier = Modifier,
                    onClick = viewModel::setFullDeath,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Text(text = DeadEmojis.random(), overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val tabs = listOf(
                    stringResource(id = R.string.speed_value_100ms),
                    stringResource(id = R.string.speed_value_250ms),
                    stringResource(id = R.string.speed_value_500ms),
                    stringResource(id = R.string.speed_value_1s),
                )

                CustomTab(
                    tabWidth = 80.dp,
                    items = tabs,
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
                Text(text = stringResource(id = R.string.free_soul_mode), color = MaterialTheme.colorScheme.onSurface)
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
                Text(text = stringResource(id = R.string.emoji_mode), color = MaterialTheme.colorScheme.onSurface)
                Switch(
                    checked = viewModel.states.value.emojiEnabled,
                    onCheckedChange = { viewModel.switchEmojiMode() },
                    thumbContent = if (viewModel.states.value.emojiEnabled) {
                        {
                            Text(text = AliveEmojis.random())
                        }
                    } else {
                        null
                    },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                var isRelated by rememberSaveable { mutableStateOf(true) }
                val rows = remember { mutableStateOf(viewModel.states.value.rows.toString()) }
                val cols = remember { mutableStateOf(viewModel.states.value.cols.toString()) }

                LaunchedEffect(rows.value, cols.value, isRelated) {
                    if (rows.value.isBlank() || cols.value.isBlank()) return@LaunchedEffect
                    viewModel.setRows(rows.value)
                    if (isRelated) {
                        cols.value = rows.value
                    }
                    viewModel.setColumns(cols.value)
                }

                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(
                        text = stringResource(id = R.string.set_field_dimensions),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = rows.value,
                            onValueChange = { rows.value = it },
                            label = {
                                Text(text = stringResource(id = R.string.rows))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = cols.value,
                            onValueChange = { cols.value = it },
                            label = {
                                Text(text = stringResource(id = R.string.columns))
                            },
                            enabled = !isRelated,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Switch(
                            checked = isRelated,
                            onCheckedChange = { isRelated = !isRelated },
                            thumbContent = if (isRelated) {
                                {
                                    Icon(
                                        imageVector = Icons.Outlined.Link,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null
                            }
                        )
                    }
                }
            }
        }
        BottomWindowInsetsSpacer()
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
                    Icon(Icons.Filled.RestartAlt, contentDescription = stringResource(id = R.string.restart))
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