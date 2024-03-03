package com.jobik.gameoflife.services.localization

import android.content.Context
import com.jobik.gameoflife.GameOfLifeApplication
import com.jobik.gameoflife.SharedPreferencesKeys
import java.util.*

object LocalizationHelper {
    fun setLocale(context: Context, localization: Localization): Context? {
        GameOfLifeApplication.currentLanguage = localization
        saveLocalization(context, localization)
        return updateResources(context, localization)
    }

    private fun saveLocalization(context: Context, localization: Localization) {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(SharedPreferencesKeys.Localization, localization.name).apply()
    }

    fun getSavedLocalization(context: Context): Localization? {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        val notSelected = "NotSelected"
        val language = sharedPreferences.getString(SharedPreferencesKeys.Localization, notSelected)
        if (language == notSelected || language == null) {
            return null
        }
        return Localization.entries.find { it.name == language }
    }

    fun getDeviceLocalization(): Localization? {
        return Localization.entries.find { (Locale.getDefault().language.equals(Locale(it.name).language)) }
    }

    private fun updateResources(context: Context, language: Localization): Context? {
        val locale = Locale(language.localeKey)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }
}