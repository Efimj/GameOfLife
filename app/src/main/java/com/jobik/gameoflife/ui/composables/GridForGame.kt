package com.jobik.gameoflife.ui.composables

import android.graphics.Paint
import androidx.annotation.Keep
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState
import com.jobik.gameoflife.screens.game.GameScreenViewModel

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
)

const val DefaultGameUnitSize = 20
const val DefaultGameGapWidth = 1

@Composable
fun GridForGame(
    viewModel: GameScreenViewModel,
) {
    val state = viewModel.states.value
    val cellSize by remember(state.gameSettings.scale) { mutableStateOf((DefaultGameUnitSize * state.gameSettings.scale).dp) }
    val cellSizePx = with(LocalDensity.current) { cellSize.toPx() }
    val cellSpacingPx = with(LocalDensity.current) { DefaultGameGapWidth.dp.toPx() }
    val cellEmojiUnit by remember(state.currentStep) {
        mutableStateOf(
            Array(state.currentStep.size) { row ->
                Array(state.currentStep.first().size) { col ->
                    when (state.currentStep[row][col]) {
                        GameOfLifeUnitState.Alive -> AliveEmojis.random()
                        GameOfLifeUnitState.Dead -> DeadEmojis.random()
                        GameOfLifeUnitState.Empty -> ""
                    }
                }
            })
    }

    val aliveUnitColor = MaterialTheme.colorScheme.primary
    val deadUnitColor = MaterialTheme.colorScheme.error

    val showDead = viewModel.states.value.gameSettings.showDead

    BoxWithConstraints {
        val fieldWidth = with(LocalDensity.current) { maxWidth.toPx() }
        val gapWidth = with(LocalDensity.current) { DefaultGameGapWidth.dp.toPx() }

        LaunchedEffect(cellSize) {
            val count = fieldWidth / (cellSizePx + gapWidth)

            viewModel.setRows(count.toInt().toString())
            viewModel.setColumns(count.toInt().toString())
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .pointerInput(state, cellSize) {
                    detectTapGestures { offset ->
                        val gridWidth = state.currentStep.size * (cellSizePx + cellSpacingPx)
                        val gridHeight =
                            state.currentStep.first().size * (cellSizePx + cellSpacingPx)

                        val offsetX = (size.width - gridWidth) / 2
                        val offsetY = (size.height - gridHeight) / 2

                        val x = ((offset.x - offsetX) / (cellSizePx + cellSpacingPx)).toInt()
                        val y = ((offset.y - offsetY) / (cellSizePx + cellSpacingPx)).toInt()

                        if (x in state.currentStep.indices && y in state.currentStep.first().indices) {
                            viewModel.onElementClick(x, y)
                        }
                    }
                }
        ) {
            val gridWidth = state.currentStep.size * (cellSizePx + cellSpacingPx)
            val gridHeight = state.currentStep.first().size * (cellSizePx + cellSpacingPx)

            val offsetX = (size.width - gridWidth) / 2
            val offsetY = (size.height - gridHeight) / 2

            for (x in 0 until state.currentStep.size) {
                for (y in 0 until state.currentStep.first().size) {
                    val cell = state.currentStep[x][y]

                    if (state.gameSettings.emojiEnabled) {
                        EmojiUnit(
                            emoji = cellEmojiUnit[x][y],
                            cellSizePx = cellSizePx,
                            cellSpacingPx = cellSpacingPx,
                            offsetX = offsetX,
                            x = x,
                            offsetY = offsetY,
                            y = y
                        )
                    } else {
                        if (cell == GameOfLifeUnitState.Alive) {
                            CircularUnit(
                                unitColor = aliveUnitColor,
                                offsetX = offsetX,
                                x = x,
                                cellSizePx = cellSizePx,
                                cellSpacingPx = cellSpacingPx,
                                offsetY = offsetY,
                                y = y
                            )
                        } else {
                            if (showDead && cell == GameOfLifeUnitState.Dead) {
                                CircularUnit(
                                    unitColor = deadUnitColor,
                                    offsetX = offsetX,
                                    x = x,
                                    cellSizePx = cellSizePx,
                                    cellSpacingPx = cellSpacingPx,
                                    offsetY = offsetY,
                                    y = y
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.CircularUnit(
    unitColor: Color,
    offsetX: Float,
    x: Int,
    cellSizePx: Float,
    cellSpacingPx: Float,
    offsetY: Float,
    y: Int
) {
    drawCircle(
        color = unitColor,
        center = Offset(
            offsetX + x * (cellSizePx + cellSpacingPx) + cellSizePx / 2,
            offsetY + y * (cellSizePx + cellSpacingPx) + cellSizePx / 2
        ),
        radius = (cellSizePx - cellSpacingPx) / 2
    )
}

private fun DrawScope.EmojiUnit(
    emoji: String,
    cellSizePx: Float,
    cellSpacingPx: Float,
    offsetX: Float,
    x: Int,
    offsetY: Float,
    y: Int
) {
    val textPaint = Paint().apply {
        color = Color.Black.toArgb()
        textSize = (cellSizePx - cellSpacingPx) * 0.85f
        textAlign = Paint.Align.CENTER
    }

    drawContext.canvas.nativeCanvas.drawText(
        emoji,
        offsetX + x * (cellSizePx + cellSpacingPx) + cellSizePx / 2,
        offsetY + y * (cellSizePx + cellSpacingPx) + cellSizePx / 2 + (cellSizePx - cellSpacingPx) / 4,  // Коррекция по Y для центрирования текста
        textPaint
    )
}