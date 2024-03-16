package com.jobik.gameoflife.services.rate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
fun RateDialog(
    isOpen: MutableState<Boolean>,
    onCancel: (dontAskAgain: Boolean) -> Unit,
    onRate: (dontAskAgain: Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val showBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(isOpen.value, showBottomSheet.value) {
        if (isOpen.value) {
            showBottomSheet.value = true
        }
        if (isOpen.value.not()) {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    showBottomSheet.value = false
                }
            }
        }
    }

    val topInsetsPaddings = topWindowInsetsPadding()
    val bottomInsetsPaddings = bottomWindowInsetsPadding()

    if (showBottomSheet.value) {
        CustomModalBottomSheet(
            state = sheetState,
            dragHandle = null,
            windowInsets = WindowInsets.ime,
            onCancel = {
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    if (sheetState.isVisible.not()) {
                        isOpen.value = false
                    }
                }
            }
        ) {

            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .horizontalWindowInsetsPadding()
                    .padding(horizontal = 40.dp)
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Spacer(modifier = Modifier.height(topInsetsPaddings))

                Image(
                    modifier = Modifier
                        .height(90.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.three_stars),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.rate_dialog_title),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.rate_dialog_description),
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                )
                val dontShowAgain = rememberSaveable { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        modifier = Modifier.offset(x = (-12).dp),
                        checked = dontShowAgain.value,
                        onCheckedChange = { isChecked -> dontShowAgain.value = isChecked }
                    )
                    Text(
                        text = stringResource(id = R.string.dont_ask_again),
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onCancel(dontShowAgain.value) }) {
                        Text(
                            text = stringResource(id = R.string.later),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onRate(dontShowAgain.value) }) {
                        Text(
                            text = stringResource(id = R.string.rate),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(bottomInsetsPaddings))
            }
        }
    }
}