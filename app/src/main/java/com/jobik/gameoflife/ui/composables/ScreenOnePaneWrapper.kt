package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass

@Composable
fun ScreenOnePaneWrapper(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = horizontalAlignment,
    ) {
        Column(
            modifier = modifier.widthIn(max = WindowWidthSizeClass.Medium.minValue.dp),
            verticalArrangement = verticalArrangement
        ) {
            content()
        }
    }
}