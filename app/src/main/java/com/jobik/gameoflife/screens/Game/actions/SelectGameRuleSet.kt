package com.jobik.gameoflife.screens.Game.actions

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.jobik.gameoflife.services.localization.LocaleData
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.TopWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGameRuleSet(isOpen: MutableState<Boolean>) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
            Header(scroll)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .horizontalWindowInsetsPadding()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
//                localizations.value.forEachIndexed { index, locale ->
//                    RulesItem(
//                        isSelected = currentLocale.name == locale.name,
//                        locale = locale
//                    ) {
//                        scope
//                            .launch {
//                                sheetState.hide()
//                            }
//                            .invokeOnCompletion {
//                                isOpen.value = false
//                                if (currentLocale.name == locale.name) return@invokeOnCompletion
//
//                                selectLocalization(index, context)
//                                (context as? Activity)?.recreate()
//                            }
//                    }
//                }
                BottomWindowInsetsSpacer()
            }
        }
    }
}

@Composable
private fun Header(scroll: ScrollState) {
    TopWindowInsetsSpacer()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.rules_set),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Right,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun RulesItem(
    isSelected: Boolean = false,
    locale: LocaleData,
    onClick: () -> Unit,
) {
    val backgroundColorValue = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
    val backgroundColor by animateColorAsState(targetValue = backgroundColorValue, label = "backgroundColor")

    val contentColorValue =
        if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
    val contentColor by animateColorAsState(targetValue = contentColorValue, label = "backgroundColor")

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = CircleShape,
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
        ) {
            Text(
                text = locale.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Right,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = locale.language,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Right,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
            )
        }
    }
}