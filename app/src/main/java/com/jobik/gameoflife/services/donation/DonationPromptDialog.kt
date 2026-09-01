package com.jobik.gameoflife.services.donation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.bottomWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationPromptDialog(
    isOpen: MutableState<Boolean>,
    onCancel: (dontAskAgain: Boolean) -> Unit,
    onDonate: (dontAskAgain: Boolean) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(isOpen.value, showBottomSheet.value) {
        if (isOpen.value) showBottomSheet.value = true
        if (!isOpen.value) {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) showBottomSheet.value = false
            }
        }
    }

    val topInsetsPadding = topWindowInsetsPadding()
    val bottomInsetsPadding = bottomWindowInsetsPadding()

    if (showBottomSheet.value) {
        CustomModalBottomSheet(
            state = sheetState,
            dragHandle = null,
            windowInsets = WindowInsets.ime,
            onCancel = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) isOpen.value = false
                }
            },
        ) {
            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .horizontalWindowInsetsPadding()
                    .padding(horizontal = 40.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                Spacer(modifier = Modifier.height(topInsetsPadding))
                Icon(
                    modifier = Modifier
                        .height(90.dp)
                        .fillMaxWidth(),
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.donation_prompt_title),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.donation_prompt_description),
                    color = MaterialTheme.colorScheme.onSurface,
                )

                val dontAskAgain = rememberSaveable { mutableStateOf(false) }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        modifier = Modifier.offset(x = (-12).dp),
                        checked = dontAskAgain.value,
                        onCheckedChange = { dontAskAgain.value = it },
                    )
                    Text(
                        text = stringResource(R.string.dont_ask_again),
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onCancel(dontAskAgain.value) },
                    ) {
                        Text(
                            text = stringResource(R.string.later),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onDonate(dontAskAgain.value) },
                    ) {
                        Text(
                            text = stringResource(R.string.donate),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(bottomInsetsPadding))
            }
        }
    }
}
