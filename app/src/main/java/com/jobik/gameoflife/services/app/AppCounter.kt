package com.jobik.gameoflife.services.app

import android.content.Context
import com.jobik.gameoflife.SharedPreferencesKeys

class AppCounter(val context: Context) {
    fun getOnCreateNumber(): Int {
        val sharedPreferences = context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(SharedPreferencesKeys.OnCreateCounter, 0)
    }

    fun updateOnCreateCounter() {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        val nextValue = getOnCreateNumber() + 1
        sharedPreferences.edit().putInt(SharedPreferencesKeys.OnCreateCounter, nextValue).apply()
    }
}