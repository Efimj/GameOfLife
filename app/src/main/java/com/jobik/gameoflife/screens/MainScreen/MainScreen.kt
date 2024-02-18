package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jobik.gameoflife.ui.composables.GridForGame

@Composable
fun MainScreen(viewModel: MainScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GridForGame(array = viewModel.states.value.currentStepValues, onElementClick = viewModel::onElementClick)
        Button(onClick = viewModel::turnOnSimulation) {
            Text(text = "Run")
        }
        Button(onClick = viewModel::turnOffSimulation) {
            Text(text = "Stop")
        }
    }
}