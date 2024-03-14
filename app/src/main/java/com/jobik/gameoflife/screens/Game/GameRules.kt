package com.jobik.gameoflife.screens.Game

import android.content.Context
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState

@Keep
enum class GameRulesType {
    STABLE,
    CHAOTIC,
    EXPLODING;

    fun getLocalizedValue(context: Context): String {
        return when (name) {
            STABLE.name -> context.getString(R.string.stable)
            CHAOTIC.name -> context.getString(R.string.chaotic)
            EXPLODING.name -> context.getString(R.string.exploding)
            else -> context.getString(R.string.undefined)
        }
    }
}

data class GameRules(
    @StringRes val title: Int,
    val type: GameRulesType,
    val firstStep: List<List<GameOfLifeUnitState>>,
    val rules: GameOfLifeStepSettings,
)

private val FullyAlive = List(10) { List(10) { GameOfLifeUnitState.Alive } }
private val FullyDead = List(10) { List(10) { GameOfLifeUnitState.Dead } }

val GameRuleSet = listOf(
    GameRules(
        title = R.string.rules,
        type = GameRulesType.CHAOTIC,
        firstStep = List(10) { List(10) { GameOfLifeUnitState.Dead }.toMutableList() }.apply {
            last()[3] = GameOfLifeUnitState.Alive
        },
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 6), neighborsForAlive = setOf(1, 2, 5)),
    )
)