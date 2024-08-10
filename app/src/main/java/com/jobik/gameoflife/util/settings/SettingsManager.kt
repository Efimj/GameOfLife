package com.jobik.gameoflife.util.settings

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.jobik.gameoflife.SharedPreferencesKeys.AppSettings
import com.jobik.gameoflife.SharedPreferencesKeys.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object SettingsManager {
    private var _settings: MutableState<SettingsState> = mutableStateOf(SettingsState())
    var state: MutableState<SettingsState>
        get() = _settings
        private set(value) {
            _settings = value
        }

    val settings: SettingsState
        get() = _settings.value


    fun init(context: Context) {
        state.value = restore(context = context)
    }

    fun update(context: Context, settings: SettingsState) {
        updateState(settings)
        saveToSharedPreferences(settings = settings, context = context)
    }

    fun update(context: Context, settings: (SettingsState) -> Unit): Boolean {
        val updatedSettings = _settings.value.apply {
            settings(_settings.value)
        }
        updateState(updatedSettings)
        saveToSharedPreferences(settings = updatedSettings, context = context)
        return true
    }

    private fun updateState(settings: SettingsState) {
        _settings.value = settings
    }

    private fun saveToSharedPreferences(
        settings: SettingsState,
        context: Context
    ) {
        val storedString = Json.encodeToString(settings)
        val store = context.getSharedPreferences(AppSettings, Context.MODE_PRIVATE)
        store.edit().putString(Settings, storedString).apply()
    }

    private fun restore(context: Context): SettingsState {
        val store = context.getSharedPreferences(AppSettings, Context.MODE_PRIVATE)
        val savedSettings = store.getString(Settings, "")
        return if (savedSettings.isNullOrEmpty()) {
            SettingsState()
        } else {
            try {
                Json.decodeFromString<SettingsState>(savedSettings)
            } catch (e: Exception) {
                SettingsState()
            }
        }
    }
}