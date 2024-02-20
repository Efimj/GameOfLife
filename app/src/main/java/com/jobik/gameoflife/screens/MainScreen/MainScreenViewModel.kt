package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeUnitState
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.cloneGameState
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countAlive
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeOneStepGameOfLife
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeRandomStartStates
import com.jobik.gameoflife.helpers.ArrayHelper.Companion.generateTwoDimList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MainScreenStates(
    val isSimulationRunning: Boolean = false,
    val freeSoulMode: Boolean = true,
    val emojiEnabled: Boolean = true,
    val rows: Int = 10,
    val columns: Int = 10,
    val aliveCount: Int = 0,
    val stepNumber: Long = 0,
    val currentStep: List<List<GameOfLifeUnitState>> = List(rows) { List(columns) { GameOfLifeUnitState.Empty } },
    val previousStep: List<List<GameOfLifeUnitState>> = emptyList(),
    val oneStepDurationMills: Long = 200,
)

class MainScreenViewModel : ViewModel() {
    private val _states = mutableStateOf(MainScreenStates())
    val states: State<MainScreenStates> = _states
    private var simulationJob: Job? = null

    init {
        regenerateGame()
    }

    private fun regenerateGame() {
        simulationJob?.cancel()
        val list = generateTwoDimList(
            rows = states.value.rows,
            cols = states.value.columns,
            initialValue = GameOfLifeUnitState.Empty
        )
        val startState = makeRandomStartStates(list)
        val aliveCount = countAlive(startState)
        _states.value = states.value.copy(
            currentStep = startState,
            aliveCount = aliveCount,
            isSimulationRunning = false,
            stepNumber = 0,
            previousStep = emptyList()
        )
    }

    fun onElementClick(row: Int, column: Int) {
        val oldList = states.value.currentStep
        if (checkIsOutOfBounds(row, column, oldList)) return
        val newList = cloneGameState(oldList)
        newList[row][column] = when (newList[row][column]) {
            GameOfLifeUnitState.Alive -> GameOfLifeUnitState.Dead
            GameOfLifeUnitState.Dead -> GameOfLifeUnitState.Empty
            GameOfLifeUnitState.Empty -> GameOfLifeUnitState.Alive
        }
        val aliveCount = countAlive(newList)
        _states.value = states.value.copy(currentStep = newList, aliveCount = aliveCount)
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

    private fun startSimulation() {
        simulationJob?.cancel()

        simulationJob = viewModelScope.launch {
            while (true) {
                delay(states.value.oneStepDurationMills)
                var nextStep = makeOneStepGameOfLife(currentState = states.value.currentStep)
                val aliveCount = countAlive(nextStep)

                if (states.value.freeSoulMode)
                    nextStep = freeSouls(nextState = nextStep, previousState = states.value.previousStep)

                _states.value = states.value.copy(
                    currentStep = nextStep,
                    aliveCount = aliveCount,
                    previousStep = nextStep
                )
                updateStep()
            }
        }
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

    private fun stopSimulation() {
        simulationJob?.cancel()

    }

    fun updateStep() {
        _states.value = states.value.copy(stepNumber = states.value.stepNumber + 1)
    }

    fun turnOnSimulation() {
        _states.value = states.value.copy(isSimulationRunning = true)
        startSimulation()
    }

    fun turnOffSimulation() {
        _states.value = states.value.copy(isSimulationRunning = false)
        stopSimulation()
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
}