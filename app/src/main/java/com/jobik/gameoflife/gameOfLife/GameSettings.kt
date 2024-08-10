package com.jobik.gameoflife.gameOfLife

import androidx.annotation.Keep
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettingsDefault
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class GameSettings(
    val freeSoulMode: Boolean = true,
    val emojiEnabled: Boolean = false,
    val rows: Int = 32,
    val cols: Int = 32,
    val gameOfLifeStepRules: GameOfLifeStepSettings = GameOfLifeStepSettingsDefault,
    val oneStepDurationMills: Long = 500,
    val scale: Float = 1f
)