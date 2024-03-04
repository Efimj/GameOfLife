package com.jobik.gameoflife.screens.Information

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding

@Composable
fun InformationContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp, top = 10.dp),
    ) {}
}