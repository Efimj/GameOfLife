package com.jobik.gameoflife.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState


@Composable
fun GridForGame(
    array: List<List<GameOfLifeUnitState>>,
    emojiMode: Boolean,
    onElementClick: (row: Int, column: Int) -> Unit
) {
    val localConfiguration = LocalConfiguration.current
    val maxHeight = localConfiguration.screenHeightDp.coerceAtMost(localConfiguration.screenWidthDp)

    LazyVerticalGrid(
        modifier = Modifier
            .heightIn(max = maxHeight.dp)
            .widthIn(max = maxHeight.dp)
            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.outline), MaterialTheme.shapes.medium),
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