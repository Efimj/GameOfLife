package com.jobik.gameoflife.screens.settings

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.compose.Palette
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.NavigationHelper.Companion.canNavigate
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.ui.composables.modifier.fadingEdges
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.util.settings.NightMode
import com.jobik.gameoflife.util.settings.SettingsManager
import com.jobik.gameoflife.util.settings.SettingsManager.settings

@Composable
fun SettingsContent(navController: NavHostController) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        item {
            SettingsContainer {
                GroupHeader(stringResource(id = R.string.application))
                SettingsItem(
                    icon =
                    when (settings.nightMode) {
                        NightMode.Light -> Icons.Outlined.DarkMode
                        NightMode.Dark -> Icons.Outlined.LightMode
                        else -> Icons.Outlined.AutoAwesome
                    },
                    title = stringResource(id = R.string.change_theme)
                ) {
                    SettingsManager.update(
                        context = context,
                        settings = settings.copy(
                            nightMode = when (settings.nightMode) {
                                NightMode.Light -> NightMode.Dark
                                NightMode.Dark -> NightMode.System
                                else -> NightMode.Light
                            }
                        )
                    )
                }
                ChangePalette()

                val isLanguageSelectorOpen = remember { mutableStateOf(false) }
                LocalizationSelector(isLanguageSelectorOpen)
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    title = stringResource(id = R.string.language),
                    action = {
                        Column(
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            modifier = Modifier.padding(vertical = 6.dp)
                        ) {
                            val currentLocale = settings.localization
                            Text(
                                text = currentLocale.getLocalizedValue(context).name,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Right,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = currentLocale.getLocalizedValue(context).language,
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
            }
        }

        item {
            SettingsContainer {
                GroupHeader(stringResource(id = R.string.other))
                SettingsItem(
                    icon = Icons.Outlined.Lightbulb,
                    title = stringResource(id = R.string.Onboarding)
                ) {
                    if (navController.canNavigate().not()) return@SettingsItem
                    navController.navigate(Screen.Onboarding)
                }
            }
        }

        item {
            SettingsContainer {
                GroupHeader(stringResource(R.string.security))
                SettingsItem(
                    icon = Icons.Outlined.Security,
                    title = stringResource(R.string.secure_mode),
                    description = stringResource(R.string.hides_content_prohibits_screen_recording),
                    action = {
                        Switch(checked = settings.secureMode, onCheckedChange = {
                            SettingsManager.update(
                                context = context,
                                settings = settings.copy(secureMode = settings.secureMode.not())
                            )
                        })
                    }
                ) {
                    SettingsManager.update(
                        context = context,
                        settings = settings.copy(secureMode = settings.secureMode.not())
                    )
                }
            }
        }

        item {
            BottomWindowInsetsSpacer()
        }
    }
}

@Composable
private fun ChangePalette() {
    val context = LocalContext.current
    val canRemoveDynamicPalette = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S).not()
    val palettes =
        if (canRemoveDynamicPalette) Palette.entries.filter { it.name != Palette.DynamicPalette.name } else Palette.entries
    val isDarkMode = settings.nightMode == NightMode.Dark

    val scroll = rememberLazyListState()

    LazyRow(
        state = scroll,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fadingEdges(scroll),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(palettes) { palette ->
            val paletteValues =
                if (palette.name == Palette.DynamicPalette.name && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) if (isDarkMode) dynamicDarkColorScheme(
                    context
                ) else dynamicLightColorScheme(
                    context
                ) else palette.getPalette(isDarkMode)
            val isSelected = palette.name == settings.theme.name

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        SettingsManager.update(
                            context = context,
                            settings = settings.copy(
                                theme = palette
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(Modifier.fillMaxSize()) {
                    Column(
                        Modifier
                            .weight(1f)
                            .background(paletteValues.primary)
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {}
                    }
                    Row(Modifier.weight(1f)) {
                        Box(
                            Modifier
                                .weight(1f)
                                .background(paletteValues.tertiaryContainer)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {}
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .background(paletteValues.secondary)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {}
                        }
                    }
                }
                AnimatedVisibility(visible = isSelected, enter = fadeIn(), exit = fadeOut()) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape)
                            .background(paletteValues.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            tint = paletteValues.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsContainer(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        content()
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
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}