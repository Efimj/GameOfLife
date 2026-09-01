package com.jobik.gameoflife.screens.game

import androidx.annotation.Keep
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SavedGameRules(
    val id: String,
    val name: String,
    val rules: GameOfLifeStepSettings,
)

fun GameOfLifeStepSettings.toRuleNotation(): String {
    val birth = neighborsForReviving.sorted().joinToString(separator = "")
    val survival = neighborsForAlive.sorted().joinToString(separator = "")
    return "B$birth/S$survival"
}
