package kr.young.sudoku

import java.util.*
import kotlin.random.Random

class Sudoku constructor(private val size: Int) {
    private val realSize = size * size

    private val sudokuMap = Array(realSize) { IntArray(realSize) { 0 } }
    private val fixMap: Array<BooleanArray>? = null

    private val totalCandidateMap = Array(realSize) {
        Array(realSize) {
            BooleanArray(realSize) { false }
        }
    }

    private val rowCheckMap = Array(realSize) { BooleanArray(realSize) { false } }
    private val columnCheckMap = Array(realSize) { BooleanArray(realSize) { false } }
    private val squareCheckMap = Array(realSize) { BooleanArray(realSize) { false } }

    fun make() {
        val startTime = System.currentTimeMillis()

        loopMake()
//        recursiveMake(0, 0)

        val endTime = System.currentTimeMillis()
        println("Time : ${endTime - startTime}")
        printSudokuMap()
    }

    private fun loopMake() {
        var rowIdx = 0
        var colIdx = 0
        var isError = true
        while (isError) {
            for (i in rowIdx until realSize) {
                for (j in colIdx until realSize) {
                    val num = getRandomNumber(i, j)
                    if (num == 0) {
                        if (i == 0 && j == 0) {
                            println("error")
                        } else if (j == 0) {
                            deleteNumber(i-1, realSize-1)
                            rowIdx = i-1
                            colIdx = realSize-1
                        } else {
                            deleteNumber(i, j-1)
                            rowIdx = i
                            colIdx = j-1
                        }
                        isError = true
                        break
                    } else {
                        sudokuMap[i][j] = num
                        isError = false
                        colIdx = 0
                    }
                }
                if (isError) {
                    break
                }
            }
        }
    }

    private fun recursiveMake(i: Int, j: Int) {
        val num = getRandomNumber(i, j)
        if (num == 0) {
            if (i == 0 && j == 0) {
                println("error")
                return
            } else if (j == 0) {
                deleteNumber(i-1, realSize-1)
                recursiveMake(i-1, realSize-1)
            } else {
                deleteNumber(i, j-1)
                recursiveMake(i, j-1)
            }
        } else {
            sudokuMap[i][j] = num
            if (i == realSize - 1 && j == realSize - 1) {
                return
            } else if (j == realSize - 1) {
                recursiveMake(i+1, 0)
            } else {
                recursiveMake(i, j+1)
            }
        }
    }

    private fun getRandomNumber(i: Int, j: Int): Int {
        val candidates = IntArray(realSize)
        var candidateIdx = 0
        for (idx in 0 until realSize) {
            if (!rowCheckMap[i][idx] && !columnCheckMap[j][idx] &&
                !squareCheckMap[i/size*size+j/size][idx] && !totalCandidateMap[i][j][idx]) {
                candidates[candidateIdx] = idx + 1
                candidateIdx++
            }
        }
        if (candidateIdx == 0) {
            return 0
        }
        val num = candidates[Random.nextInt(candidateIdx)]
        rowCheckMap[i][num-1] = true
        columnCheckMap[j][num-1] = true
        squareCheckMap[i/size*size+j/size][num-1] = true
        totalCandidateMap[i][j][num-1] = true
        return num
    }

    private fun deleteNumber(i: Int, j:Int) {
        val num = sudokuMap[i][j]
        rowCheckMap[i][num-1] = false
        columnCheckMap[j][num-1] = false
        squareCheckMap[i / size * size + j / size][num-1] = false

        for (idx in 0 until realSize) {
            if (j == realSize - 1) {
                totalCandidateMap[i+1][0][idx] = false
            } else {
                totalCandidateMap[i][j+1][idx] = false
            }
        }
        sudokuMap[i][j] = 0
    }

    private fun printSudokuMap() {
        for ((i, row) in sudokuMap.withIndex()) {
            for ((j, num) in row.withIndex()) {
//                if (num == 0) {
//                    println()
//                    return
//                }
                if (realSize < 10) {
                    print(String.format(Locale.getDefault(), " %d ", num))
                } else {
                    print(String.format(Locale.getDefault(), " %02d ", num))
                }
                if (j % size == size - 1 && j != realSize - 1) {
                    print(" | ")
                }
            }
            println()
            if (i % size == size - 1 && i != realSize - 1) {
                println("---------------------------------")
            }
        }
    }

}