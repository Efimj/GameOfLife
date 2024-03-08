package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState

@Composable
fun GridForGame(
    array: List<List<GameOfLifeUnitState>>,
    emojiMode: Boolean,
    onElementClick: (row: Int, column: Int) -> Unit
) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(array.size),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(array.size * array.first().size) { index ->
                val row = index / array.first().size
                val col = index % array.first().size

                OneGameUnit(
                    state = array[row][col],
                    enableEmojis = emojiMode,
                    onClick = { onElementClick(row, col) })
            }
        }
}