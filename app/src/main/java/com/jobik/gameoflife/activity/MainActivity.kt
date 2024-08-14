package com.jobik.gameoflife.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.jobik.gameoflife.screens.layout.AppLayout
import com.jobik.gameoflife.services.app.AppCounter
import com.jobik.gameoflife.services.rate.RateDialogProvider
import com.jobik.gameoflife.ui.helpers.SecureModeManager
import com.jobik.gameoflife.ui.theme.GameOfLifeTheme
import com.jobik.gameoflife.util.ContextUtils
import com.jobik.gameoflife.util.InAppUpdateManager.Companion.checkAppUpdate
import com.jobik.gameoflife.util.settings.NightMode
import com.jobik.gameoflife.util.settings.SettingsManager
import com.jobik.gameoflife.util.settings.SettingsManager.settings

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        SettingsManager.init(newBase)
        super.attachBaseContext(
            ContextUtils.setLocale(
                context = newBase,
                language = settings.localization
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        installSplashScreen()

        if (settings.checkUpdates) {
            checkAppUpdate(
                context = this,
                scope = lifecycleScope,
                showLatestVersionInstalled = false
            )
        }

        setContent {
            SecureModeManager()

            GameOfLifeTheme(
                darkTheme = when (settings.nightMode) {
                    NightMode.Light -> false
                    NightMode.Dark -> true
                    else -> isSystemInDarkTheme()
                }, palette = settings.theme
            ) {
                // Main application
                AppLayout()

                // Rate dialog
                RateDialogProvider()
            }
        }

        AppCounter(this).updateOnCreateCounter()
    }
}
