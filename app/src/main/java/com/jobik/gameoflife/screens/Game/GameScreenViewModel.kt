package com.jobik.gameoflife.screens.Game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeResult
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettingsDefault
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.cloneGameState
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countAlive
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countDeaths
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countRevives
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeOneStepGameOfLife
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeRandomStartStates
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.helpers.ArrayHelper.Companion.generateTwoDimList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GameScreenStates(
    val isSimulationRunning: Boolean = false,
    val freeSoulMode: Boolean = true,
    val emojiEnabled: Boolean = false,
    val rows: Int = 32,
    val cols: Int = 32,
    val alive: Int = 0,
    val deaths: Int = 0,
    val revivals: Int = 0,
    val stepNumber: Long = 0,
    val currentStep: List<List<GameOfLifeUnitState>> = List(rows) { List(cols) { GameOfLifeUnitState.Empty } },
    val previousStep: List<List<GameOfLifeUnitState>> = emptyList(),
    val gameResult: GameOfLifeResult? = null,
    val gameOfLifeStepRules: GameOfLifeStepSettings = GameOfLifeStepSettingsDefault,
    val oneStepDurationMills: Long = 500,
    val scale: Float = 1f
)

class GameScreenViewModel : ViewModel() {
    private val _states = mutableStateOf(GameScreenStates())
    val states: State<GameScreenStates> = _states
    private var simulationJob: Job? = null

    init {
        regenerateGame()
    }

    private fun regenerateGame() {
        simulationJob?.cancel()
        val list = generateTwoDimList(
            rows = states.value.rows,
            cols = states.value.cols,
            initialValue = GameOfLifeUnitState.Empty
        )
        val startState = makeRandomStartStates(list)
        val aliveCount = countAlive(startState)
        _states.value = states.value.copy(
            currentStep = startState,
            alive = aliveCount,
            deaths = 0,
            revivals = 0,
            isSimulationRunning = false,
            stepNumber = 0,
            previousStep = emptyList(),
            gameResult = null,
        )
    }

    private fun startSimulation() {
        simulationJob?.cancel()

        simulationJob = viewModelScope.launch {
            while (true) {
                delay(states.value.oneStepDurationMills)
                var nextStep = makeOneStepGameOfLife(
                    currentState = states.value.currentStep,
                    settings = states.value.gameOfLifeStepRules
                )

                val gameResult = checkIsGameFinishedResult(nextStep, states.value.previousStep)
                if (gameResult != null) {
                    turnOffSimulation()
                    _states.value = states.value.copy(gameResult = gameResult)
                    return@launch
                }

                val aliveCount = countAlive(nextStep)
                var revives = states.value.revivals
                var deaths = states.value.deaths
                if (states.value.previousStep.isNotEmpty()) {
                    revives += countRevives(nextStep, states.value.previousStep)
                    deaths += countDeaths(nextStep, states.value.previousStep)
                }

                if (states.value.freeSoulMode)
                    nextStep =
                        freeSouls(nextState = nextStep, previousState = states.value.previousStep)

                _states.value = states.value.copy(
                    currentStep = nextStep,
                    alive = aliveCount,
                    deaths = deaths,
                    revivals = revives,
                    previousStep = nextStep,
                    stepNumber = states.value.stepNumber + 1
                )
            }
        }
    }

    private fun checkIsGameFinishedResult(
        nextState: List<List<GameOfLifeUnitState>>,
        previousState: List<List<GameOfLifeUnitState>>
    ): GameOfLifeResult? {
        if (previousState.isEmpty()) return null

        var isStable = false
        for (row in nextState.indices) {
            for (col in nextState[row].indices) {
                if (nextState[row][col] != previousState[row][col])
                    return null
                if (nextState[row][col] == GameOfLifeUnitState.Alive)
                    isStable = true
            }
        }

        if (isStable) return GameOfLifeResult.StableCombination
        return GameOfLifeResult.NoOneSurvived
    }

    private fun freeSouls(
        nextState: List<List<GameOfLifeUnitState>>,
        previousState: List<List<GameOfLifeUnitState>>
    ): List<List<GameOfLifeUnitState>> {
        if (previousState.isEmpty()) return nextState

        val newState = cloneGameState(nextState)

        for (row in newState.indices) {
            for (col in newState[row].indices) {
                if (previousState[row][col] == GameOfLifeUnitState.Dead && newState[row][col] != GameOfLifeUnitState.Alive)
                    newState[row][col] = GameOfLifeUnitState.Empty
            }
        }

        return newState
    }

    fun dropGame() {
        regenerateGame()
    }


