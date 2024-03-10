package com.jobik.gameoflife.screens.Game

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.ui.composables.*
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.isWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsContent(viewModel: GameScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MainActions(viewModel)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
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

        SettingsGroup(headline = stringResource(id = R.string.game_settings)) {
            SettingsItemWrapper(onClick = viewModel::switchEmojiMode) {
                Icon(
                    imageVector = Icons.Outlined.Mood,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,

                    )
                SettingsItemContent(
                    title = stringResource(id = R.string.emoji_mode),
                    description = stringResource(id = R.string.emoji_mode_description)
                )
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
            AnimatedVisibility(
                visible = viewModel.states.value.emojiEnabled,
                enter = slideInVertically() + expandVertically() + fadeIn(),
                exit = slideOutVertically() + shrinkVertically() + fadeOut(),
            ) {
                SettingsItemWrapper(onClick = viewModel::switchFreeSoulMode) {
                    Icon(
                        imageVector = Icons.Outlined.Cached,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    SettingsItemContent(
                        title = stringResource(id = R.string.free_soul_mode),
                        description = stringResource(id = R.string.free_soul_mode_description)
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

        SettingsGroup(headline = stringResource(id = R.string.simulation_settings)) {
            SettingsItemWrapper(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                headline = stringResource(id = R.string.one_step_duration),
                horizontalArrangement = Arrangement.Center
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
                            selected = value == viewModel.states.value.oneStepDurationMills,
                            colors = SegmentedButtonDefaults.colors(inactiveContainerColor = Color.Transparent)
                        ) {
                            Text(buttonTitles[index])
                        }
                    }
                }
            }
            ChangeGameFieldDimension(viewModel)
        }

        SettingsGroup(headline = stringResource(id = R.string.rules)) {
            SettingsItemWrapper(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                headline = stringResource(id = R.string.neighbors_for_reviving),
                horizontalArrangement = Arrangement.Center
            ) {

                val buttonValues = (1..10).toList()
                val checkedList = remember { mutableStateListOf<Int>() }

                MultiChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = buttonValues.size),
                            onCheckedChange = {
                                if (index in checkedList) {
                                    checkedList.remove(index)
                                } else {
                                    checkedList.add(index)
                                }
                            },
                            checked = index in checkedList,
                            colors = SegmentedButtonDefaults.colors(inactiveContainerColor = Color.Transparent)
                        ) {
                            Text(value.toString())
                        }
                    }
                }
            }
            SettingsItemWrapper(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                headline = stringResource(id = R.string.maximum_neighbors_for_surviving),
                horizontalArrangement = Arrangement.Center
            ) {

                val buttonValues = (1..10).toList()
                val checkedList = remember { mutableStateListOf<Int>() }

                MultiChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = buttonValues.size),
                            onCheckedChange = {
                                if (index in checkedList) {
                                    checkedList.remove(index)
                                } else {
                                    checkedList.add(index)
                                }
                            },
                            checked = index in checkedList,
                            colors = SegmentedButtonDefaults.colors(inactiveContainerColor = Color.Transparent)
                        ) {
                            Text(value.toString())
                        }
                    }
                }
            }
            SettingsItemWrapper(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
                headline = stringResource(id = R.string.minimum_neighbors_for_surviving),
                horizontalArrangement = Arrangement.Center
            ) {

                val buttonValues = (1..10).toList()
                val checkedList = remember { mutableStateListOf<Int>() }

                MultiChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = buttonValues.size),
                            onCheckedChange = {
                                if (index in checkedList) {
                                    checkedList.remove(index)
                                } else {
                                    checkedList.add(index)
                                }
                            },
                            checked = index in checkedList,
                            colors = SegmentedButtonDefaults.colors(inactiveContainerColor = Color.Transparent)
                        ) {
                            Text(value.toString())
                        }
                    }
                }
            }
        }

        if (isWidth(sizeClass = WindowWidthSizeClass.Expanded).not()) {
            BottomWindowInsetsSpacer()
        }
    }
}

@Composable
private fun RowScope.SettingsItemContent(title: String, description: String) {
    Spacer(modifier = Modifier.width(20.dp))
    Column(
        modifier = Modifier.Companion
            .weight(1f)
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
        )
    }
    Spacer(modifier = Modifier.width(20.dp))
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
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
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
private fun SettingsGroup(headline: String, content: @Composable() (ColumnScope.() -> Unit)) {
    Column {
        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 5.dp),
            text = headline,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            content()
        }
    }
}

@Composable
private fun SettingsItemWrapper(
    modifier: Modifier = Modifier,
    headline: String? = null,
    paddings: PaddingValues = PaddingValues(horizontal = 20.dp),
    onClick: (() -> Unit)? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    val onClickModifier = if (onClick == null) {
        Modifier
    } else {
        Modifier.clickable(onClick = onClick)
    }
    Column {
        if (headline != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddings),
                text = headline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .then(onClickModifier)
                .padding(paddings)
                .heightIn(min = 60.dp),
            verticalAlignment = verticalAlignment,
            horizontalArrangement = horizontalArrangement
        )
        {
            content()
        }
    }
}