package com.jobik.gameoflife

import androidx.annotation.Keep

@Keep
object SharedPreferencesKeys {
    // File name
    const val AppSettings = "AppSettings"

    // Attribute key
    const val IsDarkTheme = "IsDarkTheme"
    const val SelectedPalette = "SelectedPalette"
    const val Localization = "Localization"
    const val OnboardingFinishedData = "1"
    const val CurrentOnboardingFinishedData = "OnboardingPageFinishedData"
    const val OnCreateCounter = "OnCreateCounter"
    const val CanAskRate = "CanAskRate"

}