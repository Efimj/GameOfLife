package com.jobik.gameoflife.screens.game.actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.screens.game.toRuleNotation
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.bottomWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

private const val MaxSavedRulesNameLength = 50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveGameRulesBottomSheet(
    isOpen: MutableState<Boolean>,
    rules: GameOfLifeStepSettings,
    onSave: (name: String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(isOpen.value) {
        if (isOpen.value) {
            name = ""
            showBottomSheet = true
        } else if (showBottomSheet) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                showBottomSheet = false
            }
        }
    }

    fun close() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            showBottomSheet = false
            isOpen.value = false
        }
    }

    if (showBottomSheet) {
        CustomModalBottomSheet(
            state = sheetState,
            dragHandle = null,
            windowInsets = WindowInsets.ime,
            onCancel = ::close,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .horizontalWindowInsetsPadding()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Spacer(modifier = Modifier.height(topWindowInsetsPadding()))
                Text(
                    text = stringResource(R.string.save_rules_title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { value ->
                        name = value.take(MaxSavedRulesNameLength)
                    },
                    label = { Text(stringResource(R.string.saved_rules_name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                )
                RulesPreview(rules)

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotBlank(),
                    onClick = {
                        onSave(name.trim())
                        close()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(modifier = Modifier.height(bottomWindowInsetsPadding()))
            }
        }
    }
}

@Composable
private fun RulesPreview(rules: GameOfLifeStepSettings) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = rules.toRuleNotation(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            RuleValue(
                title = stringResource(R.string.neighbors_for_reviving),
                values = rules.neighborsForReviving,
            )
            RuleValue(
                title = stringResource(R.string.neighbors_for_surviving),
                values = rules.neighborsForAlive,
            )
        }
    }
}

@Composable
private fun RuleValue(title: String, values: Set<Int>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = values.sorted().joinToString().ifEmpty { "—" },
            fontWeight = FontWeight.SemiBold,
        )
    }
}
