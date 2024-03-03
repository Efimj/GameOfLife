package com.jobik.gameoflife.screens.Settings

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.GameOfLifeApplication
import com.jobik.gameoflife.services.localization.LocaleData
import com.jobik.gameoflife.services.localization.Localization
import com.jobik.gameoflife.services.localization.LocalizationHelper
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import kotlinx.coroutines.launch

private fun getLocalizationList(localContext: Context): List<LocaleData> {
    return Localization.entries.filter { it.name != GameOfLifeApplication.currentLanguage.name }
        .map { it.getLocalizedValue(localContext) }
}

private fun selectLocalization(selectedIndex: Int, context: Context) {
    try {
        val newLocalization: Localization =
            Localization.entries.filter { it.name != GameOfLifeApplication.currentLanguage.name }[selectedIndex]
        LocalizationHelper.setLocale(context.applicationContext, newLocalization)
    } catch (e: Exception) {
        Log.d("ChangeLocalizationError", e.message.toString())
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LocalizationSelector(
    isOpen: MutableState<Boolean>
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val localizations = remember {
        mutableStateOf(getLocalizationList(context))
    }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .horizontalWindowInsetsPadding()
            ) {
                localizations.value.forEachIndexed { index, locale ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable {
                            scope
                                .launch {
                                    sheetState.hide()
                                }
                                .invokeOnCompletion {
                                    isOpen.value = false
                                    selectLocalization(index, context)
                                    (context as? Activity)?.recreate()
                                }
                        }) {
                        Text(
                            text = locale.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Right
                        )
                    }
                }
                BottomWindowInsetsSpacer()
            }
        }
    }
}