package com.jobik.gameoflife.screens.Game

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsContent(viewModel: GameScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MainActions(viewModel)
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(MaterialTheme.shapes.medium)
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
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
                Spacer(modifier = Modifier.width(10.dp))
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
                AnimatedVisibility(
                    visible = viewModel.states.value.emojiEnabled,
                    enter = slideInHorizontally { it / 2 } + expandHorizontally(
                        expandFrom = Alignment.Start,
                        clip = false
                    ) + fadeIn(),
                    exit = slideOutHorizontally { -it / 2 } + shrinkHorizontally(
                        shrinkTowards = Alignment.Start,
                        clip = false
                    ) + fadeOut(),
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(10.dp))
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
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {

                val buttonValues = listOf(
                    100L,
                    250L,
                    500L,
                    1000L,
                )
                val buttonTitles = listOf(
                    stringResource(id = R.string.speed_value_100ms),
                    stringResource(id = R.string.speed_value_250ms),
                    stringResource(id = R.string.speed_value_500ms),
                    stringResource(id = R.string.speed_value_1s),
                )

                SingleChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = buttonValues.size),
                            onClick = {
                                viewModel.changeStepDuration(value)
                            },
                            selected = value == viewModel.states.value.oneStepDurationMills
                        ) {
                            Text(buttonTitles[index])
                        }
                    }
                }
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
            ) {
                AnimatedVisibility(
                    visible = viewModel.states.value.emojiEnabled,
                    enter = slideInVertically() + expandVertically() + fadeIn(),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.free_soul_mode),
                            color = MaterialTheme.colorScheme.onSurface
                        )
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
                }
            }
            ChangeGameFieldDimension(viewModel)
        }
        BottomWindowInsetsSpacer()
    }
}

@Composable
private fun ChangeGameFieldDimension(viewModel: GameScreenViewModel) {
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
                style = MaterialTheme.typography.titleMedium,
            )
            BoxWithConstraints {
                if (maxWidth > 250.dp) {
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
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = rows.value,
                            onValueChange = { rows.value = it },
                            label = {
                                Text(text = stringResource(id = R.string.rows))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
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