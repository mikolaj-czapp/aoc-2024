package org.example.dec8

import org.example.InputReader
import org.example.common.Position
import org.example.common.SquareMatrix
import org.example.common.add
import org.example.common.cartesianProduct
import org.example.common.removeDiagonal
import org.example.common.sub
import org.example.common.times
import org.example.common.update

fun main() {
    val inputString = InputReader().readFile("dec8/input.txt")
    println("Result: ${Dec8.task1(inputString)}")
    println("Result: ${Dec8.task2(inputString)}")
}

class Dec8 {
    companion object {
        fun task1(input: String): Int {
            val matrix = prepareInput(input)
            val antennas = findAntennas(matrix)
            val antidotePositions =
                antennas.map { (_, positions) ->
                    val antennaPairs = positions.cartesianProduct().removeDiagonal()
                    antennaPairs.map { findAntidote(it.first, it.second) }.filter { matrix.isPositionInsideBounds(it) }
                }
            return antidotePositions
                .flatten()
                .toSet()
                .size
        }

        fun task2(input: String): Int {
            val matrix = prepareInput(input)
            val antennas = findAntennas(matrix)
            val antidotePositions =
                antennas.map { (_, positions) ->
                    val antennaPairs = positions.cartesianProduct().removeDiagonal()
                    antennaPairs
                        .map { findAntidotes(it.first, it.second, matrix.positions.size) }
                        .flatten()
                        .filter { matrix.isPositionInsideBounds(it) }
                }
            return antidotePositions
                .flatten()
                .toSet()
                .size
        }

        private fun prepareInput(input: String): SquareMatrix<Char> = SquareMatrix(input.split(Regex(("\n"))).map { it.toList() })

        private fun findAntennas(matrix: SquareMatrix<Char>): Map<Char, List<Position>> {
            val result = mutableMapOf<Char, MutableList<Position>>()
            matrix.positions.forEachIndexed { index, chars ->
                chars.forEachIndexed { idx, c ->
                    result.update(c, Position(index, idx))
                }
            }
            return result.filter { it.key != '.' }
        }

        private fun findAntidote(
            a1: Position,
            b1: Position,
        ): Position = findAntidotes(a1, b1, 1)[1]

        private fun findAntidotes(
            a1: Position,
            b1: Position,
            radius: Int,
        ): List<Position> {
            val vector = b1.sub(a1)
            return IntRange(0, radius).map {
                b1.add(vector.times(it))
            }
        }
    }
}
