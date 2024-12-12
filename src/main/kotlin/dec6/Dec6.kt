package org.example.dec6

import org.example.InputReader
import org.example.common.Direction
import org.example.common.SquareMatrix
import org.example.common.Vector
import org.example.common.add
import org.example.common.at

fun main() {
    val inputString = InputReader().readFile("dec6/test.txt")
    println("Result: ${Dec6.task1(inputString)}")
    println("Result: ${Dec6.task2(inputString)}")
}

class Dec6 {
    companion object {
        fun task1(input: String): Int {
            val map = GuardMapMatrix(input.split(Regex("\n")).map { it.toList() })
            return map.getUniqueGuardPathPositions().size
        }

        fun task2(input: String): Int {
            val map = GuardMapMatrix(input.split(Regex("\n")).map { it.toList() })

            return map.positions
                .flatMapIndexed { rowIndex, row ->
                    row.mapIndexed { colIndex, char ->
                        map.doesObstructionCauseLoop(Pair(rowIndex, colIndex))
                    }
                }.count { it }
        }
    }

    private class GuardMapMatrix(
        positions: List<List<Char>>,
    ) : SquareMatrix<Char>(positions) {
        private val OBSTRUCTION_CHAR = '#'

        fun getUniqueGuardPathPositions(): Set<Pair<Int, Int>> {
            var guard = Guard(findInitialGuardPosition(), GuardDirection.UP)
            val guardUniquePositions = mutableSetOf<Pair<Int, Int>>()
            while (isMoveInsideBounds(guard.position, guard.guardDirection.vector())) {
                guardUniquePositions.add(guard.position)
                guard =
                    when (isStepUnobstructed(guard)) {
                        true -> guard.move()
                        false -> guard.rotate().move()
                    }
            }
            guardUniquePositions.add(guard.position)
            return guardUniquePositions
        }

        fun doesObstructionCauseLoop(obstruction: Pair<Int, Int>): Boolean {
            var guard = Guard(findInitialGuardPosition(), GuardDirection.UP)
            val guardUniquePositions = mutableSetOf<Guard>()
            while (isMoveInsideBounds(guard.position, guard.guardDirection.vector())) {
                guardUniquePositions.add(guard)
                guard =
                    when (isStepUnobstructed(guard, obstruction)) {
                        true -> guard.move()
                        false -> guard.rotate()
                    }
                if (guardUniquePositions.contains(guard)) {
                    return true
                }
            }
            return false
        }

        private fun findInitialGuardPosition(): Pair<Int, Int> {
            positions.forEachIndexed { i, chars ->
                chars.forEachIndexed { j, char ->
                    if (char == GuardDirection.UP.char()) {
                        return Pair(i, j)
                    }
                }
            }
            throw NoSuchElementException("Cannot find initial guard position")
        }

        private fun isStepUnobstructed(guard: Guard): Boolean = positions.at(guard.move().position) != OBSTRUCTION_CHAR

        private fun isStepUnobstructed(
            guard: Guard,
            additionalObstruction: Pair<Int, Int>,
        ): Boolean {
            val moved = guard.move().position
            return additionalObstruction != moved && positions.at(moved) != OBSTRUCTION_CHAR
        }

        private data class Guard(
            val position: Pair<Int, Int>,
            val guardDirection: GuardDirection,
        ) {
            fun move() =
                Guard(
                    position.add(guardDirection.vector()),
                    guardDirection,
                )

            fun rotate() = Guard(position, guardDirection.rotate())
        }

        private enum class GuardDirection {
            UP {
                override fun rotate() = RIGHT

                override fun vector() = Pair(-1, 0)

                override fun char(): Char = '^'
            },
            RIGHT {
                override fun rotate() = DOWN

                override fun vector() = Pair(0, 1)

                override fun char(): Char = '>'
            },
            DOWN {
                override fun rotate() = LEFT

                override fun vector() = Pair(1, 0)

                override fun char(): Char = 'v'
            },
            LEFT {
                override fun rotate() = UP

                override fun vector() = Pair(0, -1)

                override fun char(): Char = '<'
            }, ;

            abstract fun rotate(): GuardDirection

            abstract fun vector(): Vector

            abstract fun char(): Char
        }
    }
}
