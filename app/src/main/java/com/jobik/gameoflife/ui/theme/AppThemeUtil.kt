package com.jobik.gameoflife.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.compose.Palette
import com.jobik.gameoflife.SharedPreferencesKeys

object AppThemeUtil {
    private var _isDarkMode: MutableState<Boolean> = mutableStateOf(false)

    var isDarkMode: MutableState<Boolean>
        get() = _isDarkMode
        private set(value) {
            _isDarkMode = value
        }

    private var _palette: MutableState<Palette> = mutableStateOf(Palette.DynamicPalette)

    var palette: MutableState<Palette>
        get() = _palette
        private set(value) {
            _palette = value
        }

    fun restore(context: Context, defaultValue: Boolean = false, defaultPalette: Palette = Palette.DynamicPalette) {
        val store = context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        isDarkMode.value = store.getBoolean(SharedPreferencesKeys.IsDarkTheme, defaultValue)

        val restoredPaletteName =
            store.getString(SharedPreferencesKeys.SelectedPalette, defaultPalette.name) ?: defaultPalette.name

        palette.value = Palette.valueOf(restoredPaletteName)
    }

    fun update(context: Context, isDarkTheme: Boolean) {
        isDarkMode.value = isDarkTheme
        val sharedPreferences = context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(SharedPreferencesKeys.IsDarkTheme, isDarkTheme)
        editor.apply()
    }

    fun update(context: Context, newPalette: Palette) {
        val sharedPreferences = context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(SharedPreferencesKeys.SelectedPalette, newPalette.name)
        editor.apply()
    }
}