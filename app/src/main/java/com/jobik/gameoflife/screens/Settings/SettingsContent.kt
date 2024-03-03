package com.jobik.gameoflife.screens.Settings

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jobik.gameoflife.GameOfLifeApplication
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.services.localization.LocaleData
import com.jobik.gameoflife.services.localization.Localization
import com.jobik.gameoflife.services.localization.LocalizationHelper
import com.jobik.gameoflife.ui.composables.CustomModalBottomSheet
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.theme.AppThemeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsContent(navController: NavHostController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp, top = 10.dp),
    ) {
        SettingsItem(
            icon = if (AppThemeUtil.isDarkMode.value) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
            text = stringResource(id = R.string.change_theme),
            action = {}) {
            AppThemeUtil.update(context = context, isDarkTheme = AppThemeUtil.isDarkMode.value.not())
        }

        val isLanguageSelectorOpen = remember { mutableStateOf(false) }
        LocalizationSelector(isLanguageSelectorOpen)
        SettingsItem(
            icon = Icons.Outlined.Language,
            text = stringResource(id = R.string.language),
            action = {
                Text(
                    text = GameOfLifeApplication.currentLanguage.getLocalizedValue(context).name,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right
                )
            }) {
            isLanguageSelectorOpen.value = true
        }
        SettingsItem(
            icon = Icons.Outlined.Lightbulb,
            text = stringResource(id = R.string.Onboarding)
        ) {
            navController.navigate(Screen.Onboarding.name)
        }
        BottomWindowInsetsSpacer()
    }
}

fun getLocalizationList(localContext: Context): List<LocaleData> {
    return Localization.entries.filter { it.name != GameOfLifeApplication.currentLanguage.name }
        .map { it.getLocalizedValue(localContext) }
}

fun selectLocalization(selectedIndex: Int, context: Context) {
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
private fun LocalizationSelector(
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

@Composable
private fun SettingsItem(
    icon: ImageVector?,
    text: String,
    action: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (action != null) {
            Spacer(modifier = Modifier.width(20.dp))
            Row(
                modifier = Modifier
            ) {
                action()
            }
        }
    }
}