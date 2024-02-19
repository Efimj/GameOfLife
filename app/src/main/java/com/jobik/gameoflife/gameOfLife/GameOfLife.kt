package com.jobik.gameoflife.gameOfLife

import com.jobik.gameoflife.helpers.ArrayHelper

class GameOfLife {
    companion object {
        fun getNumberOfNeighbors(row: Int, col: Int, list: List<List<Boolean>>): Int {
            val rowStart = maxOf(0, row - 1)
            val rowEnd = minOf(list.size - 1, row + 1)
            val colStart = maxOf(0, col - 1)
            val colEnd = minOf(list[0].size - 1, col + 1)

            var neighbors = 0
            for (i in rowStart..rowEnd) {
                for (j in colStart..colEnd) {
                    if (i != row || j != col) {
                        if (list[i][j]) neighbors++
                    }
                }
            }

            return neighbors
        }

        data class GameOfLifeStepSettings(
            val neighborsForReviving: Int = 3,
            val minimumNeighborsForAlive: Int = 2,
            val maximumNeighborsForAlive: Int = 3,
        )

        fun makeOneStepGameOfLife(
            currentState: List<List<Boolean>>,
            settings: GameOfLifeStepSettings = GameOfLifeStepSettings()
        ): List<List<Boolean>> {
            val newState = ArrayHelper.cloneList(currentState)
            for (row in currentState.indices) {
                for (col in currentState[row].indices) {
                    val numberOfNeighbors = getNumberOfNeighbors(list = currentState, row = row, col = col)
                    if (numberOfNeighbors > settings.maximumNeighborsForAlive || numberOfNeighbors < settings.minimumNeighborsForAlive) newState[row][col] =
                        false
                    if (numberOfNeighbors == settings.neighborsForReviving) newState[row][col] = true
                    if (newState[row][col] && numberOfNeighbors >= settings.minimumNeighborsForAlive && numberOfNeighbors <= settings.maximumNeighborsForAlive) continue
                }
            }
            return newState
        }

        fun countAlive(newStateOfGame: List<List<Boolean>>): Int {
            var aliveCount = 0
            for (row in newStateOfGame.indices) {
                for (col in newStateOfGame.first().indices) {
                    if (newStateOfGame[row][col]) aliveCount++
                }
            }
            return aliveCount
        }
    }
}