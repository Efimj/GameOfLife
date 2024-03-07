package com.example.compose

import androidx.annotation.Keep
import androidx.compose.material3.ColorScheme
import com.jobik.gameoflife.ui.theme.*

@Keep
enum class Palette(val light: ColorScheme, val dark: ColorScheme) {
    DynamicPalette(light = LightDefaultPalette, dark = DarkDefaultPalette),
    DefaultPalette(light = LightDefaultPalette, dark = DarkDefaultPalette),
    OrangePalette(light = LightOrangePalette, dark = DarkOrangePalette),
    AvocadoPalette(light = LightAvocadoPalette, dark = DarkAvocadoPalette),
    LemonPalette(light = LightLemonPalette, dark = DarkLemonPalette),
    SeaPalette(light = LightSeaPalette, dark = DarkSeaPalette),
    GreenPalette(light = LightGreenPalette, dark = DarkGreenPalette);

    fun getPalette(isDarkTheme: Boolean): ColorScheme {
        return if (isDarkTheme) dark else light
    }
}