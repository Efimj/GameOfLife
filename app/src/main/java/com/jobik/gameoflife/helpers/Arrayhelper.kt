package com.jobik.gameoflife.helpers

import kotlin.random.Random

class ArrayHelper {
    companion object {
        inline fun <reified T> generateTwoDimList(rows: Int, cols: Int, initialValue: T): MutableList<MutableList<T>> {
            return MutableList(rows) { MutableList(cols) { initialValue } }
        }

        fun fillTwoDimListRandomly(
            list: MutableList<MutableList<Boolean>>,
        ) {
            for (i in list.indices) {
                for (j in list[i].indices) {
                    list[i][j] = Random.nextBoolean()
                }
            }
        }

        fun cloneList(list: List<List<Boolean>>): MutableList<MutableList<Boolean>> {
            val newArray = generateTwoDimList(rows = list.size, cols = list[list.size - 1].size, false)
            for (i in list.indices) {
                for (j in list[i].indices) {
                    newArray[i][j] = list[i][j]
                }
            }
            return newArray
        }
    }
}