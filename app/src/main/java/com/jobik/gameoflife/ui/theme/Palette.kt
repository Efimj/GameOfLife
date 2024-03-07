package com.example.compose

import androidx.annotation.Keep
import androidx.compose.material3.ColorScheme
import com.jobik.gameoflife.ui.theme.*

@Keep
enum class Palette(val light: ColorScheme, val dark: ColorScheme) {
    DynamicPalette(light = LightDefaultPalette, dark = DarkDefaultPalette),
    DefaultPalette(light = LightDefaultPalette, dark = DarkDefaultPalette),
    BluePalette(light = LightBluePalette, dark = DarkBluePalette),
    Blue2Palette(light = LightBlue2Palette, dark = DarkBlue2Palette),
    GreenPalette(light = LightGreenPalette, dark = DarkGreenPalette),
    Green2Palette(light = LightGreen2Palette, dark = DarkGreen2Palette),
    YellowPalette(light = LightYellowPalette, dark = DarkYellowPalette),
    Yellow2Palette(light = LightYellow2Palette, dark = DarkYellow2Palette),
    PeachPalette(light = LightPeachPalette, dark = DarkPeachPalette),
    Peach2Palette(light = LightPeach2Palette, dark = DarkPeach2Palette),
    PurplePalette(light = LightPurplePalette, dark = DarkPurplePalette),
    Purple2Palette(light = LightPurple2Palette, dark = DarkPurple2Palette),
    NauticalPalette(light = LightNauticalPalette, dark = DarkNauticalPalette),
    Nautical2Palette(light = LightNautical2Palette, dark = DarkNautical2Palette),
    PinkPalette(light = LightPinkPalette, dark = DarkPinkPalette),
    Pink2Palette(light = LightPink2Palette, dark = DarkPink2Palette);

    fun getPalette(isDarkTheme: Boolean): ColorScheme {
        return if (isDarkTheme) dark else light
    }
}