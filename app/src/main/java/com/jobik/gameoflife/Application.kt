package com.jobik.gameoflife

import android.app.Application
import android.content.Context
import com.jobik.gameoflife.services.localization.Localization
import com.jobik.gameoflife.services.localization.LocalizationHelper

class GameOfLifeApplication : Application() {
    override fun attachBaseContext(base: Context) {
        val currentLocalization =
            LocalizationHelper.getSavedLocalization(base) ?: LocalizationHelper.getDeviceLocalization()
        super.attachBaseContext(LocalizationHelper.setLocale(base, currentLocalization ?: Localization.EN))
    }

    companion object {
        private var _currentLanguage = Localization.EN
        var currentLanguage: Localization
            get() {
                return _currentLanguage
            }
            set(value) {
                _currentLanguage = value
            }
    }
}