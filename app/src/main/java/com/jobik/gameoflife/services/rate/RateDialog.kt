package com.jobik.gameoflife.services.rate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.bottomWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateDialog(isOpen: MutableState<Boolean>) {
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
            Spacer(modifier = Modifier.height(topInsetsPaddings))

            val scroll = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .horizontalWindowInsetsPadding()
                    .padding(horizontal = 40.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Rate this app",
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Your voice matters. Take a moment to rate Battery Guru and help us shape its future. Your feedback determines what features we create and how we improve them.",
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextButton(
                        modifier = Modifier.weight(1f),
                        onClick = { /*TODO*/ }) {
                        Text(
                            text = "Later",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { /*TODO*/ }) {
                        Text(
                            text = "Rate!",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(bottomInsetsPaddings))
        }
    }
}