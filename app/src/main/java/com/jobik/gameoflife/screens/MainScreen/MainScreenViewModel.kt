package com.jobik.gameoflife.screens.MainScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.countAlive
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.makeOneStepGameOfLife
import com.jobik.gameoflife.helpers.ArrayHelper.Companion.cloneList
import com.jobik.gameoflife.helpers.ArrayHelper.Companion.fillTwoDimListRandomly
import com.jobik.gameoflife.helpers.ArrayHelper.Companion.generateTwoDimList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MainScreenStates(
    val isSimulationRunning: Boolean = false,
    val rows: Int = 10,
    val columns: Int = 10,
    val aliveCount: Int = 0,
    val currentStep: Long = 0,
    val currentStepValues: List<List<Boolean>> = List(rows) { List(columns) { false } },
    val oneStepDurationMills: Long = 200,
)

class MainScreenViewModel : ViewModel() {
    private val _states = mutableStateOf(MainScreenStates())
    val states: State<MainScreenStates> = _states

    init {
        val list = generateTwoDimList(rows = states.value.rows, cols = states.value.columns, initialValue = false)
        fillTwoDimListRandomly(list)
        val aliveCount = countAlive(list)
        _states.value = states.value.copy(currentStepValues = list, aliveCount = aliveCount)
    }

    fun onElementClick(row: Int, column: Int) {
        val oldList = states.value.currentStepValues
        if (checkIsOutOfBounds(row, column, oldList)) return
        val newList = cloneList(oldList)
        newList[row][column] = newList[row][column].not()
        val aliveCount = countAlive(newList)
        _states.value = states.value.copy(currentStepValues = newList, aliveCount = aliveCount)
    }

    private fun checkIsOutOfBounds(
        row: Int,
        column: Int,
        oldList: List<List<Boolean>>,
    ): Boolean {
        if (row > oldList.size) return true
        if (column > oldList.first().size) return true
        return false
    }

    private var simulationJob: Job? = null

    private fun startSimulation() {
        simulationJob?.cancel()

        simulationJob = viewModelScope.launch {
            wipeStep()
            while (true) {
                updateStep()
                delay(states.value.oneStepDurationMills)
                val newStateOfGame = makeOneStepGameOfLife(currentState = states.value.currentStepValues)
                val aliveCount = countAlive(newStateOfGame)
                _states.value = states.value.copy(currentStepValues = newStateOfGame, aliveCount = aliveCount)
            }
        }
    }

    private fun stopSimulation() {
        simulationJob?.cancel()

    }

    fun updateStep() {
        _states.value = states.value.copy(currentStep = states.value.currentStep + 1)
    }

    fun wipeStep() {
        _states.value = states.value.copy(currentStep = 0)
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
}