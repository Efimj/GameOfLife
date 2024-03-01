package com.jobik.gameoflife.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.jobik.gameoflife.SharedPreferencesKeys

object AppThemeUtil {
    private var _isDarkMode: MutableState<Boolean> = mutableStateOf(false)
    var isDarkMode: MutableState<Boolean>
        get() = _isDarkMode
        private set(value) {
            _isDarkMode = value
        }

    fun restore(context: Context) {
        val store = context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        val isDarkTheme = store.getBoolean(SharedPreferencesKeys.IsDarkTheme, false)
        isDarkMode.value = isDarkTheme
    }

    fun update(context: Context, isDarkTheme: Boolean) {
        isDarkMode.value = isDarkTheme
        val sharedPreferences = context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(SharedPreferencesKeys.IsDarkTheme, isDarkTheme)
        editor.apply()
    }
}