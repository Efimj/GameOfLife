package com.jobik.gameoflife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jobik.gameoflife.screens.AppLayout.AppLayout
import com.jobik.gameoflife.screens.MainScreen.GameScreen
import com.jobik.gameoflife.ui.theme.GameOfLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameOfLifeTheme {
                AppLayout()
            }
        }
    }
}
