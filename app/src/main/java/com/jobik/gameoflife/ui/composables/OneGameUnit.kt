package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OneGameUnit(state: Boolean, enableEmojis: Boolean = false, onClick: () -> Unit) {


    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .border(BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onTertiary))
            .background(if (state) MaterialTheme.colorScheme.onSurface else Color.Transparent)
    )
}