    fun onElementClick(row: Int, column: Int) {
        val oldList = states.value.currentStep
        val isEmojiMode = states.value.emojiEnabled
        if (checkIsOutOfBounds(row, column, oldList)) return
        val newList = cloneGameState(oldList)
        newList[row][column] = when (newList[row][column]) {
            GameOfLifeUnitState.Alive -> GameOfLifeUnitState.Dead
            GameOfLifeUnitState.Dead -> if (isEmojiMode) GameOfLifeUnitState.Empty else GameOfLifeUnitState.Alive
            GameOfLifeUnitState.Empty -> GameOfLifeUnitState.Alive
        }
        val aliveCount = countAlive(newList)
        _states.value = states.value.copy(
            currentStep = newList,
            alive = aliveCount,
            gameResult = null,
            stepNumber = 0
        )
    }

    private fun checkIsOutOfBounds(
        row: Int,
        column: Int,
        state: List<List<GameOfLifeUnitState>>,
    ): Boolean {
        if (row > state.size) return true
        if (column > state.first().size) return true
        return false
    }

    fun setFullAlive() {
        val list = generateTwoDimList(
            rows = states.value.rows,
            cols = states.value.cols,
            initialValue = GameOfLifeUnitState.Alive,
        )

        _states.value = states.value.copy(currentStep = list, gameResult = null, stepNumber = 0)
    }

    fun setFullDeath() {
        val list = generateTwoDimList(
            rows = states.value.rows,
            cols = states.value.cols,
            initialValue = GameOfLifeUnitState.Dead
        )
        _states.value = states.value.copy(currentStep = list, gameResult = null, stepNumber = 0)
    }

    fun setFullEmpty() {
        val list = generateTwoDimList(
            rows = states.value.rows,
            cols = states.value.cols,
            initialValue = GameOfLifeUnitState.Empty
        )
        _states.value =
            states.value.copy(currentStep = list, alive = 0, gameResult = null, stepNumber = 0)
    }

    fun turnOnSimulation() {
        _states.value = states.value.copy(isSimulationRunning = true)
        startSimulation()
    }

    fun turnOffSimulation() {
        simulationJob?.cancel()
        _states.value = states.value.copy(isSimulationRunning = false)
    }

    fun changeStepDuration(duration: Long) {
        _states.value = states.value.copy(oneStepDurationMills = duration)
    }

    fun switchFreeSoulMode() {
        _states.value = states.value.copy(freeSoulMode = states.value.freeSoulMode.not())
    }

    fun switchEmojiMode() {
        _states.value = states.value.copy(emojiEnabled = states.value.emojiEnabled.not())
    }

    val MaxGameDimension = 100

    fun setRows(matrixRowsString: String): Boolean {
        val matrixRows = matrixRowsString.toIntOrNull() ?: return false
        if (matrixRows == states.value.rows) return true
        if (matrixRows < 3) return false
        if (matrixRows > MaxGameDimension) return false

        _states.value = states.value.copy(rows = matrixRows, gameResult = null, stepNumber = 0)
        regenerateGame()
        return true
    }

    fun setColumns(matrixColumnsString: String): Boolean {
        val matrixCols = matrixColumnsString.toIntOrNull() ?: return false
        if (matrixCols == states.value.cols) return true
        if (matrixCols < 1) return false
        if (matrixCols > MaxGameDimension) return false

        _states.value = states.value.copy(cols = matrixCols, gameResult = null, stepNumber = 0)
        regenerateGame()
        return true
    }

    fun updateGameRules(
        neighborsForReviving: Set<Int> = states.value.gameOfLifeStepRules.neighborsForReviving,
        neighborsForAlive: Set<Int> = states.value.gameOfLifeStepRules.neighborsForAlive
    ) {
        val newRules = states.value.gameOfLifeStepRules.copy(
            neighborsForReviving = neighborsForReviving,
            neighborsForAlive = neighborsForAlive
        )
        _states.value = states.value.copy(gameOfLifeStepRules = newRules)
    }

    fun gameRulesToDefault() {
        _states.value = states.value.copy(gameOfLifeStepRules = GameOfLifeStepSettingsDefault)
    }

    fun setRules(rules: GameRules) {
        if (rules.firstStep == null) {
            _states.value = states.value.copy(
                gameOfLifeStepRules = rules.rules,
                isSimulationRunning = false,
                alive = 0,
                deaths = 0,
                revivals = 0,
                stepNumber = 0,
                previousStep = emptyList(),
            )
        } else {
            _states.value = states.value.copy(
                gameOfLifeStepRules = rules.rules,
                isSimulationRunning = false,
                rows = rules.firstStep.size,
                cols = rules.firstStep.first().size,
                alive = 0,
                deaths = 0,
                revivals = 0,
                stepNumber = 0,
                previousStep = emptyList(),
                currentStep = rules.firstStep
            )
        }
    }

    fun updateScale(value: Float) {
        if (value < 0.5f) return
        if (value > 1.5f) return
        _states.value = states.value.copy(scale = value)
    }
}