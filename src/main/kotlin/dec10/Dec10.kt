package org.example.dec10

import org.example.InputReader
import org.example.common.Position
import org.example.common.SquareMatrix
import org.example.common.Vector
import org.example.common.add
import org.example.common.at
import org.example.common.isAdjacentTo

fun main() {
    val inputString = InputReader().readFile("dec10/test.txt")
    println("Result: ${Dec10.task1(inputString)}")
}

class Dec10 {
    companion object {
        fun task1(inputString: String): Int {
            val matrix = prepareInput(inputString)
            val trails = findTrails(matrix)
            return trails.trailStarts.sumOf {
                calculateTrailheadScore(matrix, it, trails.trailEnds)
            }
        }

        private fun prepareInput(input: String): SquareMatrix<Int> =
            SquareMatrix(input.split(Regex(("\n"))).map { it.toList().map { it.digitToInt() } })

        private fun calculateTrailheadScore(
            map: SquareMatrix<Int>,
            start: Position,
            ends: List<Position>,
        ) = ends.count { isPositionReachable(map, start, it) }

        private fun findTrails(map: SquareMatrix<Int>): Trails {
            val starts = mutableListOf<Position>()
            val ends = mutableListOf<Position>()
            map.positions.forEachIndexed { index, heights ->
                heights.forEachIndexed { idx, height ->
                    if (height == 9) {
                        ends.add(Position(index, idx))
                    }
                    if (height == 0) {
                        starts.add(Position(index, idx))
                    }
                }
            }
            return Trails(starts, ends)
        }

        private data class Trails(
            val trailStarts: List<Position>,
            val trailEnds: List<Position>,
        )

        private fun isPositionReachable(
            map: SquareMatrix<Int>,
            initialPosition: Position,
            destinationPosition: Position,
        ): Boolean {
            if (isPositionUphill(map, initialPosition, destinationPosition)) {
                return true
            }
            return Direction.entries
                .filter {
                    map.isPositionInsideBounds(initialPosition.add(it.vector()))
                }.filter {
                    isPositionUphill(map, initialPosition, initialPosition.add(it.vector()))
                }.any {
                    isPositionReachable(map, initialPosition.add(it.vector()), destinationPosition)
                }
        }

        private fun isPositionUphill(
            map: SquareMatrix<Int>,
            initialPosition: Position,
            destinationPosition: Position,
        ) = destinationPosition.isAdjacentTo(initialPosition) &&
            map.positions.at(destinationPosition) == map.positions.at(
                initialPosition,
            ) + 1

        private enum class Direction {
            UP {
                override fun vector() = Pair(-1, 0)
            },
            RIGHT {
                override fun vector() = Pair(0, 1)
            },
            DOWN {
                override fun vector() = Pair(1, 0)
            },
            LEFT {
                override fun vector() = Pair(0, -1)
            }, ;

            abstract fun vector(): Vector
        }
    }
}
