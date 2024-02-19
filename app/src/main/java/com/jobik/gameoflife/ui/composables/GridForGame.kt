package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun GridForGame(array: List<List<Boolean>>, onElementClick: (row: Int, column: Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(array.size),
    ) {
        items(array.size * array.first().size) { index ->
            val row = index / array.first().size
            val col = index % array.first().size
            OneGameUnit(state = array[row][col], onClick = { onElementClick(row, col) })
        }
    }
}