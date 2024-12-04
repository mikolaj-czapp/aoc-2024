package org.example.dec4

import org.example.InputReader

fun main() {
    val inputString = InputReader().readFile("dec4/input.txt")
    println("Number of XMAS in matrix: ${Dec4.task1(inputString)}")
    println("Number of X-MAS in matrix: ${Dec4.task2(inputString)}")
}

class Dec4 {
    companion object {
        fun task1(input: String): Int {
            val charMatrix = WordMatrix(prepareInput(input), "XMAS")
            return charMatrix.getWordCount()
        }

        fun task2(input: String): Int {
            val charMatrix = WordMatrix(prepareInput(input), "XMAS")
            return charMatrix.getXMasWordCount()
        }

        private fun prepareInput(input: String): List<List<Char>> = input.split(Regex("\n")).map { it.toList() }

        private class WordMatrix(
            val charMatrix: List<List<Char>>,
            val word: String,
        ) {
            init {
                val numberOfRows = charMatrix.size
                charMatrix.forEach { row -> assert(row.size == numberOfRows) }
            }

            fun getWordCount(): Int =
                charMatrix
                    .flatMapIndexed { indexRow: Int, chars: List<Char> ->
                        List(chars.size) { indexColumn ->
                            countInAllDirections(Pair(indexRow, indexColumn))
                        }
                    }.sum()

            fun getXMasWordCount(): Int =
                charMatrix
                    .flatMapIndexed { indexRow: Int, chars: List<Char> ->
                        List(chars.size) { indexColumn ->
                            countInDiagonalDirections(Pair(indexRow, indexColumn))
                        }
                    }.count { it }

            // This is ugly, I don't care
            fun countInDiagonalDirections(position: Pair<Int, Int>): Boolean {
                if (this.charMatrix[position.first][position.second] != 'A') {
                    return false
                }
                if (isCorrectOnRightDiagonal(position) && isCorrectOnLeftDiagonal(position)) {
                    return true
                }
                return false
            }

            fun countInAllDirections(position: Pair<Int, Int>): Int {
                val directions =
                    listOf(
                        Pair(0, 1),
                        Pair(0, -1),
                        Pair(1, 0),
                        Pair(-1, 0),
                        Pair(1, 1),
                        Pair(1, -1),
                        Pair(-1, -1),
                        Pair(-1, 1),
                    )
                if (this.charMatrix[position.first][position.second] != word[0]) {
                    return 0
                }
                var countValid = 0
                directions.forEach { direction ->
                    if (word.all {
                            val index = word.indexOf(it)
                            if (isValidDirection(position, direction, index)) {
                                return@all it ==
                                    charMatrix[position.first + direction.first * index][position.second + direction.second * index]
                            }
                            return@all false
                        }
                    ) {
                        countValid++
                    }
                }
                return countValid
            }

            private fun isCorrectOnRightDiagonal(position: Pair<Int, Int>): Boolean {
                val rightDiagonal =
                    listOf(
                        Pair(-1, 1),
                        Pair(1, -1),
                    )
                if (rightDiagonal.all {
                        isValidDirection(position, it, 1)
                    }
                ) {
                    return (
                        charMatrix[position.first + 1][position.second - 1] == 'S' &&
                            charMatrix[position.first - 1][position.second + 1] == 'M'
                    ) ||
                        (
                            charMatrix[position.first + 1][position.second - 1] == 'M' &&
                                charMatrix[position.first - 1][position.second + 1] == 'S'
                        )
                }
                return false
            }

            private fun isCorrectOnLeftDiagonal(position: Pair<Int, Int>): Boolean {
                val leftDiagonal =
                    listOf(
                        Pair(1, 1),
                        Pair(-1, -1),
                    )
                if (leftDiagonal.all {
                        isValidDirection(position, it, 1)
                    }
                ) {
                    return (
                        charMatrix[position.first - 1][position.second - 1] == 'S' &&
                            charMatrix[position.first + 1][position.second + 1] == 'M'
                    ) ||
                        (
                            charMatrix[position.first - 1][position.second - 1] == 'M' &&
                                charMatrix[position.first + 1][position.second + 1] == 'S'
                        )
                }
                return false
            }

            private fun isValidDirection(
                position: Pair<Int, Int>,
                direction: Pair<Int, Int>,
                index: Int,
            ): Boolean =
                position.first + direction.first * index >= 0 &&
                    position.second + direction.second * index >= 0 &&
                    position.first + direction.first * index < charMatrix.size &&
                    position.second + direction.second * index < charMatrix.size
        }
    }
}
