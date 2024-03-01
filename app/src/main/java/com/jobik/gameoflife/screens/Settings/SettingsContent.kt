package com.jobik.gameoflife.screens.Settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer

@Composable
fun SettingsContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(30) {
            Card {
                Row(modifier = Modifier.padding(40.dp)) {

                }
            }
        }
        BottomWindowInsetsSpacer()
    }
}