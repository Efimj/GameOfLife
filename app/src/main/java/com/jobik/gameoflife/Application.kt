package com.jobik.gameoflife

import android.app.Application
import android.content.Context
import com.jobik.gameoflife.util.ContextUtils
import com.jobik.gameoflife.util.settings.SettingsManager

class GameOfLifeApplication : Application() {
    override fun attachBaseContext(base: Context) {
        SettingsManager.init(base)
        super.attachBaseContext(
            ContextUtils.setLocale(
                context = base,
                language = SettingsManager.settings.localization
            )
        )
    }
}