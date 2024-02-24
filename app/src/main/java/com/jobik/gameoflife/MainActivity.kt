package com.jobik.gameoflife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.jobik.gameoflife.screens.AppLayout.AppLayout
import com.jobik.gameoflife.ui.theme.GameOfLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            GameOfLifeTheme (true){
                AppLayout()
            }
        }
    }
}
