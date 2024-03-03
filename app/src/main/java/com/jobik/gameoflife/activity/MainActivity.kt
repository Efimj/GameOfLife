package com.jobik.gameoflife.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jobik.gameoflife.GameOfLifeApplication
import com.jobik.gameoflife.screens.AppLayout.AppLayout
import com.jobik.gameoflife.services.localization.LocalizationHelper
import com.jobik.gameoflife.ui.theme.AppThemeUtil
import com.jobik.gameoflife.ui.theme.GameOfLifeTheme

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            LocalizationHelper.setLocale(newBase, GameOfLifeApplication.currentLanguage)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        installSplashScreen()

        setContent {
            AppThemeUtil.restore(context = this, defaultValue = isSystemInDarkTheme())

            GameOfLifeTheme(darkTheme = AppThemeUtil.isDarkMode.value) {
                AppLayout()
            }
        }
    }
}
