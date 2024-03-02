package com.jobik.gameoflife.screens.Settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.theme.AppThemeUtil

@Composable
fun SettingsContent(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp, top = 10.dp),
    ) {
        val context = LocalContext.current
        SettingsItem(
            icon = if (AppThemeUtil.isDarkMode.value) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
            text = stringResource(id = R.string.change_theme),
            action = {}) {
            AppThemeUtil.update(context = context, isDarkTheme = AppThemeUtil.isDarkMode.value.not())
        }
        SettingsItem(icon = Icons.Outlined.Language, text = stringResource(id = R.string.language), action = {
            Text(text = "English", color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Right)
        }) {

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