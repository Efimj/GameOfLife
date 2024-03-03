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
        GroupHeader(stringResource(id = R.string.application))
        SettingsItem(
            icon = if (AppThemeUtil.isDarkMode.value) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
            text = stringResource(id = R.string.change_theme)
        ) {
            AppThemeUtil.update(context = context, isDarkTheme = AppThemeUtil.isDarkMode.value.not())
        }

        val isLanguageSelectorOpen = remember { mutableStateOf(false) }
        LocalizationSelector(isLanguageSelectorOpen)
        SettingsItem(
            icon = Icons.Outlined.Language,
            text = stringResource(id = R.string.language),
            action = {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    val currentLocale = GameOfLifeApplication.currentLanguage.getLocalizedValue(context)
                    Text(
                        text = currentLocale.name,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = currentLocale.language,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Right,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f)
                    )
                }
            }) {
            isLanguageSelectorOpen.value = true
        }
        GroupHeader(stringResource(id = R.string.other))
        SettingsItem(
            icon = Icons.Outlined.Lightbulb,
            text = stringResource(id = R.string.Onboarding)
        ) {
            navController.navigate(Screen.Onboarding.name)
        }
        BottomWindowInsetsSpacer()
    }
}

@Composable
fun GroupHeader(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
    )
    {
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}