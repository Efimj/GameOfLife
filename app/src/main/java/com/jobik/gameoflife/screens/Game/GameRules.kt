package com.jobik.gameoflife.screens.Game

import android.content.Context
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState
import com.jobik.gameoflife.helpers.ArrayHelper

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
    val firstStep: List<List<GameOfLifeUnitState>>?,
    val rules: GameOfLifeStepSettings,
)

private val FullyAlive = List(10) { List(10) { GameOfLifeUnitState.Alive } }
private val FullyDead = List(10) { List(10) { GameOfLifeUnitState.Dead } }
private fun getRandomStartStates(rows: Int = 15, cols: Int = 15): List<List<GameOfLifeUnitState>> {
    val list = ArrayHelper.generateTwoDimList(rows = rows, cols = cols, initialValue = GameOfLifeUnitState.Empty)
    return GameOfLife.makeRandomStartStates(list)
}

// List(10) { List(10) { GameOfLifeUnitState.Dead }.toMutableList() }.apply {
//            last()[3] = GameOfLifeUnitState.Alive
//        },

val GameRuleSet = listOf(
    GameRules(
        title = R.string.rs_conways_life,
        type = GameRulesType.CHAOTIC,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3), neighborsForAlive = setOf(2, 3)),
    ),
    GameRules(
        title = R.string.rs_two_x_two,
        type = GameRulesType.CHAOTIC,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 6), neighborsForAlive = setOf(1, 2, 5)),
    ),
    GameRules(
        title = R.string.rs_thirty_four_life,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 4), neighborsForAlive = setOf(3, 4)),
    ),
    GameRules(
        title = R.string.rs_assimilation,
        type = GameRulesType.STABLE,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 4, 5), neighborsForAlive = setOf(4, 5, 6, 7)),
    ),
    GameRules(
        title = R.string.rs_coagulations,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(3, 7, 8),
            neighborsForAlive = setOf(2, 3, 5, 6, 7, 8)
        ),
    ),
    GameRules(
        title = R.string.rs_coral,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3), neighborsForAlive = setOf(4, 5, 6, 7, 8)),
    ),
    GameRules(
        title = R.string.rs_day_and_night,
        type = GameRulesType.STABLE,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(3, 6, 7, 8),
            neighborsForAlive = setOf(3, 4, 6, 7, 8)
        ),
    ),
    GameRules(
        title = R.string.rs_diamoeba,
        type = GameRulesType.CHAOTIC,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(5, 6, 7, 8),
            neighborsForAlive = setOf(3, 5, 6, 7, 8)
        ),
    ),
    GameRules(
        title = R.string.rs_flakes,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(3),
            neighborsForAlive = setOf(0, 1, 2, 3, 4, 5, 6, 7, 8)
        ),
    ),
    GameRules(
        title = R.string.rs_gnarl,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(1), neighborsForAlive = setOf(1)),
    ),
    GameRules(
        title = R.string.rs_high_life,
        type = GameRulesType.CHAOTIC,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 6), neighborsForAlive = setOf(2, 3)),
    ),
    GameRules(
        title = R.string.rs_inverse_life,
        type = GameRulesType.CHAOTIC,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(0, 1, 2, 3, 4, 7, 8),
            neighborsForAlive = setOf(3, 4, 6, 7, 8)
        ),
    ),
    GameRules(
        title = R.string.rs_long_life,
        type = GameRulesType.STABLE,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 4, 5), neighborsForAlive = setOf(5)),
    ),
    GameRules(
        title = R.string.rs_maze,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3), neighborsForAlive = setOf(1, 2, 3, 4, 5)),
    ),
    GameRules(
        title = R.string.rs_mazectric,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3), neighborsForAlive = setOf(1, 2, 3, 4)),
    ),
    GameRules(
        title = R.string.rs_move,
        type = GameRulesType.STABLE,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 6, 8), neighborsForAlive = setOf(2, 4, 5)),
    ),
    GameRules(
        title = R.string.rs_pseudo_life,
        type = GameRulesType.CHAOTIC,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(3, 5, 7), neighborsForAlive = setOf(2, 3, 8)),
    ),
    GameRules(
        title = R.string.rs_replicator,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(1, 3, 5, 7), neighborsForAlive = setOf(1, 3, 5, 7)),
    ),
    GameRules(
        title = R.string.rs_seeds,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(2), neighborsForAlive = setOf()),
    ),
    GameRules(
        title = R.string.rs_serviettes,
        type = GameRulesType.EXPLODING,
        firstStep = null,
        rules = GameOfLifeStepSettings(neighborsForReviving = setOf(2, 3, 4), neighborsForAlive = setOf()),
    ),
    GameRules(
        title = R.string.rs_stains,
        type = GameRulesType.STABLE,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(3, 6, 7, 8),
            neighborsForAlive = setOf(2, 3, 5, 6, 7, 8)
        ),
    ),
    GameRules(
        title = R.string.rs_walled_cities,
        type = GameRulesType.STABLE,
        firstStep = null,
        rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(4, 5, 6, 7, 8),
            neighborsForAlive = setOf(2, 3, 4, 5)
        ),
    ),
)