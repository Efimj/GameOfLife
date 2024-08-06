package com.jobik.gameoflife.screens.game.actions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.screens.game.GameRuleSet
import com.jobik.gameoflife.screens.game.GameRules
import com.jobik.gameoflife.screens.game.GameScreenViewModel
import com.jobik.gameoflife.ui.composables.AliveEmojis
import com.jobik.gameoflife.ui.composables.DeadEmojis
import com.jobik.gameoflife.ui.composables.modifier.fadingEdges
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.isWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun GameActions(viewModel: GameScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column {
            MainActions(viewModel)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
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
                    Text(
                        text = stringResource(id = R.string.clear),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
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
                    Text(
                        text = AliveEmojis.random(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
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
                            Text(
                                text = DeadEmojis.random(),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )
                        }
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
            Spacer(modifier = Modifier.height(5.dp))
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
            Spacer(modifier = Modifier.height(5.dp))

            val ruleSetDialogVisible = remember { mutableStateOf(false) }

            LaunchedEffect(ruleSetDialogVisible.value) {
                if (ruleSetDialogVisible.value) {
                    viewModel.turnOffSimulation()
                }
            }

            SettingsItemWrapper(onClick = {
                ruleSetDialogVisible.value = ruleSetDialogVisible.value.not()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                SettingsItemContent(
                    title = stringResource(id = R.string.rules_set),
                    description = stringResource(id = R.string.rules_set_description),
                    paddings = PaddingValues(start = 20.dp, top = 4.dp, bottom = 4.dp)
                )

                val selectedRules = remember { mutableStateOf<GameRules?>(null) }
                val context = LocalContext.current

                LaunchedEffect(
                    viewModel.states.value.gameOfLifeStepRules.neighborsForAlive,
                    viewModel.states.value.gameOfLifeStepRules.neighborsForReviving
                ) {
                    val currentRules = viewModel.states.value.gameOfLifeStepRules
                    GameRuleSet.forEach { rules ->
                        if (currentRules.neighborsForAlive == rules.rules.neighborsForAlive && currentRules.neighborsForReviving == rules.rules.neighborsForReviving) {
                            selectedRules.value = rules
                            return@LaunchedEffect
                        }
                    }
                    selectedRules.value = null
                }

                AnimatedContent(targetState = selectedRules.value, label = "") { rules ->
                    if (rules != null) {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.padding(start = 20.dp)
                        ) {
                            Text(
                                text = stringResource(id = rules.title),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = rules.type.getLocalizedValue(context),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(start = 20.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.custom_rules),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                SelectGameRuleSet(
                    isOpen = ruleSetDialogVisible,
                    selectedRules = selectedRules.value,
                    onClick = { rules -> viewModel.setRules(rules) })
            }
        }

        SettingsGroup(headline = stringResource(id = R.string.rules)) {
            val scrollRevivingButtons = rememberScrollState()

            SettingsItemWrapper(
                modifier = Modifier
                    .fadingEdges(scrollRevivingButtons)
                    .horizontalScroll(scrollRevivingButtons),
                headline = stringResource(id = R.string.neighbors_for_reviving),
                horizontalArrangement = Arrangement.Center
            ) {

                val buttonValues = (0..8).toList()

                MultiChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = buttonValues.size
                            ),
                            onCheckedChange = {
                                val newRule =
                                    if (it) viewModel.states.value.gameOfLifeStepRules.neighborsForReviving.plus(
                                        value
                                    )
                                    else viewModel.states.value.gameOfLifeStepRules.neighborsForReviving.minus(
                                        value
                                    )
                                viewModel.updateGameRules(neighborsForReviving = newRule)
                            },
                            checked = value in viewModel.states.value.gameOfLifeStepRules.neighborsForReviving,
                            colors = SegmentedButtonDefaults.colors(inactiveContainerColor = Color.Transparent)
                        ) {
                            Text(value.toString())
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            val scrollSurvivalButtons = rememberScrollState()

            SettingsItemWrapper(
                modifier = Modifier
                    .fadingEdges(scrollSurvivalButtons)
                    .horizontalScroll(scrollSurvivalButtons),
                headline = stringResource(id = R.string.neighbors_for_surviving),
                horizontalArrangement = Arrangement.Center
            ) {

                val buttonValues = (0..8).toList()

                MultiChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = buttonValues.size
                            ),
                            onCheckedChange = {
                                val newRule =
                                    if (it) viewModel.states.value.gameOfLifeStepRules.neighborsForAlive.plus(
                                        value
                                    )
                                    else viewModel.states.value.gameOfLifeStepRules.neighborsForAlive.minus(
                                        value
                                    )
                                viewModel.updateGameRules(neighborsForAlive = newRule)
                            },
                            checked = value in viewModel.states.value.gameOfLifeStepRules.neighborsForAlive,
                            colors = SegmentedButtonDefaults.colors(inactiveContainerColor = Color.Transparent)
                        ) {
                            Text(value.toString())
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.padding(
                    start = 20.dp
                )
            ) {
                val enabled = remember(viewModel.states.value.gameOfLifeStepRules) {
                    mutableStateOf(checkIsRulesNotDefault(viewModel))
                }
                AnimatedVisibility(
                    visible = enabled.value,
                    enter = slideInVertically() + expandVertically() + fadeIn(),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    Button(
                        onClick = { viewModel.gameRulesToDefault() },
                        enabled = enabled.value
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_default),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        SettingsGroup(headline = stringResource(id = R.string.simulation_settings)) {
            GameFieldScale(viewModel)
            Spacer(modifier = Modifier.height(5.dp))

            val scrollTimeButtons = rememberScrollState()

            SettingsItemWrapper(
                modifier = Modifier
                    .fadingEdges(scrollTimeButtons)
                    .horizontalScroll(scrollTimeButtons),
                headline = stringResource(id = R.string.one_step_duration),
                horizontalArrangement = Arrangement.Center
            ) {
                val buttonValues = listOf(
                    100L,
                    250L,
                    500L,
                    750L,
                    1000L,
                )

                val buttonTitles = listOf(
                    stringResource(id = R.string.speed_value_100ms),
                    stringResource(id = R.string.speed_value_250ms),
                    stringResource(id = R.string.speed_value_500ms),
                    stringResource(id = R.string.speed_value_750ms),
                    stringResource(id = R.string.speed_value_1s),
                )

                SingleChoiceSegmentedButtonRow {
                    buttonValues.forEachIndexed { index, value ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = buttonValues.size
                            ),
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
        }

        if (isWidth(sizeClass = WindowWidthSizeClass.Expanded).not()) {
            BottomWindowInsetsSpacer()
        }
    }
}

@Composable
private fun GameFieldScale(viewModel: GameScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = stringResource(id = R.string.set_field_scale),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = String.format(Locale.getDefault(), "%.2f", viewModel.states.value.scale),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(5.dp))

        val scope = rememberCoroutineScope()

        var isVisibleRow by remember { mutableStateOf(false) }

        AnimatedVisibility(
            visible = isVisibleRow,
            enter = fadeIn() + slideInVertically { it } + expandVertically(),
            exit = fadeOut() + slideOutVertically { it } + shrinkVertically()) {
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (v in 0 until viewModel.states.value.cols) {
                    Box(
                        modifier = Modifier
                            .padding(1.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .fillMaxHeight()
                            .weight(1f)
                    )
                }
            }
        }

        Slider(
            value = viewModel.states.value.scale,
            onValueChange = {
                isVisibleRow = true
                viewModel.updateScale(it)
            },
            valueRange = .5f..1.5f,
            steps = 15,
            onValueChangeFinished = {
                scope.launch {
                    delay(1000)
                    isVisibleRow = false
                }
            }
        )
    }
}

private fun checkIsRulesNotDefault(viewModel: GameScreenViewModel) =
    GameOfLife.GameOfLifeStepSettingsDefault.neighborsForReviving != viewModel.states.value.gameOfLifeStepRules.neighborsForReviving || GameOfLife.GameOfLifeStepSettingsDefault.neighborsForAlive != viewModel.states.value.gameOfLifeStepRules.neighborsForAlive

@Composable
private fun RowScope.SettingsItemContent(
    title: String,
    description: String,
    paddings: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
) {
    Column(
        modifier = Modifier.Companion
            .weight(1f)
            .padding(paddings),
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
}

@Composable
private fun SettingsGroup(headline: String, content: @Composable (ColumnScope.() -> Unit)) {
    Column(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 15.dp, bottom = 5.dp),
            text = headline,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Column {
            content()
        }
        Spacer(modifier = Modifier.height(10.dp))
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