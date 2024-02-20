package com.jobik.gameoflife.ui.composables

import androidx.annotation.Keep
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState

@Keep
val AliveEmojis = listOf(
    "\uD83D\uDE03",
    "\uD83D\uDE04",
    "\uD83D\uDE01",
    "\uD83D\uDE06",
    "\uD83D\uDE07",
    "\uD83D\uDE0D",
    "\uD83E\uDD29",
    "\uD83E\uDD11",
    "\uD83D\uDE0B",
)

@Keep
val DeadEmojis = listOf(
    "\uD83D\uDC80",
    "☠\uFE0F",
)

@Composable
fun OneGameUnit(
    modifier: Modifier = Modifier,
    state: GameOfLifeUnitState,
    enableEmojis: Boolean = true,
    onClick: () -> Unit
) {
    val backgroundColorValue = when {
        enableEmojis -> Color.Transparent
        state == GameOfLifeUnitState.Alive -> MaterialTheme.colorScheme.tertiary
        else -> Color.Transparent
    }

    val background by animateColorAsState(targetValue = backgroundColorValue, label = "background")

    val text by remember(state) {
        mutableStateOf(
            when (state) {
                GameOfLifeUnitState.Alive -> AliveEmojis.random()
                GameOfLifeUnitState.Dead -> DeadEmojis.random()
                else -> ""
            }
        )
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        if (enableEmojis) {
            Text(text = text)
        }
    }
}