package com.jobik.gameoflife.screens.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeResult
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettingsDefault
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.cloneGameState
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countAlive
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countDeaths
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countRevives
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeOneStepGameOfLife
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeRandomStartStates
import com.jobik.gameoflife.gameOfLife.GameSettings
import com.jobik.gameoflife.helpers.ArrayHelper.Companion.generateTwoDimList
import com.jobik.gameoflife.util.settings.SettingsManager.settings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GameScreenStates(
    val isSimulationRunning: Boolean = false,
    val alive: Int = 0,
    val deaths: Int = 0,
    val revivals: Int = 0,
    val stepNumber: Int = 0,
    val gameSettings: GameSettings = settings.gameSettings,
    val currentStep: List<List<GameOfLifeUnitState>> = List(gameSettings.rows) { List(gameSettings.cols) { GameOfLifeUnitState.Empty } },
    val previousStepsHash: List<Int> = emptyList(),
    val gameResult: GameOfLifeResult? = null,
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
            rows = states.value.gameSettings.rows,
            cols = states.value.gameSettings.cols,
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
            previousStepsHash = emptyList(),
            gameResult = null,
        )
    }

    private fun startSimulation() {
        simulationJob?.cancel()

        simulationJob = viewModelScope.launch {
            while (true) {
                val gameSettings = states.value.gameSettings

                if (gameSettings.skipSteps == 0 || states.value.stepNumber % gameSettings.skipSteps == 0)
                    delay(gameSettings.oneStepDurationMills)

                var nextStep = makeOneStepGameOfLife(
                    currentState = states.value.currentStep,
                    settings = gameSettings.gameOfLifeStepRules
                )

                val gameResult = checkIsGameFinishedResult(
                    nextState = nextStep,
                    previousStepsHash = states.value.previousStepsHash,
                    previousStep = states.value.currentStep
                )

                if (gameResult != null) {
                    turnOffSimulation()
                    _states.value = states.value.copy(gameResult = gameResult)
                    return@launch
                }

                val aliveCount = countAlive(nextStep)
                var revives = states.value.revivals
                var deaths = states.value.deaths
                if (states.value.previousStepsHash.isNotEmpty()) {
                    revives += countRevives(nextStep, states.value.currentStep)
                    deaths += countDeaths(nextStep, states.value.currentStep)
                }

                if (gameSettings.freeSoulMode)
                    nextStep =
                        freeSouls(
                            nextState = nextStep,
                            previousState = states.value.currentStep
                        )

                _states.value = states.value.copy(
                    currentStep = nextStep,
                    alive = aliveCount,
                    deaths = deaths,
                    revivals = revives,
                    previousStepsHash = states.value.previousStepsHash + nextStep.hashCode(),
                    stepNumber = states.value.stepNumber + 1
                )
            }
        }
    }

    private fun checkIsGameFinishedResult(
        nextState: List<List<GameOfLifeUnitState>>,
        previousStepsHash: List<Int>,
        previousStep: List<List<GameOfLifeUnitState>>
    ): GameOfLifeResult? {
        if (previousStepsHash.isEmpty()) return null
        var stableCombination = true
        var noSurvived = true

        for (row in nextState.indices) {
            for (col in nextState[row].indices) {
                if (nextState[row][col] != previousStep[row][col]) {
                    stableCombination = false
                }
                if (nextState[row][col] == GameOfLifeUnitState.Alive) {
                    noSurvived = false
                }
            }
        }

        if (noSurvived) return GameOfLifeResult.NoOneSurvived
        if (stableCombination) return GameOfLifeResult.StableCombination

        if (states.value.gameSettings.loopDetecting.not()) return null

        val currentStateHash = nextState.hashCode()
        if (previousStepsHash.contains(currentStateHash)) {
            return GameOfLifeResult.Loop
        }

        return null
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
        val isEmojiMode = states.value.gameSettings.emojiEnabled
        if (checkIsOutOfBounds(row, column, oldList)) return
        val newList = cloneGameState(oldList)
        newList[row][column] = when (newList[row][column]) {
            GameOfLifeUnitState.Alive -> GameOfLifeUnitState.Dead
            GameOfLifeUnitState.Dead -> GameOfLifeUnitState.Empty
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
            rows = states.value.gameSettings.rows,
            cols = states.value.gameSettings.cols,
            initialValue = GameOfLifeUnitState.Alive,
        )

        _states.value = states.value.copy(
            currentStep = list,
            gameResult = null,
            stepNumber = 0,
            alive = states.value.gameSettings.cols * states.value.gameSettings.rows,
            deaths = 0,
            previousStepsHash = emptyList()
        )
    }

    fun setFullDeath() {
        val list = generateTwoDimList(
            rows = states.value.gameSettings.rows,
            cols = states.value.gameSettings.cols,
            initialValue = GameOfLifeUnitState.Dead
        )
        _states.value = states.value.copy(
            currentStep = list,
            gameResult = null,
            stepNumber = 0,
            alive = 0,
            deaths = states.value.gameSettings.cols * states.value.gameSettings.rows,
            previousStepsHash = emptyList()
        )
    }

    fun setFullEmpty() {
        val list = generateTwoDimList(
            rows = states.value.gameSettings.rows,
            cols = states.value.gameSettings.cols,
            initialValue = GameOfLifeUnitState.Empty
        )
        _states.value =
            states.value.copy(
                currentStep = list,
                alive = 0,
                deaths = 0,
                gameResult = null,
                stepNumber = 0,
                previousStepsHash = emptyList(),
            )
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
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(oneStepDurationMills = duration))
    }

    fun switchFreeSoulMode() {
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(freeSoulMode = states.value.gameSettings.freeSoulMode.not()))
    }

    fun switchEmojiMode() {
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(emojiEnabled = states.value.gameSettings.emojiEnabled.not()))
    }

    fun switchLoopDetectingMode() {
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(loopDetecting = states.value.gameSettings.loopDetecting.not()))
    }

    fun switchShowDeadMode() {
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(showDead = states.value.gameSettings.showDead.not()))
    }

    val MaxGameDimension = 100

    fun setRows(matrixRowsString: String): Boolean {
        val matrixRows = matrixRowsString.toIntOrNull() ?: return false
        if (matrixRows == states.value.gameSettings.rows) return true
        if (matrixRows < 3) return false
        if (matrixRows > MaxGameDimension) return false

        _states.value = states.value.copy(
            gameSettings = states.value.gameSettings.copy(rows = matrixRows),
            gameResult = null,
            stepNumber = 0
        )
        regenerateGame()
        return true
    }

    fun setColumns(matrixColumnsString: String): Boolean {
        val matrixCols = matrixColumnsString.toIntOrNull() ?: return false
        if (matrixCols == states.value.gameSettings.cols) return true
        if (matrixCols < 1) return false
        if (matrixCols > MaxGameDimension) return false

        _states.value = states.value.copy(
            gameSettings = states.value.gameSettings.copy(cols = matrixCols),
            gameResult = null,
            stepNumber = 0
        )
        regenerateGame()
        return true
    }

    fun updateSkipSteps(stepsCount: Int) {
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(skipSteps = stepsCount))
    }

    fun updateGameRules(
        neighborsForReviving: Set<Int> = states.value.gameSettings.gameOfLifeStepRules.neighborsForReviving,
        neighborsForAlive: Set<Int> = states.value.gameSettings.gameOfLifeStepRules.neighborsForAlive
    ) {
        val newRules = states.value.gameSettings.gameOfLifeStepRules.copy(
            neighborsForReviving = neighborsForReviving,
            neighborsForAlive = neighborsForAlive
        )
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(gameOfLifeStepRules = newRules))
    }

    fun gameRulesToDefault() {
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(gameOfLifeStepRules = GameOfLifeStepSettingsDefault))
    }

    fun setRules(rules: GameRules) {
        if (rules.firstStep == null) {
            _states.value = states.value.copy(
                gameSettings = states.value.gameSettings.copy(
                    gameOfLifeStepRules = rules.rules
                ),
                isSimulationRunning = false,
                alive = 0,
                deaths = 0,
                revivals = 0,
                stepNumber = 0,
                previousStepsHash = emptyList(),
            )
        } else {
            _states.value = states.value.copy(
                gameSettings = states.value.gameSettings.copy(
                    gameOfLifeStepRules = rules.rules,
                    rows = rules.firstStep.size,
                    cols = rules.firstStep.first().size,
                ),
                isSimulationRunning = false,
                alive = 0,
                deaths = 0,
                revivals = 0,
                stepNumber = 0,
                previousStepsHash = emptyList(),
                currentStep = rules.firstStep
            )
        }
    }

    fun updateScale(value: Float) {
        if (value < 0.5f) return
        if (value > 1.5f) return
        _states.value =
            states.value.copy(gameSettings = states.value.gameSettings.copy(scale = value))
    }
}