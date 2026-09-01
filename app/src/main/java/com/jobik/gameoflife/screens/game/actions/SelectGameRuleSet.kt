package com.jobik.gameoflife.screens.game.actions

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.screens.game.GameRuleSet
import com.jobik.gameoflife.screens.game.GameRules
import com.jobik.gameoflife.screens.game.SavedGameRules
import com.jobik.gameoflife.screens.game.ruleSetId
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.bottomWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import com.jobik.gameoflife.util.settings.SettingsManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGameRuleSet(
    isOpen: MutableState<Boolean>,
    selectedRuleSetId: String?,
    onDefaultClick: (rules: GameRules) -> Unit,
    onSavedClick: (rules: SavedGameRules) -> Unit,
    onDelete: (rules: SavedGameRules) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var pendingDeletion by remember { mutableStateOf<SavedGameRules?>(null) }
    val savedRules = SettingsManager.state.value.savedGameRules

    LaunchedEffect(isOpen.value) {
        if (isOpen.value) {
            showBottomSheet = true
        } else if (showBottomSheet) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                showBottomSheet = false
            }
        }
    }

    fun close(onClosed: (() -> Unit)? = null) {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showBottomSheet = false
            isOpen.value = false
            onClosed?.invoke()
        }
    }

    if (showBottomSheet) {
        CustomModalBottomSheet(
            state = sheetState,
            dragHandle = null,
            windowInsets = WindowInsets.ime,
            onCancel = { close() },
        ) {
            Spacer(modifier = Modifier.height(topWindowInsetsPadding()))
            Header()

            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .horizontalWindowInsetsPadding()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
            ) {
                if (savedRules.isNotEmpty()) {
                    SectionLabel(stringResource(R.string.saved_rules))
                    savedRules.forEach { rules ->
                        RulesItem(
                            isSelected = selectedRuleSetId == rules.id,
                            title = rules.name,
                            subtitle = null,
                            onDelete = { pendingDeletion = rules },
                            onClick = {
                                close {
                                    if (selectedRuleSetId != rules.id) onSavedClick(rules)
                                }
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionLabel(stringResource(R.string.standard_rules))
                }

                val context = LocalContext.current
                GameRuleSet.forEach { rules ->
                    RulesItem(
                        isSelected = selectedRuleSetId == rules.ruleSetId(),
                        title = stringResource(rules.title),
                        subtitle = rules.type.getLocalizedValue(context),
                        onClick = {
                            close {
                                if (selectedRuleSetId != rules.ruleSetId()) onDefaultClick(rules)
                            }
                        },
                    )
                }

                Spacer(modifier = Modifier.height(bottomWindowInsetsPadding()))
            }
        }
    }

    pendingDeletion?.let { rules ->
        AlertDialog(
            onDismissRequest = { pendingDeletion = null },
            title = { Text(stringResource(R.string.delete_saved_rules_title)) },
            text = {
                Text(stringResource(R.string.delete_saved_rules_message, rules.name))
            },
            dismissButton = {
                TextButton(onClick = { pendingDeletion = null }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingDeletion = null
                        onDelete(rules)
                    },
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
        )
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(R.string.rules_set),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Right,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun SectionLabel(title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 16.dp, bottom = 8.dp),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
    )
}

@Composable
private fun RulesItem(
    isSelected: Boolean,
    title: String,
    subtitle: String?,
    onDelete: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    val backgroundColorValue =
        if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    val backgroundColor by animateColorAsState(
        targetValue = backgroundColorValue,
        label = "backgroundColor",
    )
    val contentColorValue =
        if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
    val contentColor by animateColorAsState(
        targetValue = contentColorValue,
        label = "contentColor",
    )

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = CircleShape,
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 72.dp)
                .padding(start = 20.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = contentColor.copy(alpha = 0.76f),
                )
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Spacer(modifier = Modifier.padding(end = 12.dp))
            }
        }
    }
}
