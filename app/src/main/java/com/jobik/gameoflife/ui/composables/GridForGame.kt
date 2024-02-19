package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.*
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState


@Composable
fun GridForGame(
    array: List<List<GameOfLifeUnitState>>,
    emojiMode: Boolean,
    content: @Composable () -> Unit,
    onElementClick: (row: Int, column: Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(array.size),
    ) {
        items(array.size * array.first().size) { index ->
            val row = index / array.first().size
            val col = index % array.first().size

            OneGameUnit(state = array[row][col], enableEmojis = emojiMode, onClick = { onElementClick(row, col) })
        }
        item(span = {
            // LazyGridItemSpanScope:
            // maxLineSpan
            GridItemSpan(maxLineSpan)
        }
        ) {
            content()
        }
    }
}