package com.jobik.gameoflife.util.settings

import androidx.annotation.Keep
import com.example.compose.Palette
import com.jobik.gameoflife.gameOfLife.GameSettings
import kotlinx.serialization.Serializable
import java.util.Locale

@Keep
enum class NightMode {
    Light,
    Dark,
    System,
}

@Keep
@Serializable
data class SettingsState(
    val fontScale: Float = 1f,
    val checkUpdates: Boolean = true,
    val secureMode: Boolean = false,
    val nightMode: NightMode = NightMode.System,
    val theme: Palette = Palette.DynamicPalette,
    val localization: Localization = defaultLocalization(),
    val gameSettings: GameSettings = GameSettings(),
)

private fun defaultLocalization() = Localization.entries.find {
    (Locale.getDefault().language.equals(
        Locale(it.name).language
    ))
} ?: Localization.EN